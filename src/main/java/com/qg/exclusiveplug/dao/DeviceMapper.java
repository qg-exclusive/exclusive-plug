package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.PredicatedData;
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
     * 取得某串口某时间段内的总用电量
     * @param index 串口号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 某串口某时间段内的总用电量
     */
    Double listPowerSum(@Param("index") int index, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
