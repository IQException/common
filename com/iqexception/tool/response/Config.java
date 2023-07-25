package com.iqexception.tool.response;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.Map;

/**
 * @author wang.wei
 * @since 2019/5/31
 */
@Configuration
public class Config {

    private Map<String, String> customMessages = Maps.newHashMap();

    public Config(ObjectProvider<Messages> messagesProvider) {
        if (messagesProvider.getIfAvailable() != null) {
            customMessages = messagesProvider.getIfAvailable().messages();
        }
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("i18n/error");
        messageSource.addBasenames("i18n/message");

        return new MessageSource() {
            @Override
            public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
                if (customMessages != null && customMessages.get(code) != null) {
                    return messageSource.getMessage(code, args, customMessages.get(code), locale);
                }
                return messageSource.getMessage(code, args, defaultMessage, locale);
            }

            @Override
            public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
                if (customMessages != null && customMessages.get(code) != null) {
                    return messageSource.getMessage(code, args, customMessages.get(code), locale);
                }
                return messageSource.getMessage(code, args, locale);
            }

            @Override
            public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
                return messageSource.getMessage(resolvable, locale);
            }
        };
    }

    @Bean
    public ExceptionHandler exceptionHandler() {
        return new ExceptionHandler(messageSource());
    }
}
