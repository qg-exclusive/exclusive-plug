package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;

public interface DeviceService {

    /**
     * 得到某天(24小时)/某月(30天)的用电量
     * @param interactionData key(天/月/周)，时间
     * @return 状态码和用电量集合
     */
    ResponseData listPowerSum(InteractionData interactionData);

    /**
     * 控制用电器的开关
     * @param interactionData 串口
     * @return 是否操控成功
     */
    ResponseData controller(InteractionData interactionData);
}
