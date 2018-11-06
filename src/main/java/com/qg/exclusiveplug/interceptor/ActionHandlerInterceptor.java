package com.qg.exclusiveplug.interceptor;

import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class ActionHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("用户操作拦截器");

        String url = httpServletRequest.getRequestURI();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        log.info("用户正在访问-->{}", url);
        if(url.indexOf("/device") > 0) {

        }
        if (url.indexOf("views") > 0) {
            if (url.indexOf("login.html") > 0) {
                return true;
            } else {
                HttpSession session = httpServletRequest.getSession();
//                Integer user = (Integer) session.getAttribute("privilege");

                System.out.println("拦截权限 is :" + user);
                if (null == user) {
                    httpServletResponse.sendRedirect("login.html");
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
