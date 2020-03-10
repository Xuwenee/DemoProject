//package com.autohome.datacentersso.interceptor;
//
//import com.autohome.datacentersso.constant.CommonConstant;
//import com.autohome.datacentersso.util.RedisUtil;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 拦截器
// *
// * @author XuWen
// * @created 2018-04-17 11:08.
// */
//@Component
//public class SsoInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    /**
//     * 防刷拦截校验
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @param o
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        String token = httpServletRequest.getHeader("token");
//        if(!StringUtils.isEmpty(token)){
//            redisUtil.get(token);
//        }
//        String sign = httpServletRequest.getHeader("sign");
//        if(!CommonConstant.SGIN.equals(sign)){
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//    }
//}