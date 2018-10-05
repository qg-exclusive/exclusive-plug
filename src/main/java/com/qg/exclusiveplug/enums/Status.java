package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    /**
     * 一切正常
     */
    NORMAL("2000"),

    /**
     * 预测失败
     */
    PREDICTED_FAILED("5001"),

    /**
     * 参数解析错误
     */
    PARAMETER_ERROR("4001"),
    ;

    private String status;

    public String getStatus(){
        return status;
    }
}
