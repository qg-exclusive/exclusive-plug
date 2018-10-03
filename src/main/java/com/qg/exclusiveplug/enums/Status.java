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
     * 前端数据错误
     */
    DATA_FAILED("5002")
    ;

    private String status;

    public String getStatus(){
        return status;
    }
}
