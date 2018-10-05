package com.qg.exclusiveplug.enums;

public enum DeviceStatus {

    /**
     * 断电
     */
    OUTAGE(0),

    /**
     * 待机
     */
    STANDBY(1),

    /**
     * 正常
     */
    NORMAL(2),

    /**
     * 故障
     */
    BROKEN(3)
    ;

    private int index;

    DeviceStatus(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
