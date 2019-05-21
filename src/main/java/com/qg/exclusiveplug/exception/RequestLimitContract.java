/*
package com.qg.exclusiveplug.exception;

import com.qg.exclusiveplug.model.User;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.Element;
import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RequestLimitContract {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Before("@within(limit) || @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws RequestLimitException {
        log.info("防止IP恶意访问AOP-->>拦截");
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                    break;
                }
            }
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURI();
            String key = "reqlimit".concat(url).concat(ip);
            log.info("防止IP恶意访问AOP-->>key = {}", key);
            if (redisTemplate.opsForValue().get(key) == null) {
                redisTemplate.opsForValue().set(key, 1);
            } else {
                redisTemplate.opsForValue().set(key, redisTemplate.opsForValue().get(key) + 1);
            }
            System.out.println(joinPoint);

            long limitTime = Class.forName(joinPoint.getTarget().getClass().getName()).getAnnotation(RequestLimit.class).time();
            */
/*MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();

            long time = 0;
            if(method != null) {
                time = .getAnnotation(RequestLimit.class).time();
            }*//*


            log.info("防止IP恶意访问AOP-->>规定时间 = {}", limitTime);
            redisTemplate.expire(key, limitTime, TimeUnit.MILLISECONDS);
            int count = redisTemplate.opsForValue().get(key);
            */
/*if (count > 0) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() { //创建一个新的计时器任务。
                    @Override
                    public void run() {
                        redisTemplate.remove(key);
                    }
                };
                timer.schedule(task, limit.time());  //安排在指定延迟后执行指定的任务。task : 所要安排的任务。: 执行任务前的延迟时间，单位是毫秒。
            }*//*

            if (count > limitTime) {
                //logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
                throw new RequestLimitException();
            }
        } catch (RequestLimitException e) {
            throw e;
        } catch (Exception e) {
            log.error("发生异常: ", e);
        }
    }

}*/
