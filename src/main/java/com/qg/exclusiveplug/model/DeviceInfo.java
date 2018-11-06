package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    /**
     * 用电器端口
     */
    private int index;

    /**
     * 用电器名称
     */
    private String deviceName;

    /**
     * 用电器工作功率
     */
    private double deviceWorkPower;

    /**
     * 用电器待机功率
     */
    private double deviceStandbyPower;
}
