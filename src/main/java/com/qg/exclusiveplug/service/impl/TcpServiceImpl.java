package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.cache.CacheMap;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.DMUrl;
import com.qg.exclusiveplug.enums.DeviceStatus;
import com.qg.exclusiveplug.enums.StateEnum;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.handlers.MyWebSocketHandler;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.TcpService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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

    /**
     * 记录某串口最后待机的时间
     */
    private static Map<Integer, Date> timeMap = new HashMap<>();
    /**
     * 记录长时间待机队列
     */
    private static List<Integer> longAwaitList = new ArrayList<>();

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

        if (!CacheMap.containKey(CACHE_KEY)) {
            CacheMap.put(CACHE_KEY, devices);
        } else {
            CacheMap.get(CACHE_KEY).addAll(devices);
        }
        return StateEnum.OK.getState();
    }

    /**
     * 将tcp收到的消息解析成设备对象
     *
     * @param message 消息
     * @return 设备对象
     */
    private List<Device> analysisMessage(String message) {
        //解析参数
        String[] parameters = message.split("end");
        List<String> list = Arrays.asList(parameters);
        List<Device> devices = new LinkedList<>();
        for (String s : list) {
            //查看是哪个串口
            int index = (int) s.charAt(s.length() - 1) - 48;
            s = s.substring(0, s.length() - 1);
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

            // 如果需要发送数据
            if(MyWebSocketHandler.getIndex() != 0){
                // 向数据挖掘端发送设备信息
                int status = sendDeviceToDM(device);
                // 将数据传回给前端
                send(device, status);
            }

            devices.add(device);
        }

        return devices;
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
            interactBigData = HttpClientUtil.demandedCount(DMUrl.JUDGE_STATUS.getDMUrl(), requestData);
        } catch (IOException e) {
            log.debug("数据挖掘端连接失败");
            responseData.setStatus(Status.PREDICTED_FAILED.getStatus());
            e.printStackTrace();
        }

        if (null != interactBigData) {
            return interactBigData.getStatus();
        }

        // 返回信息
        return 0;
    }

    private void send(Device device, int status) {
        log.info("状态" + status);
        Data data = new Data();
        int index = device.getIndex();
        // 判断长时间待机状态
        if (status == DeviceStatus.STANDBY.getIndex()) {
            // 更新timeMap时间
            if (!timeMap.containsKey(index)) {
                timeMap.put(index, DateUtil.getCurrentDate());
            } else {
                Date currentTime = DateUtil.getCurrentDate();
                int diffHour = DateUtil.diffHours(timeMap.get(index), currentTime);
                // 判断待机持续时间
                if (diffHour > 5) {
                    // 加入长时间待机警示队列
                    if (!longAwaitList.contains(index)) {
                        longAwaitList.add(index);
                    }
                } else {
                    // 移出长时间待机警示队列
                    if(longAwaitList.contains(index)){
                        longAwaitList.remove(Integer.valueOf(index));
                    }
                }
            }
        }else {
            // 更新最近检测时间和待机队列
            timeMap.replace(index, DateUtil.getCurrentDate());
            longAwaitList.remove(Integer.valueOf(index));
        }

        // 发送特定串口数据
        if (device.getIndex() == MyWebSocketHandler.getIndex()) {
            // 设置交互数据
            data.setDevice(device);
            data.setStatus(status);
            data.setLongAwaitList(longAwaitList);
            ResponseData responseData = new ResponseData();
            responseData.setStatus(Status.NORMAL.getStatus());
            responseData.setData(data);
            MyWebSocketHandler.send(responseData);
        }
    }
}
