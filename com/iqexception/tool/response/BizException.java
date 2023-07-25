package com.iqexception.tool.response;

/**
 * @author wang.wei
 * @since 2019/4/18
 */
public class BizException extends MessageException {

    private int errorCode;

    public BizException(String message) {
        super(message);
    }

    public BizException() {
    }

    public BizException(int errorCode) {
        this.errorCode = errorCode;
    }

    public BizException(String message, int httpStatus, int errorCode) {
        super(message, httpStatus);
        this.errorCode = errorCode;
    }
    public BizException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BizException(int httpStatus, int errorCode) {
        super(httpStatus);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public BizException setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}
