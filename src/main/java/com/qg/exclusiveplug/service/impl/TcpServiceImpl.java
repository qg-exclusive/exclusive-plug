package com.qg.exclusiveplug.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.qg.exclusiveplug.constant.DMUrlEnum;
import com.qg.exclusiveplug.constant.DeviceStatusEnum;
import com.qg.exclusiveplug.constant.SmsEnum;
import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.handlers.MyWebSocketHandler;
import com.qg.exclusiveplug.map.LongWaitList;
import com.qg.exclusiveplug.map.TimeMap;
import com.qg.exclusiveplug.map.WebSocketHolder;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.TcpService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import com.qg.exclusiveplug.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author WilderGao
 * time 2018-09-23 10:37
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class TcpServiceImpl implements TcpService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final String CACHE_KEY = "devices";

    @Override
    public void messageHandler(String message) {
        analysisMessage(message);

    }

    /**
     * 将tcp收到的消息解析成设备对象
     *
     * @param message 消息
     */
    private void analysisMessage(String message) {
        //解析参数
        String[] list = message.split("end");
        for (String s : list) {
            //查看是哪个串口
            int index = (int) s.charAt(s.length() - 1) - 48;
            s = s.substring(0, s.length() - 1);
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
//            int status = sendDeviceToDM(device);
            int status = 0;
            // 更新待机信息
            try {
                standBy(device, status);
            } catch (ParseException e) {
                log.info("当前时间解析失败");
            }
            device.setStatus(status);
            redisTemplate.opsForList().leftPush(CACHE_KEY, device);

            // 如果需要发送数据
            if (WebSocketHolder.containsKey(index)) {
                // 将数据传回给前端
                send(device, status);
            }

//            log.info("接收到数据：" + device.toString());
        }
    }

    /**
     * 向数据挖掘端发送设备信息并得到设备状态
     *
     * @param device 设备信息
     */
    private int sendDeviceToDM(Device device) {
        ResponseData responseData = new ResponseData();

        // 将设备信息放入交互类
        RequestData<Device> requestData = new RequestData<>();
        requestData.setData(device);
        InteractBigData interactBigData = null;

        // 与数据挖掘端交互
        try {
            interactBigData = HttpClientUtil.demandedCount(DMUrlEnum.JUDGE_STATUS.getDMUrl(), requestData);
        } catch (IOException e) {
            log.debug("数据挖掘端连接失败");
            responseData.setStatus(StatusEnum.PREDICTED_FAILED.getStatus());
            e.printStackTrace();
        }

        if (null != interactBigData) {
            return interactBigData.getStatus();
        }

        // 返回信息
        return 0;
    }

    private void send(Device device, int status) {
        // 设置交互数据
        Data data = new Data();
        data.setDevice(device);
        data.setStatus(status);
        data.setLongAwaitList(LongWaitList.getLongWaitList());
        ResponseData responseData = new ResponseData();
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        responseData.setData(data);
        MyWebSocketHandler.send(device.getIndex(), responseData);
    }

    /**
     * 待机队列
     */
    private void standBy(Device device, int status) throws ParseException {
        // 待机时间
        final long standByTime = 5 * 60;
        int index = device.getIndex();
        // 判断长时间待机状态
        if (status == DeviceStatusEnum.STANDBY.getIndex()) {
            // 更新timeMap时间
            if (!TimeMap.containsKey(index)) {
                TimeMap.put(index, DateUtil.getCurrentDate());
            } else {
                int diffSecond = DateUtil.diffSecond(TimeMap.get(index), DateUtil.getCurrentDate());
                log.info(device.getName() + "已挂机" + diffSecond + "秒");
                // 判断待机持续时间
                if (diffSecond > standByTime) {
                    // 加入长时间待机警示队列并发送短信
                    if (!LongWaitList.contains(index)) {
                        try {
                            log.info("发送短信");
                            SmsUtil.sendSms("18929824809",
                                    "{\"name\":\" " + device.getName() + " \"}",
                                    SmsEnum.DEVICE_STANDBY_LONGTIME.getTemplateCode());
                        } catch (ClientException e) {
                            log.error("短信发送失败");
                            e.printStackTrace();
                        }
                        LongWaitList.add(index);
                    }
                } else {
                    // 移出长时间待机警示队列
                    if (LongWaitList.contains(index)) {
                        LongWaitList.remove(index);
                    }
                }
            }
        } else {
            // 更新最近检测时间和待机队列
            TimeMap.replace(index, DateUtil.getCurrentDate());
            if (LongWaitList.contains(index)) {
                LongWaitList.remove(index);
            }
        }
    }
}
