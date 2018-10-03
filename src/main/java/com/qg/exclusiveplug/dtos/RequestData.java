package com.qg.exclusiveplug.dtos;

import com.qg.exclusiveplug.model.Device;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-02 17:26:30
 * description 向数据挖掘发送请求的类
 */
@Data
public class RequestData<T> {

    /**
     * 各类信息
     */
    T data;

    /**
     * 列表
     */
    List<T> list;

}
