/*
package com.qg.exclusiveplug.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebListener
@Slf4j
public class RequestListener implements ServletRequestListener {
    private   MySessionContext myc=MySessionContext.getInstance();

//    @Autowired
//    private RequestListener requestListener;

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        log.info("请求初始化");
        // 将所有request请求都携带上httpSession
        HttpSession httpSession = ((HttpServletRequest) servletRequestEvent.getServletRequest()).getSession();
        myc.AddSession(httpSession);
    }

    */
/*@Bean
    public ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(requestListener);
        return servletListenerRegistrationBean;
    }*//*

}
*/
