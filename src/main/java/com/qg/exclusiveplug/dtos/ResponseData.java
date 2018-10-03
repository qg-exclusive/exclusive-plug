package com.qg.exclusiveplug.dtos;

import lombok.AllArgsConstructor;

/**
 * @author Chen
 * time 2018-10-03 15:42:20
 * description 响应前端的类
 */
@lombok.Data
@AllArgsConstructor
public class ResponseData {
    /**
     * 响应状态码
     */
    String status;

    Data data;
}
