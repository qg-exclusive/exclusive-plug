package com.qg.exclusiveplug.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 配置静态访问资源
     * @param registry
     */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/index").setViewName("index.html");
        registry.addViewController("/home").setViewName("home.html");
        registry.addViewController("/hello").setViewName("hello.html");
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/user/**");
//        registry.addInterceptor(new QueryHandlerInterceptor()).addPathPatterns("/querydevice/**");
//        registry.addInterceptor(new ActionHandlerInterceptor()).addPathPatterns("/actiondevice/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
}