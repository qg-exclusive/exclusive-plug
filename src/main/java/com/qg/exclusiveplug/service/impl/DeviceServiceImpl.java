package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.DeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.handlers.TcpHandler;
import com.qg.exclusiveplug.model.PowerSum;
import com.qg.exclusiveplug.service.DeviceService;
import com.qg.exclusiveplug.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 得到某天(24小时)/某月(30天)的用电量
     *
     * @param interactionData key(天/月/周)，时间
     * @return 状态码和用电量集合
     */
    @Override
    public ResponseData listPowerSum(InteractionData interactionData) {
        if(interactionData.getKey() > 6 || interactionData.getKey() < 3 || !DateUtil.isTimeLegal(interactionData.getTime())){
            //参数有误
            log.error("前端传入参数有误");
            return new ResponseData(Status.PARAMETER_ERROR.getStatus(), null);
        }
        ResponseData responseData = new ResponseData();
        log.info(String.valueOf(interactionData.getKey()));
        List<PowerSum> powerSumList;

        if (interactionData.getKey() == 3) {
            // 按天查询
            powerSumList = listPowerSumByDay(interactionData.getIndex(), interactionData.getTime());
        } else if (interactionData.getKey() == 4) {
            // 按周查询
            powerSumList = listPowerSumByWeek(interactionData.getIndex(), interactionData.getTime());
        } else {
            // 按月查询
            powerSumList = listPowerSumByMonth(interactionData.getIndex(), interactionData.getTime());
        }

        // 返回数据
        responseData.setStatus(Status.NORMAL.getStatus());
        Data data = new Data();
        data.setPowerSumList(powerSumList);
        responseData.setData(data);
        return responseData;
    }

    /**
     * 以小时分隔，得到该天的用电量
     * @param index 串口
     * @param time 日期
     * @return 该天24小时各自的用电量
     */
    private List<PowerSum> listPowerSumByDay(int index, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 24; i++) {
                // 得到起始时间和截至时间的总用电量
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                // 切割小时单位
                PowerSum powerSum = new PowerSum(startTime.split(" ")[1].split(":")[0],
                        deviceMapper.listPowerSum(index, startTime, endTime));
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
                powerSumList.add(powerSum);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return powerSumList;
    }

    /**
     * 以天分隔，得到该周的用电量
     * @param index 串口
     * @param time 日期
     * @return 该周7天各自的用电量
     */
    private List<PowerSum> listPowerSumByWeek(int index, String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 7; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                // 以天为单位
                PowerSum powerSum = new PowerSum(startTime.split(" ")[0],
                        deviceMapper.listPowerSum(index, startTime, endTime));
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
                powerSumList.add(powerSum);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return powerSumList;
    }

    /**
     * 以天分隔，得到该月的用电量
     * @param index 串口
     * @param time 日期
     * @return 该月30天各自的用电量
     */
    private List<PowerSum> listPowerSumByMonth(int index, String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 30; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                // 以天为单位
                PowerSum powerSum = new PowerSum(startTime.split(" ")[0],
                        deviceMapper.listPowerSum(index, startTime, endTime));
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
                powerSumList.add(powerSum);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return powerSumList;
    }


    /**
     * 控制用电器的开关
     *
     * @param interactionData 串口
     * @return 是否操控成功
     */
    @Override
    public ResponseData controller(InteractionData interactionData) {
        // 发送串口和控制开关信息
        String message = "#" + interactionData.getIndex() + "-" + interactionData.getKey() + "$";
        log.info("控制开关" + message);
        new TcpHandler().send(message);

        // 返回结果
        ResponseData responseData = new ResponseData();
        responseData.setStatus(Status.NORMAL.getStatus());
        return responseData;
    }
}
