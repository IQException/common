package com.iqexception.tool.response;

/**
 * @author wang.wei
 * @since 2019/5/30
 * 错误码：规则：<0 的为系统错误； >0 的业务错误；0 为正常结果（因为int默认初始化为0，不需要特殊处理，作为正常情况比较方便）
 * 一共五位数字（暂时AA只有一位）：AA-BB-CC ；AA表示业务或项目，如1代表订单，2代表供应商，3代表用户等等；
 * BB表示：1. 功能模块，每个业务之间可能会有重合的功能，如参数校验，暂定为01；2. 更细分的业务，比如用户的发票，暂定为11; 3. 00 直接表示AA 业务下的错误
 * CC表示具体的错误。
 *
 * 系统错误比较少就随便定义了，暂定-1为笼统的系统错误（如果调用方不关心错误类型，如大部分App请求，都可以直接返回系统错误），其他如服务异常之类的根据需求添加
 */
public class ErrorCode {

    /**
     * 系统异常
     */
    public static final int SYSTEM_ERROR = -1;

    /**
     * 服务不可用
     */
    public static final int SERVICE_UNAVAILABLE = -2;

    /**
     * 系统太忙
     */
    public static final int SYSTEM_TOO_BUSY = -3;

    /**
     * 参数异常
     */
    public static final int PARAM_ERROR = -4;
    /**
     * 数据库异常
     */
    public static final int DB_ERROR = -11;


    // region 订单
    /**
     * 订单不存在
     */
    public static final int ORDER_NOT_EXIST = 10001;
    // endregion 订单

    // region 供应商
    /**
     * 供应商不存在
     */
    public static final int VENDOR_NOT_EXIST = 20001;
    // endregion 供应商

    // region 用户
    /**
     * 用户不存在
     */
    public static final int USER_NOT_EXIST = 30001;
    /**
     * 发票信息不存在
     */
    public static final int USER_INVOICE_NOT_EXIST= 31101;
    /**
     * 非法的抬头类型
     */
    public static final int USER_INVOICE_TYPE_INVALID = 31102;
    // endregion 用户

}
