package com.iqexception.tool.response;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wang.wei
 * @since 2019/5/30
 */
public class MessageException extends RuntimeException {

    private int httpStatus = HttpServletResponse.SC_OK;

    public MessageException() {
    }

    public MessageException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(int httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public MessageException setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }
}
