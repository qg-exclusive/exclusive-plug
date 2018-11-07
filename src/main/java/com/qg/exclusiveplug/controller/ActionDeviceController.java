package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.ActionDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuaChen
 * time:2018年11月6日22:17:12
 * description:用户操控设备控制类
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("actiondevice")
public class ActionDeviceController {
    @Autowired
    private ActionDeviceService actionDeviceService;

    /**
     * 操控设备的开关
     * @param interactionData 设备端口以及开关信息
     * @return 处理结果
     */
    @RequestMapping("/controller")
    public ResponseData controller(@RequestBody InteractionData interactionData) {
        return actionDeviceService.controller(interactionData);
    }
}
