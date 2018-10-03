package com.qg.exclusiveplug.dtos;

import lombok.Data;

/**
 * @author Chen
 * time 2018-10-03 15:33:01
 * description 接收数据挖掘回应的类
 */
@Data
public class InteractBigData {
    /**
     * 用电器的状态
     * 0断电/1待机/2工作
     */
    int status;

    double[] powerSum;
}