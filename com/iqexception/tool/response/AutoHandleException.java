package com.iqexception.tool.response;

import java.lang.annotation.*;

/**
 * @author wang.wei
 * @since 2019/5/30
 * 统一异常处理 \
 * @see ErrorCode 统一异常代码
 * @see ExceptionHandler 统一异常处理切面
 * @see BizException 对于业务异常，将code转换成message
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoHandleException {
}
