package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryDeviceServiceImplTest {
    @Autowired
    private QueryDeviceMapper queryDeviceMapper;

    @Transactional
    @org.junit.Test
    public void test() {
        String time = "2018-10-01 00:00:00";
        int index = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            double[] doubles = new double[48];
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for(int i = 0; i < 39; i++) {
                System.out.print(sdf.format(calendar.getTime()) + " ");
                for (int j = 0; j < 48; j++) {
                    // 查询每个时间段的用电量
                    String tableName="device"+time.replaceAll("-","");
                    String endTime = sdf.format(calendar.getTime());
                    calendar.add(Calendar.MINUTE, 30);
                    String startTime = sdf.format(calendar.getTime());
                    System.out.print(queryDeviceMapper.listPowerSum(index,endTime, startTime,tableName) + " ");
                }
                System.out.println(" ");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}