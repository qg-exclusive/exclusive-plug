package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.DeviceInfo;
import com.qg.exclusiveplug.model.DeviceLog;
import com.qg.exclusiveplug.model.DeviceUuid;
import com.qg.exclusiveplug.model.UserDeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActionDeviceMapper {

    /**
     * 添加与用户关联的设备
     * @param userDeviceInfo 用户ID，端口号，用户权限
     * @return 操作结果
     */
    int addUserDeviceInfo(UserDeviceInfo userDeviceInfo);

    /**
     * 更新与用户关联的设备，主要是权限
     * @param userDeviceInfo 用户ID，端口号，用户权限
     * @return 操作结果
     */
    int updateUserDeviceInfo(UserDeviceInfo userDeviceInfo);

    /**
     * 查询与用户关联的设备，权限
     * @param deviceIndex 设备端口号
     * @param userId 用户ID
     * @return 用户权限
     */
    UserDeviceInfo queryUserDeviceInfo(@Param("userId") int userId, @Param("deviceIndex") int deviceIndex);

    /**
     * 根据用户ID得到所有与该用户相关联的设备和权限
     * @param userId 用户ID
     * @return 设备端口以及权限
     */
    List<UserDeviceInfo> listUserDeviceIndex(@Param("userId") int userId);

    /**
     * 删除与用户关联的设备
     * @param deviceIndex 设备端口
     * @param userId 用户ID
     * @return 操作结果
     */
    int delUserDeviceInfo(@Param("userId") int userId, @Param("deviceIndex") int deviceIndex);

    /**
     * 根据UUID查询对应的设备端口以及权限
     * @param uuid 唯一识别码
     * @return 对应的设备端口以及权限
     */
    DeviceUuid queryDeviceUuidByUuid(String uuid);

    /**
     * 增加设备信息
     * @param deviceInfo 设备信息
     * @return 处理结果
     */
    int addDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 更新设备信息
     * @param deviceInfo 设备信息
     * @return 操作结果
     */
    int updateDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 查询设备信息
     * @param deviceIndex 设备端口
     * @return 操作结果
     */
    DeviceInfo queryDeviceInfo(@Param("deviceIndex") int deviceIndex);

    /**
     * 删除设备信息
     * @param deviceIndex 设备端口
     * @return 操作结果
     */
    int delDeviceInfo(@Param("deviceIndex") int deviceIndex);

    /**
     * 查看该端口被系统操作的日志信息
     * @param deviceIndex 设备端口
     * @return 日志信息
     */
    List<DeviceLog> queryDeviceLog(@Param("deviceIndex") int deviceIndex);
}
