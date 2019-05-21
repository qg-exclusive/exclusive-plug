/*
package com.qg.exclusiveplug.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@WebFilter("/*")
@Slf4j
public class IPFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String ip=getIpAddr(req);

        log.info("IP-->{}<--访问了本网站", ip);
        System.out.println(ip);
        if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            chain.doFilter(request, response);
        }else{
            log.warn("ip:"+ip+"被拦截了");
        }
    }

    @Override
    public void destroy() {
    }


    */
/**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     *
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *//*

    public  String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP"); // 用虚拟机发会出现一个莫名其妙的IP地址
        ip = request.getHeader("Proxy-Client-IP");
        log.info("Proxy-Client-IP得到的IP" + ip);

        ip = request.getHeader("WL-Proxy-Client-IP");
        log.info("WL-Proxy-Client-IP得到的IP" + ip);


//        log.info("X-Real-IP得到的IP" + ip);
        if (ip!= null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        log.info("X-Forwarded-For得到的IP" + ip);
        if (ip!= null && !"".equals(ip)  && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            System.out.println(ip);
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
}*/
