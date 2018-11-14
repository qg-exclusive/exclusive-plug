package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.QueryDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author Wilder
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/querydevice")
public class QueryDeviceController {
    @Resource
    private QueryDeviceService queryDeviceService;

    @RequestMapping("/pastpowersum")
    public ResponseData getPassPowerSum(@RequestBody InteractionData interactionData) {
        return queryDeviceService.listPowerSum(interactionData);
    }

}
