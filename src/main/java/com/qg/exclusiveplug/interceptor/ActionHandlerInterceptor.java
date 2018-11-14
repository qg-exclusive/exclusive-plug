package com.qg.exclusiveplug.interceptor;

import com.google.gson.Gson;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.StatusEnum;
import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class ActionHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        BodyReaderHttpServletRequestWrapper myRequestWrapper = null;
        // 封装请求参数
        myRequestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        int index = new Gson().fromJson(myRequestWrapper.getReader(), InteractionData.class).getIndex();
        User user = (User) myRequestWrapper.getSession().getAttribute("user");


        ResponseData responseData = new ResponseData();
        log.info("用户操作拦截器");
        String url = myRequestWrapper.getRequestURI();
        log.info("用户正在访问-->{}", url);

//        User user = (User) httpServletRequest.getSession().getAttribute("user");
//        int index = new Gson().fromJson(httpServletRequest.getReader(), InteractionData.class).getIndex();

        if (null != user) {
            Map<Integer, Integer> indexPrivilegeMap = user.getIndexPrivilegeMap();
            if (indexPrivilegeMap.containsKey(index) && indexPrivilegeMap.get(index) == 1) {
                return true;
            } else {
                if (url.indexOf("/adddevice") > 0) {
                    return true;
                } else {
                    log.info("操作用电器拦截器 -->> 用户无权限");
                    responseData.setStatus(StatusEnum.USER_HASNOPRIVILEGE.getStatus());
                }
            }
        } else {
            log.info("操作用电器拦截器 -->> 用户未登陆");
            responseData.setStatus(StatusEnum.USER_ISNOLOGIN.getStatus());
        }

        httpServletResponse.getWriter().write(new Gson().toJson(responseData));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
