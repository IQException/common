package com.iqexception.tool.request;

import java.lang.annotation.*;

/**
 * @author wang.wei
 * @since 2019/6/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestValidator {

    /**
     * validator class
     * @return
     */
     Class<?> value();
}
