package com.ctrip.train.flight.common.job.support;

import com.ctrip.framework.foundation.Foundation;
import com.ctrip.train.flight.common.common.util.CacheUtil;
import com.ctrip.train.tieyouflight.common.credis.CacheManager;
import com.ctrip.train.tieyouflight.common.log.CLogger;
import com.ctrip.train.tieyouflight.common.utils.AsyncUtil;
import com.dianping.cat.async.CatAsync;
import com.google.common.base.MoreObjects;
import com.google.common.util.concurrent.MoreExecutors;
import credis.java.client.CacheProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import qunar.tc.schedule.Parameter;
import qunar.tc.schedule.TaskHolder;
import qunar.tc.schedule.TaskMonitor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * @author wang.wei
 * @since 2019/9/30
 * 抽象出任务处理流程：取批量任务->处理（有可能多线程）->记录进度-取下一批任务，循环往复，直至任务处理完毕。
 */
public abstract class AbstractWorker<M> {
    private CacheProvider cacheProvider = CacheManager.getInstance();
    @Autowired
    private IWorkerBiz<M> workerBiz;

    /**
     * 一次性取的任务数
     */
    private int batch = 100;

    /**
     * 处理某一条数据的间隔时间（秒）
     */
    private int interval = 0;

    /**
     * 多线程处理
     */
    private int concurrency = 1;

    /**
     * 处理两个批次数据之间的等待时间（毫秒）
     */
    private int waitIoMills = 1000;

    /**
     * 是否循环处理
     */
    private boolean isCycle = false;

    private ExecutorService executor = CatAsync.wrap(Executors.newCachedThreadPool());

    private Executor directExecutor = CatAsync.wrap(MoreExecutors.directExecutor());

    private static final String WORK_RECORD_TAG = "workRecordId";

    protected void process(String jobName, Parameter parameter) {

        int _batch = MoreObjects.firstNonNull(parameter.getProperty("batch", Integer.class), batch);
        int _interval = MoreObjects.firstNonNull(parameter.getProperty("interval", Integer.class), interval);
        int _waitIoMills = MoreObjects.firstNonNull(parameter.getProperty("waitIoMills", Integer.class), waitIoMills);
        int _concurrency = MoreObjects.firstNonNull(parameter.getProperty("concurrency", Integer.class), concurrency);
        boolean _isCycle = MoreObjects.firstNonNull(parameter.getProperty("isCycle", Boolean.class), isCycle);

        if (_waitIoMills > 0) {
            //如果要控制执行频率的话就不应该用多线程
            _concurrency = 1;
        }

        String config = String.format(jobName, " %s started ! batch:%s,interval:%s,concurrency:%s,waitIoMills:%s", jobName, _batch, _interval, _concurrency, _waitIoMills);
        CLogger.writeInfo(jobName, config);

        TaskMonitor monitor = TaskHolder.getKeeper();
        String processKey = Foundation.app().getAppId() + ":" + jobName + ":process";

        try {
            while (!monitor.isStopped()) {
                //停10ms等redis同步，最好用强一致性存储
                Thread.sleep(10);
                //获取当前进度
                String process = cacheProvider.get(processKey);

                List<M> records;
                if (StringUtils.isBlank(process)) {
                    //从头开始
                    records = workerBiz.queryAll(parameter, jobName, _batch);
                } else {
                    //从当前进度开始
                    records = workerBiz.querySince(parameter, jobName, process, _batch);
                }

                if (CollectionUtils.isNotEmpty(records)) {
                    CLogger.writeInfo(jobName, String.format("fetch record size:%s", records.size()));

                    Semaphore semaphore = new Semaphore(_concurrency);
                    List<Future> futures = new ArrayList<>(_concurrency);

                    for (M record : records) {
                        if (monitor.isStopped()) break;

                        CLogger.addTag("job", jobName);
                        CLogger.addTag(WORK_RECORD_TAG, String.valueOf(workerBiz.getId(jobName, record)));
                        //控制执行频率
                        String lastProcessKey = Foundation.app().getAppId() + ":" + jobName + ":lastProcessTime:" + workerBiz.getId(jobName, record);
                        if (_interval > 0 && !CacheUtil.setNx(lastProcessKey, String.valueOf(System.currentTimeMillis()), Duration.ofSeconds(_interval))) {
                            CLogger.writeInfo(jobName, String.format("skip the record. id:%s", workerBiz.getId(jobName, record)));
                            continue;
                        }
                        //并行处理
                        if (_concurrency > 1) {
                            if (!semaphore.tryAcquire()) {
                                AsyncUtil.waitAll2(futures);
                                semaphore = new Semaphore(_concurrency);
                                futures = new ArrayList<>(_concurrency);
                                semaphore.acquire();
                            }
                            Future future = executor.submit(() -> process0(jobName, record));
                            futures.add(future);
                        } else {
                            directExecutor.execute(() -> process0(jobName, record));
                        }

                    }
                    if (_concurrency > 1) {
                        AsyncUtil.waitAll2(futures);
                    }
                    String newProcess = workerBiz.getProcess(jobName, records);
                    //任务量不足一批次，说明任务已处理完
                    if (records.size() < _batch || Objects.equals(process, newProcess)) {
                        cacheProvider.del(processKey);
                        CLogger.writeInfo(jobName, String.format("process finished ! last record is: %s", workerBiz.getId(jobName, records.get(records.size() - 1))));
                        if (!_isCycle) return;
                    } else {
                        cacheProvider.setex(processKey, (int) Duration.ofDays(180).getSeconds(), newProcess);
                    }
                } else {
                    cacheProvider.del(processKey);
                    if (!_isCycle) return;
                    if (waitIoMills > 0) {
                        Thread.sleep(waitIoMills);
                    }
                }

            }
        } catch (Exception e) {
            CLogger.writeError(jobName, e);
            throw new RuntimeException(e);
        }

    }

    private void process0(String jobName, M record) {
        CLogger.addTag("job", jobName);
        CLogger.addTag(WORK_RECORD_TAG, String.valueOf(workerBiz.getId(jobName, record)));
        try {
            workerBiz.process(jobName, record);
            CLogger.writeInfo(jobName, String.format("process succeed! id:%s", workerBiz.getId(jobName, record)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            CLogger.delTag(WORK_RECORD_TAG);
            CLogger.delTag("job");
        }
    }


}
