package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Wilder
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @RequestMapping("/pastpowersum")
    public ResponseData getPassPowerSum(){
        return new ResponseData();
    }
}
