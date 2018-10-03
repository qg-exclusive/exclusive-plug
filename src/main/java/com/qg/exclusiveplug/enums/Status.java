package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    /**
     * 一切正常
     */
    NORMAL("2000"),

    /**
     * 参数解析错误
     */
    PARAMETER_ERROR("4003"),

    PREDICTED_FAILED("5001")
    ;

    private String status;

    public String getStatus(){
        return status;
    }
}
