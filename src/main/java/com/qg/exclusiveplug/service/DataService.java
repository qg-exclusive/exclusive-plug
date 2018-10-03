package com.qg.exclusiveplug.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author Wilder
 */
public interface DataService {
    /**
     * 将某电器在某一天的总用电量存入数据库
     * @param machineName 用电器名称
     * @param date 天数
     */
    void listDevicesCPByDate(String machineName, String date);

    /**
     * 保存未读入的设备信息
     * @param multipartFile 设备信息
     */
    void saveDevice(MultipartFile multipartFile);

}
