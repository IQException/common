package com.iqexception.tool.response;

/**
 * @author wang.wei
 * @since 2019/4/18
 */
public class InternalException extends MessageException {

    private int errorCode;

    public InternalException(String message) {
        super(message);
    }

    public InternalException() {
    }

    public InternalException(int errorCode) {
        this.errorCode = errorCode;
    }

    public InternalException(String message, int httpStatus, int errorCode) {
        super(message, httpStatus);
        this.errorCode = errorCode;
    }
    public InternalException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InternalException(int httpStatus, int errorCode) {
        super(httpStatus);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public InternalException setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}
