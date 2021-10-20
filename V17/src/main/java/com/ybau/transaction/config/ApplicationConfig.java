package com.ybau.transaction.config;


import com.ybau.transaction.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class ApplicationConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(jwtInterceptor);
        interceptorRegistration.addPathPatterns("/**");        //拦截所有请求
        interceptorRegistration.excludePathPatterns(
                "/**/login/**", "/order/findOrderDate", "/express/saveExpress","/file/downloadFile","/code/getCode"
                ,"/**/customizedOrder/saveCo","/customizedOrder/findClient"

        );
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }
}
