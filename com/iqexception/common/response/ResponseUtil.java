package com.iqexception.tool.response;

import com.ctrip.basebiz.accounts.mobile.request.filter.exception.AccountsMobileRequestFilterException;
import com.ctrip.train.tieyouflight.common.consts.CommonData;
import com.ctrip.train.tieyouflight.common.log.CLogger;
import com.ctrip.train.tieyouflight.common.log.entity.LogLevelEnum;
import com.ctriposs.baiji.rpc.common.HasResponseStatus;
import com.ctriposs.baiji.rpc.common.types.*;
import com.google.common.collect.Lists;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @author wang.wei
 * @since 2019/5/30
 */
public class ResponseUtil {
    /**
     * 以默认值初始化响应对象：ResponseStatusType：{ack："Success"} 、BaseResult {resultCode:0}等
     *
     * @param responseClazz
     * @return T
     */
    public static <T extends HasResponseStatus> T initResponse(Class<T> responseClazz) {
        try {
            T response = responseClazz.newInstance();
            ResponseStatusType responseStatusType = new ResponseStatusType();
            responseStatusType.setAck(AckCodeType.Success);
            responseStatusType.setTimestamp(Calendar.getInstance());
            response.setResponseStatus(responseStatusType);

            Field resultCode = ReflectionUtils.findField(responseClazz, CommonData.RESULT_CODE_FIELD, Integer.class);
            if (resultCode != null) {
                resultCode.setAccessible(true);
                resultCode.set(response, 1);
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static CommonResponse initCommonResponse() {
        return initResponse(CommonResponse.class);
    }

    public static <T extends HasResponseStatus> T buildUnLoginResponse(Class<T> responseClazz) {
        try {
            T response = responseClazz.newInstance();
            ResponseStatusType responseStatusType = new ResponseStatusType();
            responseStatusType.setAck(AckCodeType.Failure);
            responseStatusType.setTimestamp(Calendar.getInstance());
            response.setResponseStatus(responseStatusType);
            ErrorDataType errorDataType = new ErrorDataType();
            errorDataType.setErrorCode(AccountsMobileRequestFilterException.class.getSimpleName());
            errorDataType.setMessage("用户未登录");
            errorDataType.setSeverityCode(SeverityCodeType.Error);
            errorDataType.setErrorClassification(ErrorClassificationCodeType.FrameworkError);
            responseStatusType.setErrors(Lists.newArrayList(errorDataType));
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T extends HasResponseStatus> T buildWarnResponse(Class<T> responseClazz, String errorMessage) {
        return buildWarnResponse(responseClazz, ErrorCode.PARAM_ERROR, errorMessage);
    }

    public static <T extends HasResponseStatus> T buildWarnResponse(Class<T> responseClazz, int errorCode, String errorMessage) {
        return buildResponse(responseClazz, errorCode, errorMessage, LogLevelEnum.WARN);
    }

    public static <T extends HasResponseStatus> T buildErrorResponse(Class<T> responseClazz, String errorMessage) {
        return buildErrorResponse(responseClazz, ErrorCode.SYSTEM_ERROR, errorMessage);
    }

    public static <T extends HasResponseStatus> T buildErrorResponse(Class<T> responseClazz, int errorCode, String errorMessage) {
        return buildResponse(responseClazz, errorCode, errorMessage, LogLevelEnum.ERROR);
    }

    public static <T extends HasResponseStatus> T buildResponse(Class<T> responseClazz, int errorCode, String errorMessage, LogLevelEnum logLevel) {
        T response ;
        try {
            response = responseClazz.newInstance();
            ResponseStatusType responseStatusType = new ResponseStatusType();
            ErrorDataType errorData = new ErrorDataType();
            responseStatusType.setErrors(Lists.newArrayList(errorData));
            responseStatusType.setTimestamp(Calendar.getInstance());
            response.setResponseStatus(responseStatusType);
            Field resultCode = ReflectionUtils.findField(responseClazz, CommonData.RESULT_CODE_FIELD, Integer.class);
            if (resultCode != null) resultCode.setAccessible(true);
            Field resultMessage = ReflectionUtils.findField(responseClazz, CommonData.RESULT_MESSAGE_FIELD, String.class);
            if (resultMessage != null) resultMessage.setAccessible(true);

            if (logLevel == LogLevelEnum.ERROR) {
                responseStatusType.setAck(AckCodeType.Failure);
                CLogger.writeError(errorMessage);

            } else if (logLevel == LogLevelEnum.WARN) {
                responseStatusType.setAck(AckCodeType.Warning);
                CLogger.writeWarn(errorMessage);

            }
            errorData.setErrorCode(String.valueOf(errorCode));
            if (resultCode != null) resultCode.set(response, errorCode);

            errorData.setMessage(errorMessage);
            if (resultMessage != null) resultMessage.set(response, errorMessage);
            errorData.setErrorClassification(ErrorClassificationCodeType.ServiceError);
        } catch (Exception e) {
            //never happen
            CLogger.writeError(e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return response;
    }
    public static <T extends HasResponseStatus> T buildSuccessResponse(Class<T> responseClazz) {
        return initResponse(responseClazz);
    }
}
