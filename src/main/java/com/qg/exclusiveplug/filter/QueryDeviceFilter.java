package com.qg.exclusiveplug.filter;

import com.google.gson.Gson;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.enums.StatusEnum;
import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@WebFilter(filterName = "actiondevice", urlPatterns = {"/querydevice/*"})
public class QueryDeviceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("用户查看拦截器");
        ResponseData responseData = new ResponseData();

        BodyReaderHttpServletRequestWrapper myRequestWrapper = null;

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 封装请求参数
        myRequestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        int index = new Gson().fromJson(myRequestWrapper.getReader(), InteractionData.class).getIndex();
        User user = (User) myRequestWrapper.getSession().getAttribute("user");

        String url = myRequestWrapper.getRequestURI();
        log.info("用户正在访问-->{}", url);

        if(null != user) {
            Map<Integer, Integer> indexPrivilegeMap = user.getIndexPrivilegeMap();
            if(indexPrivilegeMap.containsKey(index)) {
                filterChain.doFilter(myRequestWrapper, servletResponse);
                return;
            } else {
                log.info("用户权限不足");
                responseData.setStatus(StatusEnum.USER_HASNOPRIVILEGE.getStatus());
            }
        } else {
            log.info("用户未登陆");
            responseData.setStatus(StatusEnum.USER_ISNOLOGIN.getStatus());
        }
        httpServletResponse.getWriter().write(new Gson().toJson(responseData));
    }

    @Override
    public void destroy() {

    }
}
