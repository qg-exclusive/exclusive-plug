package com.qg.exclusiveplug.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SerialPort {

    /**
     * 串口1
     */
    INDEX_1("PRINTER"),

    /**
     * 串口2
     */
    INDEX_2("WATERDISPENSER"),

    /**
     * 串口3
     */
    INDEX_3("PHONECHARGERS")
    ;
    /**
     * 设备名称
     */
    private String machineName;

    public String getSerialPort(){
        return machineName;
    }
}
