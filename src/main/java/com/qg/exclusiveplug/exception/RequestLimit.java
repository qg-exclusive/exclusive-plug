package com.qg.exclusiveplug.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {
    /**
     *
     * 允许访问的次数，默认值MAX_VALUE
     */
    int count() default 10;


    /**
     *
     * 时间段，单位为毫秒，默认值一分钟
     */
    long time() default 60000;
}