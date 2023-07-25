package com.iqexception.tool.request;

import com.ctrip.train.tieyouflight.common.log.CLogger;
import com.ctrip.train.tieyouflight.common.response.BizException;
import com.ctrip.train.tieyouflight.common.response.ErrorCode;
import com.ctrip.train.tieyouflight.common.utils.FormatValidateUtil;
import com.ctriposs.baiji.rpc.common.apidoc.FieldDoc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author wang.wei
 * @since 2019/6/4
 */
public class CommonRequestValidator {

    private Class<?> clazz;

    private final StringBuilder messageBuilder = new StringBuilder();

    private static final String INVALID_EMPTY_MESSAGE_SUFFIX = "不能为空";

    private static final String INVALID_FORMAT_MESSAGE_SUFFIX = "格式有误";

    private static final String INVALID_NUMBER_MESSAGE_SUFFIX = "非法";

    public CommonRequestValidator(Object request) {
        if (request == null)
            throw new RuntimeException("request is null!");
        this.clazz = request.getClass();

    }

    public static void handleInvalidMessage(String invalidMessage) throws BizException {
        if (StringUtils.isNotBlank(invalidMessage) && !Validator.VALID.equals(invalidMessage))
            throw new BizException(invalidMessage, ErrorCode.PARAM_ERROR);
    }

    /**
     * @return "VALID" or Invalid Message
     */
    public String validate() {
        if (messageBuilder.length() > 0)
            return messageBuilder.toString();
        else
            return Validator.VALID;
    }

    /**
     * @param fieldValue
     * @param fieldComment 字段注释：例如 uid fieldComment: 用户id ，报错：用户id不能为空
     * @return
     */
    public CommonRequestValidator notNull(Object fieldValue, String fieldComment) {
        if (fieldValue == null) {
            buildInvalidEmptyMessageByFieldComment(fieldComment);
        }
        return this;
    }

    public CommonRequestValidator greaterThanZero(Object fieldValue, String fieldComment) {
        if(fieldValue == null){
            buildInvalidNumberMessageByFieldComment(fieldComment);
        }else{
            String number = String.valueOf(fieldValue);
            if(!NumberUtils.isNumber(number)){
                buildInvalidNumberMessageByFieldComment(fieldComment);
            }
            else if(Double.parseDouble(number) <=0){
                buildInvalidNumberMessageByFieldComment(fieldComment);
            }
        }

        return this;

    }

    /**
     * @param fieldValue
     * @param fieldName  从field的注释获取提示信息
     * @return
     */
    public CommonRequestValidator notNull2(Object fieldValue, String fieldName) {
        if (fieldValue == null) {
            buildInvalidEmptyMessageByFieldName(fieldName);
        }
        return this;
    }

    /**
     * @param fieldValue
     * @param fieldName  从field的注释获取提示信息
     * @return
     */
    public CommonRequestValidator notEmpty2(Object fieldValue, String fieldName) {
        if (fieldValue != null && !isEmptyString(fieldValue) && !isEmptyCollection(fieldValue))
            return this;
        else
            buildInvalidEmptyMessageByFieldName(fieldName);
        return this;
    }

    /**
     * @param fieldValue
     * @param fieldComment 字段注释：例如 uid fieldComment: 用户id ，报错：用户id不能为空
     * @return
     */
    public CommonRequestValidator notEmpty(Object fieldValue, String fieldComment) {
        if (fieldValue != null && !isEmptyString(fieldValue) && !isEmptyCollection(fieldValue))
            return this;
        else
            buildInvalidEmptyMessageByFieldComment(fieldComment);
        return this;
    }

    public CommonRequestValidator validPhoneNumber(Object fieldValue) {
        return validPhoneNumber(fieldValue, "手机号");
    }

    public CommonRequestValidator validPhoneNumber(Object fieldValue, String fieldComment) {
        if (fieldValue != null && FormatValidateUtil.isValidPhoneNumber(String.valueOf(fieldValue)))
            return this;
        else if (fieldValue == null || isEmptyString(fieldValue)) {
            buildInvalidFormatMessageByFieldComment(fieldComment);
        } else {
            buildInvalidFormatMessageByFieldComment(fieldComment);
        }
        return this;
    }

    public CommonRequestValidator validPhoneNumber2(Object fieldValue, String fieldName) {
        if (fieldValue != null && FormatValidateUtil.isValidPhoneNumber(String.valueOf(fieldValue)))
            return this;
        else if (fieldValue == null || isEmptyString(fieldValue)) {
            buildInvalidFormatMessageByFieldName(fieldName);
        } else {
            buildInvalidFormatMessageByFieldName(fieldName);
        }
        return this;
    }

    public CommonRequestValidator validIdCardNumber(Object fieldValue) {

        return validIdCardNumber(fieldValue, "身份证号");

    }


    public CommonRequestValidator validIdCardNumber(Object fieldValue, String fieldComment) {
        if (fieldValue != null && FormatValidateUtil.isValidIdCardNumber(String.valueOf(fieldValue)))
            return this;
        else if (fieldValue == null || isEmptyString(fieldValue)) {
            buildInvalidFormatMessageByFieldComment(fieldComment);
        } else {
            buildInvalidFormatMessageByFieldComment(fieldComment);
        }
        return this;
    }

    public CommonRequestValidator validIdCardNumber2(Object fieldValue, String fieldName) {
        if (fieldValue != null && FormatValidateUtil.isValidIdCardNumber(String.valueOf(fieldValue)))
            return this;
        else if (fieldValue == null || isEmptyString(fieldValue)) {
            buildInvalidFormatMessageByFieldName(fieldName);
        } else {
            buildInvalidFormatMessageByFieldName(fieldName);
        }
        return this;
    }


    private void buildInvalidEmptyMessageByFieldName(String fieldName) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field == null) {
            logInvalidFieldName(fieldName);
        } else {
            if (messageBuilder.length() > 0) messageBuilder.append(";");
            messageBuilder.append("\"").append(findComment(field)).append("\"").append(INVALID_EMPTY_MESSAGE_SUFFIX);
        }
    }

    private void buildInvalidEmptyMessageByFieldComment(String fieldComment) {
        if (messageBuilder.length() > 0) messageBuilder.append(";");
        messageBuilder.append("\"").append(fieldComment).append("\"").append(INVALID_EMPTY_MESSAGE_SUFFIX);

    }

    private void buildInvalidNumberMessageByFieldComment(String fieldComment) {

        if (messageBuilder.length() > 0) messageBuilder.append(";");
        messageBuilder.append("\"").append(fieldComment).append("\"").append(INVALID_NUMBER_MESSAGE_SUFFIX);
    }

    private void buildInvalidFormatMessageByFieldName(String fieldName) {
        Field field = ReflectionUtils.findField(clazz, fieldName);
        if (field == null) {
            logInvalidFieldName(fieldName);
        } else {
            if (messageBuilder.length() > 0) messageBuilder.append(";");
            messageBuilder.append("\"").append(findComment(field)).append("\"").append(INVALID_FORMAT_MESSAGE_SUFFIX);
        }
    }

    private void buildInvalidFormatMessageByFieldComment(String fieldComment) {

        if (messageBuilder.length() > 0) messageBuilder.append(";");
        messageBuilder.append("\"").append(fieldComment).append("\"").append(INVALID_FORMAT_MESSAGE_SUFFIX);

    }

    private String findComment(Field field) {
        field.setAccessible(true);
        FieldDoc fieldDoc = field.getAnnotation(FieldDoc.class);
        return fieldDoc.value();
    }

    private boolean isEmptyCollection(Object object) {
        if (Collection.class.isAssignableFrom(object.getClass())) {
            return ((Collection) object).size() == 0;
        } else if (Map.class.isAssignableFrom(object.getClass())) {
            return ((Map) object).size() == 0;
        } else {
            return false;
        }
    }

    private boolean isEmptyString(Object object) {
        return String.class.isAssignableFrom(object.getClass()) && StringUtils.isBlank((String) object);

    }

    private void logInvalidFieldName(String fieldName) {
        CLogger.writeWarn("Request Validation", String.format("can't find field %s in class %s ", fieldName, clazz.getName()));
    }


}

