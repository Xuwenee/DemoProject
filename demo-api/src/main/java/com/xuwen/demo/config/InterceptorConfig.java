//package com.xuwen.datacentersso.config;
//
//import com.xuwen.datacentersso.interceptor.SsoInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * 拦截器规则配置
// *
// * @author XuWen
// * @created 2018-04-17 11:07.
// */
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurerAdapter {
//
//    /**
//     * 注入拦截器中bean组件
//     * @return
//     */
//    @Bean
//    public SsoInterceptor SsoInterceptor() {
//        return new SsoInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(SsoInterceptor()).addPathPatterns("/test");
//        super.addInterceptors(registry);
//    }
//}
