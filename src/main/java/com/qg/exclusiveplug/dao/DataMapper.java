package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.PredicatedData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-01 21:49:23
 * description 与预测数据有关的数据库操作
 */
@Repository
@Mapper
public interface DataMapper {
    /**
     * 取出某电器在某一天开始与结束的累计用电量
     * @param machineName 用电器名称
     * @param date yyyy-MM-dd
     * @return 该天第一条数据与最后一条数据
     */
    List<Device> listDevicesCPByDate(@Param("machineName") String machineName, @Param("date") String date);

    /**
     * 保存某电器在某一天的总用电量
     * @param predicatedData 预测数据信息
     * @return 插入结果
     */
    int saveDayPower(PredicatedData predicatedData);

}
