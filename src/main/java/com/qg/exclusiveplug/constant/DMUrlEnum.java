package com.qg.exclusiveplug.constant;

import lombok.AllArgsConstructor;

/**
 * 存放与数据挖掘端交互的URL
 *
 * @author Chen
 */
@AllArgsConstructor
public enum DMUrlEnum {

    /**
     * 实时设备数据
     */
    JUDGE_STATUS("http://127.0.0.1:5050/paicha/state/judge"),

    /**
     * 预测当天用电量
     */
    PREDICTED_POWERSUM("http://127.0.0.1:5050/paicha/predict/one");

    private String url;

    public String getDMUrl() {
        return url;
    }
}
