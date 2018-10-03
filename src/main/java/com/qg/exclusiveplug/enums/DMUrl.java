package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

/**
 * 存放与数据挖掘端交互的URL
 *
 * @author Chen
 */
@AllArgsConstructor
public enum  DMUrl {

    /**
     * 实时设备数据
     */
    JUDGE_STATUS("http://10.21.48.11:5050/paicha/state/judge");

    private String url;

    public String getDMUrl() {
        return url;
    }
}
