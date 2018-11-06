package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;

import javax.servlet.http.HttpSession;

/**
 * @author Wilder
 */
public interface QueryDeviceService {

    /**
     * 得到某天(24小时)/某月(30天)的用电量
     *
     * @param interactionData key(天/月/周)，时间
     * @return 状态码和用电量集合
     */
    ResponseData listPowerSum(InteractionData interactionData);

    /**
     * 控制用电器的开关
     *
     * @param interactionData 串口
     * @return 是否操控成功
     */
    ResponseData controller(InteractionData interactionData);

    /**
     * 增加用户关联设备信息
     * @param interactionData deviceUUID出厂序列号,用户关联设备信息
     * @param httpSession 用户信息
     * @return 操作结果
     */
    ResponseData addDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 增加用户关联设备信息
     * @param interactionData deviceUUID出厂序列号,用户关联设备信息
     * @param httpSession 用户信息
     * @return 操作结果
     */
    ResponseData queryDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 增加用户关联设备信息
     * @param interactionData deviceUUID出厂序列号,用户关联设备信息
     * @param httpSession 用户信息
     * @return 操作结果
     */
    ResponseData updateDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 增加用户关联设备信息
     * @param interactionData deviceUUID出厂序列号,用户关联设备信息
     * @param httpSession 用户信息
     * @return 操作结果
     */
    ResponseData delDeviceInfo(InteractionData interactionData, HttpSession httpSession);
}
