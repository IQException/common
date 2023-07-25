package com.iqexception.tool.response;

import qunar.tc.qconfig.client.spring.QConfig;

import java.util.Map;

/**
 * @author wang.wei
 * @since 2019/5/31
 */
public class QConfigMessages implements Messages {

    @QConfig("message.properties")
    private Map<String, String> qconfigMessages;

    @Override
    public Map<String, String> messages() {
        return qconfigMessages;
    }
}
