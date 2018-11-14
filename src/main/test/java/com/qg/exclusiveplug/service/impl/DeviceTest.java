package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.CreateTableMapper;
import com.qg.exclusiveplug.dao.Provider;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author linxu
 * @date 2018/11/13
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DeviceTest {
    @Autowired
    private CreateTableMapper createTableMapper;
    @Autowired
    private QueryDeviceMapper queryDeviceMapper;
    @Test
    public void test(){
        String date="2018-11-13 20:22:00";
        System.out.println(date.substring(0,10));
        this.createTableMapper.updateTableField(Provider.createTableSql("device20181113"));
    }
}
