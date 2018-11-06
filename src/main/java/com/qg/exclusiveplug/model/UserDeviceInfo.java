package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDeviceInfo {

    /**
     * 用户ID
     */
    private int userId;

    /**
     * 设备端口号
     */
    private int deviceIndex;

    /**
     * 用户权限
     */
    private int userPrivilege;
}
