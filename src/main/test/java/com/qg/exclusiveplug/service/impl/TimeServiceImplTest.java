package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TimeServiceImplTest {
    @Autowired
    private QueryDeviceMapper queryDeviceMapper;

    @Test
    public void test() {
        List<Integer> integerList = Arrays.asList(new Integer[] {1,2,3});
        System.out.println(integerList);
        for (int deviceIndex : integerList) {
            queryDeviceMapper.listAllDevice(deviceIndex, "device");

        }
    }
}