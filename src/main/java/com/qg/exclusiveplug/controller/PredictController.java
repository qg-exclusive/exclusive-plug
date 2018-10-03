package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WilderGao
 * time 2018-10-03 23:55
 * motto : everything is no in vain
 * description 预测接口
 */
@RestController(value = "/predicted")
public class PredictController {

    /**
     * 预测今天的用电量
     * @param request   前端请求参数，包括串口号index 和时间time
     * @return  预测之后的结果
     */
    @PutMapping(value = "/nowtpowersum")
    public ResponseData predictTodayPowerSum(@RequestBody InteractionData request){
        //todo 还没写完
        return null;
    }
}
