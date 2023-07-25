package com.iqexception.tool.request;

import com.ctrip.train.tieyouflight.common.response.BizException;

/**
 * @author wang.wei
 * @since 2019/6/4
 */
public interface Validator<T> {

    public static final String VALID = "VALID";

    void validate(T request) throws BizException;
}
