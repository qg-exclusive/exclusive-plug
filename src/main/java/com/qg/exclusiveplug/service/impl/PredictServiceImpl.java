package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.DataMapper;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.SerialPort;
import com.qg.exclusiveplug.enums.Status;
import com.qg.exclusiveplug.service.PredictService;
import com.qg.exclusiveplug.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

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
    private static String PATTERN = "yyyy-MM-dd";
    @Autowired
    private DataMapper dataMapper;
    @Override
    public ResponseData predictNowPowerSumService(String time, int index) {
        if (index > THREE || index < ONE || !DateUtil.isDate(time)){
            //参数有误
            log.error("前端传入参数有误");
            return new ResponseData(Status.PARAMETER_ERROR.getStatus(), null);
        }

        Date date = DateUtil.stringToDate(time, PATTERN);
        String[] beforeDates = DateUtil.getDayBefore(date, 7);

        //开始收集七天的数据
        //todo 这里后期要改为批量模糊查询，暂时先用循环查询
        for (String d : beforeDates) {
        }
        return null;
    }
}
