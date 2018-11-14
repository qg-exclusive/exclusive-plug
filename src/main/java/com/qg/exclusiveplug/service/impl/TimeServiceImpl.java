package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.cache.CacheMap;
import com.qg.exclusiveplug.dao.CreateTableMapper;
import com.qg.exclusiveplug.dao.Provider;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-23 18:03
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class TimeServiceImpl implements TimeService {
    private static final String CACHE_KEY = "devices";

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private QueryDeviceMapper queryDeviceMapper;
    @Autowired
    private CreateTableMapper createTableMapper;

    @Override
    @Async
    @Scheduled(fixedRate = 20000)
    public void saveDataToMySql() {

        if (CacheMap.containKey(CACHE_KEY)) {
            List<Device> devices = CacheMap.get(CACHE_KEY);
            log.info("将map中的数据保存到mysql并删除");
            //首先获取数据，并且尝试插入；
            Device device = devices.get(0);
            //2018-11-10 22:30:29
            String date = device.getDate().substring(0,10);
            date=date.replaceAll("-","");
            String tableName="device"+date;
            try {
                queryDeviceMapper.saveDevices(devices,tableName );
            } catch (Exception e) {
                //if table is not exists.will catch a ex.and create a table and save data;
                try {
                    createTableMapper.updateTableField(Provider.createTableSql(tableName));
                    queryDeviceMapper.saveDevices(devices,tableName );
                } catch (Exception e1) {
                    log.error("自动建表并且存储数据失败");
                }
            }
            CacheMap.remove(CACHE_KEY);
        }
    }
}
