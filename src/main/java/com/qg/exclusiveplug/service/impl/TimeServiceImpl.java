package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.dao.CreateTableMapper;
import com.qg.exclusiveplug.dao.Provider;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.service.TimeService;
import com.qg.exclusiveplug.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.SessionCallback;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private RedisTemplate<String, Device> redisTemplate;

    @Autowired
    private QueryDeviceMapper queryDeviceMapper;
    @Autowired
    private CreateTableMapper createTableMapper;

    @Override
    @Async
    @Scheduled(fixedRate = 20000)
    public void saveDataToMySql() {
        if (null != redisTemplate.hasKey(CACHE_KEY) && redisTemplate.hasKey(CACHE_KEY)) {
            // 开启事务
            redisTemplate.multi();
            redisTemplate.opsForList().range(CACHE_KEY, 0, -1);
            redisTemplate.opsForList().trim(CACHE_KEY, 1, 0);
            List<Object> objects = redisTemplate.exec();
            // 事物没有回滚
            if (null != objects && objects.size() != 0) {
                @SuppressWarnings("unchecked")
                List<Device> devices = (List<Device>) objects.get(0);

                log.info("定时存储-->>" + devices);
                assert devices != null;
                Date date;
                try {
                    date = DateUtil.getCurrentDate();
                } catch (ParseException e) {
                    log.info("获取当前时间失败");
                    e.printStackTrace();
                    return;
                }
                String tableName = "device" + new SimpleDateFormat("yyyyMMdd").format(date);
                System.out.println(tableName);
                try {
                    queryDeviceMapper.saveDevices(devices, tableName);
                } catch (Exception e) {
                    //if table is not exists.will catch a ex.and create a table and save data;
                    try {
                        createTableMapper.updateTableField(Provider.createTableSql(tableName));
                        queryDeviceMapper.saveDevices(devices, tableName);
                    } catch (Exception e1) {
                        log.error("自动建表并且存储数据失败");
                    }
                }
                System.out.println(devices.size());
            }
        }
    }

        /*if (CacheMap.containKey(CACHE_KEY)) {
            List<Device> devices = CacheMap.get(CACHE_KEY);
            log.info("将map中的数据保存到mysql并删除");
            //首先获取数据，并且尝试插入；
            assert devices != null;
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
        }*/
}
