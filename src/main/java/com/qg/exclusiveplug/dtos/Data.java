package com.qg.exclusiveplug.dtos;

import com.qg.exclusiveplug.model.Device;

/**
 * @author Chen
 * time 2018-10-03 15:42:20
 * description 用来承载所需响应给前端的数据
 */
@lombok.Data
public class Data {

    /**
     * 设备资料
     */
    Device device;

    /**
     * 设备状态
     */
    int status;
}
