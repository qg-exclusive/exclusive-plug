package com.qg.exclusiveplug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HuaChen
 * time:2018年11月5日22:46:26
 * describe:用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户当前所处接口
     */
    private String userDeviceIndex;

    /**
     * 用户所拥有的接口
     */
    private String userDeviceIndexs;
}
