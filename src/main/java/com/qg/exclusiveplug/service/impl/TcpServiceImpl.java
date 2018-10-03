package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.cache.CacheMap;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.DMUrl;
import com.qg.exclusiveplug.enums.StateEnum;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.handlers.MyWebSocketHandler;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.TcpService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-23 10:37
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class TcpServiceImpl implements TcpService {
    private static final String CACHE_KEY = "devices";

    @Override
    public int messageHandler(String message) {
        assert message != null;
        List<Device> devices = analysisMessage(message);
        //将数据存放到Redis中进行缓存
        // TODO: 2018/9/26 0026  现在Redis不稳定，先换Map做缓存
//        if (!redisTemplate.hasKey(CACHE_KEY)) {
//            //不存在这个缓存
//            redisTemplate.opsForList().rightPushAll(CACHE_KEY, devices);
//        }else {
//            List<Device> devicesCache = (List) redisTemplate.opsForList().leftPop(CACHE_KEY);
//            devicesCache.addAll(devices);
//            redisTemplate.opsForList().rightPushAll(CACHE_KEY, devices);
//        }
//        return StateEnum.OK.getState();

        if (!CacheMap.containKey(CACHE_KEY)){
            CacheMap.put(CACHE_KEY, devices);
        }else {
            CacheMap.get(CACHE_KEY).addAll(devices);
        }
        return StateEnum.OK.getState();
    }

    /**
     * 将tcp收到的消息解析成设备对象
     * @param message   消息
     * @return  设备对象
     */
    private List<Device> analysisMessage(String message){
        //解析参数
        String[] parameters = message.split("end");
        List<String> list = Arrays.asList(parameters);
        List<Device> devices = new LinkedList<>();
        for(String s : list){
            //查看是哪个串口
            int index = (int)s.charAt(s.length()-1) - 48;
            s = s.substring(0, s.length()-1);
            log.info(s);
            //得到单个插口所有参数信息
            String[] plugs = s.split(",");
            String name = plugs[0].split(":")[0];
            double voltage = Double.parseDouble(plugs[0].split(":")[2]);
            double current = Double.parseDouble(plugs[1].split(":")[1]);
            double power = Double.parseDouble(plugs[2].split(":")[1]);
            double powerFactor = Double.parseDouble(plugs[3].split(":")[1]);
            double frequency = Double.parseDouble(plugs[4].split(":")[1]);
            double cumulativePower = Double.parseDouble(plugs[5].split(":")[1]);
            String currentTime = DateUtil.currentTime();

            Device device = new Device(index, name, current, voltage, power, powerFactor, frequency, currentTime, cumulativePower);

            // 向数据挖掘端发送设备信息
            ResponseData responseData = sendDeviceToDM(device);
            // 将数据传回给前端
            new MyWebSocketHandler().send(responseData);

            System.out.println(device.toString());
            devices.add(device);
        }

        return devices;
    }

    /**
     * 向数据挖掘端发送设备信息
     * @param device 设备信息
     */
    private ResponseData sendDeviceToDM(Device device){
        ResponseData responseData = new ResponseData();

        // 将设备信息放入交互类
        RequestData<Device> requestData = new RequestData<>();
        requestData.setData(device);
        InteractBigData interactBigData = null;

        // 与数据挖掘端交互
        try {
            interactBigData = HttpClientUtil.demandedCount(DMUrl.JUDGE_STATUS.getDMUrl(), requestData);
        } catch (IOException e) {
            log.debug("数据挖掘端连接失败");
            responseData.setStatus(Status.PREDICTED_FAILED.getStatus());
            e.printStackTrace();
            return responseData;
        }

        Data data;
        if(null != interactBigData) {
            data = new Data();
            // 设置设备状态与信息
            data.setStatus(interactBigData.getStatus());
            data.setDevice(device);
            responseData.setData(data);
            log.info(data.toString());
        } else {
            responseData.setStatus(Status.PREDICTED_FAILED.getStatus());
        }

        // 返回信息
        return responseData;
    }



}
