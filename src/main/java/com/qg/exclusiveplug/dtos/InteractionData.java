package com.qg.exclusiveplug.dtos;

import lombok.Data;

/**
 * @author Chen
 * time 2018-10-03 20:32:48
 * description 接收前端发来的数据
 */
@Data
public class InteractionData {

    /**
     * 串口号
     */
    int index;

    /**
     * 时间
     */
    String time;

    /**
     * 关键词
     */
    int key;
}
