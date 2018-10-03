package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @RequestMapping("/pastpowersum")
    public ResponseData getPassPowerSum(@RequestBody InteractionData interactionData){
        return deviceService.listPowerSum(interactionData);
    }

    @RequestMapping("/controller")
    public ResponseData controller(@RequestBody InteractionData interactionData){
        return deviceService.controller(interactionData);
    }
}
