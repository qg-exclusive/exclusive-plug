package com.qg.exclusiveplug.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.exclusiveplug.model.*;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-03 15:42:20
 * description 用来承载所需响应给前端的数据
 */
@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {

    /**
     * 设备资料
     */
    Device device;

    /**
     * 设备状态
     */
    int status;

    /**
     * 用电量
     */
    Double[] powerSums;

    /**
     * 用电量
     */
    List<PowerSum> powerSumList;

    /**
     * 用电量
     */
    PowerSum powerSum;

    /**
     * 长时间待机队列
     */
    List<Integer> longAwaitList;

    /**
     * 用户信息
     */
    User user;

    /**
     * 设备信息
     */
    DeviceInfo deviceInfo;

    /**
     * 设备被操作日志
     */
    DeviceLog deviceLog;

}
