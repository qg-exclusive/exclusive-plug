package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-23 16:57
 * motto : everything is no in vain
 * description 与电器有关的数据库操作
 */
@Repository
@Mapper
public interface DeviceMapper {

    /**
     * 保存设备到数据库
     * @param devices   设备集合
     * @return  插入结果
     */
    int saveDevices(List<Device> devices);

    /**
     * 取得一系列时间块的用电量
     * @param date 时间点
     * @return 一系列时间块的用电量
     */
    List<Device> listPowerSum(@Param("index") int index, @Param("data") List<String> date);
}
