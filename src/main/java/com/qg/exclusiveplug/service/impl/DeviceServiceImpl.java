package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.DeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.handlers.TcpHandler;
import com.qg.exclusiveplug.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        ResponseData responseData = new ResponseData();
        log.info(String.valueOf(interactionData.getKey()));
        Double[] doubles;

        if (interactionData.getKey() == 3) {
            // 按天查询
            doubles = listPowerSumByDay(interactionData.getIndex(), interactionData.getTime());
        } else if (interactionData.getKey() == 4) {
            // 按周查询
            doubles = listPowerSumByWeek(interactionData.getIndex(), interactionData.getTime());
        } else if (interactionData.getKey() == 5){
            // 按月查询
            doubles = listPowerSumByMonth(interactionData.getIndex(), interactionData.getTime());
        } else {
            responseData.setStatus(Status.PARAMETER_ERROR.getStatus());
            return  responseData;
        }

        // 返回数据
        responseData.setStatus(Status.NORMAL.getStatus());
        Data data = new Data();
        data.setPowerSums(doubles);
        responseData.setData(data);
        return new ResponseData();
    }

    /**
     * 以小时分隔，得到该天的用电量
     * @param index 串口
     * @param time 日期
     * @return 该天24小时各自的用电量
     */
    private Double[] listPowerSumByDay(int index, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:dd:ss");
        Double[] doubles = new Double[24];
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 24; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                doubles[i] = deviceMapper.listPowerSum(index, startTime, endTime);
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
            }
            for(Double d : doubles){
                log.info(String.valueOf(d));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doubles;
    }

    /**
     * 以天分隔，得到该周的用电量
     * @param index 串口
     * @param time 日期
     * @return 该周7天各自的用电量
     */
    private Double[] listPowerSumByWeek(int index, String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:dd:ss");
        Double[] doubles = new Double[7];
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 7; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                doubles[i] = deviceMapper.listPowerSum(index, startTime, endTime);
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
            }
            for(Double d : doubles){
                log.info(String.valueOf(d));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doubles;
    }

    /**
     * 以天分隔，得到该月的用电量
     * @param index 串口
     * @param time 日期
     * @return 该月30天各自的用电量
     */
    private Double[] listPowerSumByMonth(int index, String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:dd:ss");
        Double[] doubles = new Double[30];
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 30; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                doubles[i] = deviceMapper.listPowerSum(index, startTime, endTime);
                log.info("开始时间：" + startTime + "结束时间：" + endTime);
            }
            for(Double d : doubles){
                log.info(String.valueOf(d));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doubles;
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
        String message = interactionData.getIndex() + "-" + interactionData.getKey();
        new TcpHandler().send(message);

        // 返回结果
        ResponseData responseData = new ResponseData();
        responseData.setStatus(Status.NORMAL.getStatus());
        return responseData;
    }
}
