package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.dao.ActionDeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.handlers.TcpHandler;
import com.qg.exclusiveplug.map.LongWaitList;
import com.qg.exclusiveplug.map.TimeMap;
import com.qg.exclusiveplug.model.DeviceInfo;
import com.qg.exclusiveplug.model.DeviceUuid;
import com.qg.exclusiveplug.model.User;
import com.qg.exclusiveplug.model.UserDeviceInfo;
import com.qg.exclusiveplug.service.ActionDeviceService;
import com.qg.exclusiveplug.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class ActionDeviceServiceImpl implements ActionDeviceService {

    @Autowired
    private ActionDeviceMapper actionDeviceMapper;

    /**
     * 控制用电器的开关
     *
     * @param interactionData 串口
     * @return 是否操控成功
     */
    @Override
    public ResponseData controller(InteractionData interactionData) {
        int index = interactionData.getIndex();
        int key = interactionData.getKey();
        // 发送串口和控制开关信息
        String message = "#" + index + "-" + key + "$";
        log.info("控制开关" + message);
        new TcpHandler().send(index, message);

        // 将设备移出待机队列
        if (LongWaitList.contains(index)) {
            LongWaitList.remove(index);
            try {
                TimeMap.replace(index, DateUtil.getCurrentDate());
            } catch (ParseException e) {
                log.info("时间解析失败!!");
            }
        }

        // 返回结果
        ResponseData responseData = new ResponseData();
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        return responseData;
    }

    /**
     * 增加用户设备
     *
     * @param interactionData 设备UUID
     * @return 操作结果 若成功则返回用户端口权限Map集合
     */
    @Override
    public ResponseData addDevice(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        User user = (User) httpSession.getAttribute("user");
        log.info("增加用户设备-->>用户信息：{}", user);
        Integer userId = user.getUserId();
        String uuid = interactionData.getUuid();
        log.info("增加设备-->>用户ID:{},uuid:{}", userId, uuid);

        if (null != uuid) {
            // 取出出厂设置中该UUID对应的端口与权限
            DeviceUuid deviceUuid = actionDeviceMapper.queryDeviceUuidByUuid(uuid);
            int deviceIndex = deviceUuid.getDeviceIndex();
            if (0 != deviceIndex) {
                int devicePrivilege = deviceUuid.getDevicePrivilege();
                log.info("增加设备-->>{}对应的端口:{}，权限:{}", uuid, deviceIndex, devicePrivilege);

                // 得到用户关联设备信息
                UserDeviceInfo userDeviceInfo = actionDeviceMapper.queryUserDeviceInfo(userId, deviceIndex);

                Map<Integer, Integer> integerIntegerMap = user.getIndexPrivilegeMap();
                // 用户是否已跟该用电器关联
                if (null == userDeviceInfo) {
                    actionDeviceMapper.addUserDeviceInfo(new UserDeviceInfo(userId, deviceIndex, devicePrivilege));
                    integerIntegerMap.put(deviceIndex, devicePrivilege);
                } else {
                    // 更改权限
                    log.info("增加设备-->> 更改权限");
                    userDeviceInfo.setUserPrivilege(devicePrivilege);
                    actionDeviceMapper.updateUserDeviceInfo(new UserDeviceInfo(userId, deviceIndex, devicePrivilege));
                    integerIntegerMap.replace(deviceIndex, devicePrivilege);
                }
                // 替换权限
                user.setIndexPrivilegeMap(integerIntegerMap);
                httpSession.setAttribute("user", user);

                Data data = new Data();
                data.setUser(User.builder().indexPrivilegeMap(integerIntegerMap).build());
                responseData.setData(data);
                responseData.setStatus(StatusEnum.NORMAL.getStatus());

            } else {
                responseData.setStatus(StatusEnum.DEVICE_ISNOEXIST.getStatus());
                log.info("增加设备-->>设备不存在");
            }
        } else {
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
            log.info("增加设备-->>前端参数错误");
        }
        log.info("增加设备-->>响应成功");
        return responseData;

    }

    /**
     * 增加用户关联设备信息
     *
     * @param interactionData 用户关联设备信息
     * @param httpSession     用户信息
     * @return 操作结果
     */
    @Override
    public ResponseData addDeviceInfo(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        DeviceInfo deviceInfo = interactionData.getDeviceInfo();
        if (null != deviceInfo) {
            int deviceIndex= deviceInfo.getDeviceIndex();
            double deviceWorkPower = deviceInfo.getDeviceWorkPower();
            double deviceStandbyPower = deviceInfo.getDeviceStandbyPower();
            log.info("增加用户关联设备信息-->>工作功率：{}，待机功率；{}，是否自动开关：{}",
                    deviceWorkPower, deviceStandbyPower, deviceInfo.getAutoClose());

            if (deviceWorkPower != 0 && deviceStandbyPower != 0) {

                DeviceInfo deviceInfo1 = actionDeviceMapper.queryDeviceInfo(deviceIndex);
                if (null != deviceInfo1) {
                    actionDeviceMapper.updateDeviceInfo(deviceInfo);
                } else {
                    actionDeviceMapper.addDeviceInfo(deviceInfo);
                }

                Data data = new Data();
                data.setDeviceInfo(deviceInfo);
                responseData.setStatus(StatusEnum.NORMAL.getStatus());
                responseData.setData(data);
                log.info("增加用户关联设备信息-->>请求结束");

            } else {
                responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
                log.info("增加用户关联设备信息-->>前端参数错误");
            }
        } else {
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
            log.info("增加用户关联设备信息-->>前端参数错误");
        }
        return responseData;
    }

    /**
     * 查看用户关联设备信息
     *
     * @param interactionData 设备端口
     * @return 操作结果 若成功则返回端口信息
     */
    @Override
    public ResponseData queryDeviceInfo(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        int deviceIndex = interactionData.getIndex();

        DeviceInfo deviceInfo = actionDeviceMapper.queryDeviceInfo(deviceIndex);

        Data data = new Data();
        data.setDeviceInfo(deviceInfo);
        responseData.setData(data);
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        log.info("查看用户关联设备信息-->>请求结束");
        return responseData;
    }

    /**
     * 修改设备信息
     *
     * @param interactionData 设备信息
     * @param httpSession     用户信息
     * @return 操作结果
     */
    @Override
    public ResponseData updateDeviceInfo(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();

        DeviceInfo deviceInfo = interactionData.getDeviceInfo();
        if (null != deviceInfo) {
            if (actionDeviceMapper.updateDeviceInfo(deviceInfo) > 0) {
                Data data = new Data();
                data.setDeviceInfo(actionDeviceMapper.queryDeviceInfo(deviceInfo.getDeviceIndex()));
                responseData.setData(data);
                responseData.setStatus(StatusEnum.NORMAL.getStatus());

                log.info("修改设备信息-->>请求结束");
            } else {
                responseData.setStatus(StatusEnum.RUN_ERROR.getStatus());
                log.info("修改设备信息-->>发生未知错误");
            }
        } else {
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
            log.info("修改设备信息-->>前端参数错误");
        }
        return responseData;
    }

    /**
     * 修改端口名字
     *
     * @param interactionData 端口号，设备新名字
     * @param httpSession     用户信息
     * @return 操作结果
     */
    @Override
    public ResponseData updateDeviceName(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        int deviceIndex = interactionData.getIndex();
        String machineName = interactionData.getMachineName();
        if (null != machineName && !machineName.equals("")) {
            String message = "*" + deviceIndex + "-" + machineName + "$";
            log.info("修改端口名称" + message);
            new TcpHandler().send(deviceIndex, message);
        }
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        return responseData;
    }

    /**
     * 删除设备
     *
     * @param interactionData 设备串口号
     * @param httpSession     用户信息
     * @return 操作结果 若成功则返回设备操作信息
     */
    @Override
    public ResponseData delDevice(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        User user = (User) httpSession.getAttribute("user");
        int deviceIndex = interactionData.getIndex();
        int userId = user.getUserId();

        log.info("删除设备-->>用户ID：{}，设备端口：{}", userId, deviceIndex);

        // 将指定端口移除
        Map<Integer, Integer> indexPrivilegeMap = user.getIndexPrivilegeMap();
        actionDeviceMapper.delUserDeviceInfo(userId, deviceIndex);
        indexPrivilegeMap.remove(deviceIndex);

        user.setIndexPrivilegeMap(indexPrivilegeMap);
        httpSession.setAttribute("user", user);

        // 数据打包
        Data data = new Data();
        data.setUser(User.builder().indexPrivilegeMap(indexPrivilegeMap).build());
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        responseData.setData(data);
        return responseData;
    }

    /**
     * 获取设备被系统操作日志
     *
     * @param interactionData 端口号
     * @return 日志信息
     */
    @Override
    public ResponseData queryDeviceLog(InteractionData interactionData) {
        log.info("获取设备被系统操作日志-->>端口号：{}", interactionData.getIndex());

        ResponseData responseData = new ResponseData();
        Data data = new Data();
        data.setDeviceLogList(actionDeviceMapper.queryDeviceLog(interactionData.getIndex()));

        responseData.setData(data);
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        return responseData;
    }

    /**
     * 定时任务
     *
     * @param interactionData 串口号，启动或关闭，时间
     * @param httpSession     用户信息
     * @return 操作结果
     */
    @Async
    @Override
    public ResponseData timing(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        final String PATTERN = "yyyy-MM-dd HH:mm:ss";

        int key = interactionData.getKey();
        int index = interactionData.getIndex();
        String time = interactionData.getTime();

        log.info("定时任务-->> 状态切换：{}， 端口：{}， 时间：{}",key, index, time);
        if(DateUtil.isTimeLegal(time)) {
            Calendar timingCal = Calendar.getInstance();

            // get timingTask's Cal
            try {
                Date timingDate = Objects.requireNonNull(DateUtil.stringToDate(time, PATTERN));
                timingCal.setTime(timingDate);
            } catch (NullPointerException e) {
                responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
            }

            // get currentTime's Cal
            Calendar currentCal = Calendar.getInstance();
            try {
                currentCal.setTime(DateUtil.getCurrentDate());
            } catch (ParseException e) {
                responseData.setStatus(StatusEnum.RUN_ERROR.getStatus());

            }

            // judge timingCal is after than currentCal or not
            if (timingCal.after(currentCal)) {
                long diffSecond = DateUtil.diffSecond(currentCal.getTime(), timingCal.getTime());

                log.info("定时任务-->> 距离{}秒执行", diffSecond);
                try {
                    Thread.sleep(diffSecond * 1000);
                } catch (InterruptedException e) {
                    responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
                    e.printStackTrace();
                    return responseData;
                }

                String message = "#" + index + "-" + key + "$";
                //TODO bug
                log.info("定时任务" + message);
                new TcpHandler().send(index, message);

                responseData.setStatus(StatusEnum.NORMAL.getStatus());
            } else {
                responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
            }
        }else {
            log.info("定时任务-->> 参数错误");
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
        }
        return responseData;
    }
}

