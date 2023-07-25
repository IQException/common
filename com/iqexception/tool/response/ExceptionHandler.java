package com.iqexception.tool.response;

import com.ctrip.train.tieyouflight.common.consts.CommonData;
import com.ctrip.train.tieyouflight.common.log.CLogger;
import com.ctrip.train.tieyouflight.common.utils.JsonSerializer;
import com.ctrip.train.tieyouflight.common.utils.MessageUtil;
import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.types.AckCodeType;
import com.ctriposs.baiji.rpc.common.types.ErrorClassificationCodeType;
import com.ctriposs.baiji.rpc.common.types.ErrorDataType;
import com.ctriposs.baiji.rpc.common.types.ResponseStatusType;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * @author wang.wei
 * @since 2019/5/30
 */
@Order(Integer.MIN_VALUE)
@Aspect
public class ExceptionHandler {

    private MessageSource messageSource;

    public ExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Around("@annotation(com.ctrip.train.tieyouflight.common.response.AutoHandleException)" +
            "||@within(com.ctrip.train.tieyouflight.common.response.AutoHandleException)")
    public Object proxy(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Class returnType = method.getReturnType();
        Object result ;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            result = handleException(throwable, returnType);
        }
        return result;
    }

    private <T extends HasResponseStatus> T handleException(Throwable throwable, Class<T> returnType) {
        if (!HasResponseStatus.class.isAssignableFrom(returnType)) {
            throw new RuntimeException(throwable.getMessage(), throwable);
        } else {
            T response;
            try {
                response = returnType.newInstance();

                ResponseStatusType responseStatusType = new ResponseStatusType();
                ErrorDataType errorData = new ErrorDataType();
                responseStatusType.setErrors(Lists.newArrayList(errorData));
                responseStatusType.setTimestamp(Calendar.getInstance());
                response.setResponseStatus(responseStatusType);
                Field resultCode = ReflectionUtils.findField(returnType, CommonData.RESULT_CODE_FIELD, Integer.class);
                if (resultCode != null) resultCode.setAccessible(true);
                Field resultMessage = ReflectionUtils.findField(returnType, CommonData.RESULT_MESSAGE_FIELD, String.class);
                if (resultMessage != null) resultMessage.setAccessible(true);
                int errorCode;
                String errorMessage;
                if (throwable instanceof BizException) {
                    responseStatusType.setAck(AckCodeType.Warning);
                    errorCode = ((BizException) throwable).getErrorCode() == 0 ? ErrorCode.SYSTEM_ERROR
                            : ((BizException) throwable).getErrorCode();

                    errorMessage = MoreObjects.firstNonNull(throwable.getMessage(),
                            MessageUtil.message(String.valueOf(errorCode), messageSource));
                    CLogger.writeWarn(throwable);
                } else if (throwable instanceof InternalException) {
                    responseStatusType.setAck(AckCodeType.Failure);
                    errorCode = ((InternalException) throwable).getErrorCode() == 0 ? ErrorCode.SYSTEM_ERROR
                            : ((InternalException) throwable).getErrorCode();
                    CLogger.writeError(throwable);
                    errorMessage = MessageUtil.message(String.valueOf(errorCode), messageSource);
                } else {
                    response.getResponseStatus().setAck(AckCodeType.Failure);
                    errorCode = ErrorCode.SYSTEM_ERROR;
                    CLogger.writeError(throwable);
                    errorMessage = MessageUtil.message(String.valueOf(errorCode), messageSource);
                }
                errorData.setErrorCode(String.valueOf(errorCode));
                if (resultCode != null) resultCode.set(response, errorCode);

                errorData.setMessage(errorMessage);
                if (resultMessage != null) resultMessage.set(response, errorMessage);
                errorData.setErrorClassification(ErrorClassificationCodeType.ServiceError);

                CLogger.writeInfo(String.format("response:%s", JsonSerializer.serialize(response)));
                return response;
            } catch (Exception e) {
                //never happen
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}
