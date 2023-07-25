package com.iqexception.tool.job;

import qunar.tc.schedule.Parameter;

import java.util.List;

/**
 * @author wang.wei
 * @since 2019/9/30
 */
public interface IWorkerBiz<M> {

    List<M> querySince(Parameter parameter, String jobName, String process, int batch) throws Exception;

    List<M> queryAll(Parameter parameter, String jobName, int batch) throws Exception;

    void process(String jobName, M record) throws Exception;

    Long getId(String jobName, M record);

    String getProcess(String jobName, List<M> records);
}
