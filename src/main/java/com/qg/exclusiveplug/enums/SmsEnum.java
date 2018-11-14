package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SmsEnum {

    /**
     * 用电器长时间待机
     */
    DEVICE_STANDBY_LONGTIME("SMS_148380226"),

    /**
     * 用户操作
     */
    USER_ACTION("SMS_150184129");

    private String templateCode;

    public String getTemplateCode() {
        return templateCode;
    }
}
