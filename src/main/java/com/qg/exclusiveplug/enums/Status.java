package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    /**
     * 一切正常
     */
    NORMAL("2000"),

    PREDICTED_FAILED("5001")
    ;

    private String status;

    public String getStatus(){
        return status;
    }
}
