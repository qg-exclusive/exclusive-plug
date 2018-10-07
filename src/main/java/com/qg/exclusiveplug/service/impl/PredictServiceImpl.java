package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.DeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.DMUrl;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.model.PowerSum;
import com.qg.exclusiveplug.service.PredictService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author WilderGao
 * time 2018-10-04 00:47
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class PredictServiceImpl implements PredictService {
    private static int THREE = 3;
    private static int ONE = 1;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public ResponseData predictNowPowerSumService(String time, int index) {
        if (index > THREE || index < ONE || !DateUtil.isDate(time)) {
            //参数有误
            log.error("前端传入参数有误");
            return new ResponseData(Status.PARAMETER_ERROR.getStatus(), null);
        }

        ResponseData responseData = new ResponseData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Double[] doubles = new Double[7];

        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 7; i++) {
                // 查询每个时间段的用电量
                String endTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String startTime = sdf.format(calendar.getTime().getTime());
                doubles[6 - i] = deviceMapper.listPowerSum(index, startTime, endTime);
                // 判空，防止无数据的情况发生
                if(null == doubles[6 - i]){
                    doubles[6 - i] = 0.0;
                }
                log.info("开始时间：" + startTime + "结束时间：" + endTime + "结果：" + doubles[6 - i]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 准备预测数据
        RequestData requestData = new RequestData<>();
        requestData.setPowerSums(doubles);

        InteractBigData interactBigData;
        try {
            interactBigData = HttpClientUtil.demandedCount(DMUrl.PREDICTED_POWERSUM.getDMUrl(), requestData);
        } catch (IOException e) {
            log.info("连接DM失败");
            responseData.setStatus(Status.PREDICTED_FAILED.getStatus());
            return responseData;
        }

        // 预测成功
        if(null != interactBigData){
            responseData.setStatus(Status.NORMAL.getStatus());
            Data data = new Data();
            PowerSum powerSum = new PowerSum(time, interactBigData.getPowerSum());
            data.setPowerSum(powerSum);
            responseData.setData(data);
            return responseData;
        }

        // 预测失败
        log.info("DM返回数据为空");
        responseData.setStatus(Status.PREDICTED_FAILED.getStatus());
        return responseData;
    }
}
