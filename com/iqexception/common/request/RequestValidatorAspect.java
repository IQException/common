package com.ctrip.train.flight.common.job.support.com.iqexception.common.request;

import com.ctrip.train.tieyouflight.common.log.CLogger;
import com.ctrip.train.tieyouflight.common.response.BizException;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author wang.wei
 * @since 2019/6/4
 */
@Aspect
@Component
public class RequestValidatorAspect {

    private static final Map<Class, Validator> validators = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    @Before("@annotation(com.ctrip.train.tieyouflight.common.request.RequestValidator)")
    public void validate(JoinPoint jp) throws BizException {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();
        RequestValidator validatorAnn = method.getAnnotation(RequestValidator.class);
        if (validatorAnn != null) {
            Class<?> validatorClass = validatorAnn.value();
            Validator validator = validators.computeIfAbsent(validatorClass, clazz -> {
                try {
                    return (Validator) clazz.newInstance();
                } catch (Exception e) {
                    CLogger.writeError(e);
                }
                return null;
            });
            if (validator != null) {
                Object[] args = jp.getArgs();
                validator.validate(args[0]);
            }

        }

    }
}
