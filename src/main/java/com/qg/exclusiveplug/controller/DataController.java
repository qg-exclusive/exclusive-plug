package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/data")
@Slf4j
public class DataController {
    @Resource
    private DataService dataService;

    /**
     * 统计信息
     * @param device 设备名称/统计日期
     */
    @RequestMapping("savedaypower")
    public void listWholePower(@RequestBody Device device){
        dataService.listDevicesCPByDate(device.getName(), device.getDate());
    }

    @RequestMapping(value = "savedevice", method = RequestMethod.POST)
    public void saveDevice(@RequestParam("file")MultipartFile multipartFile){
        dataService.saveDevice(multipartFile);
    }
}
