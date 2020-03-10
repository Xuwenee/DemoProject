//package com.autohome.datacentersso.config;
//
//import com.autohome.datacentersso.filter.SsoFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 过滤器
// *
// * @author XuWen
// * @created 2018-04-17 20:09.
// */
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean registerFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new SsoFilter());
//        registration.addUrlPatterns("/test");
//        //设置名称
//        registration.setName("SsoFilter");
//        //执行顺序
//        registration.setOrder(1);
//        return registration;
//    }
//
//}
