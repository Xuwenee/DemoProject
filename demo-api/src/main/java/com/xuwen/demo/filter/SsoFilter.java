package com.xuwen.demo.filter;

import com.xuwen.demo.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 *
 * @author XuWen
 * @created 2018-04-17 20:10.
 */
@WebFilter(filterName = "SsoFilter", urlPatterns = {"/user/operator/*"})
public class SsoFilter implements Filter {


    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = null;
        //取Cookie判断是否有登陆token
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if("token".equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }
        if(StringUtils.isEmpty(token)){
            token = httpServletRequest.getHeader("token");
        }
        //获取用户信息
        String uid = null;
        if(!StringUtils.isEmpty(token)){
            Object uidObj = redisUtil.get(token);
            if(uidObj != null && uidObj instanceof String){
                uid = (String) uidObj;
            }
        }
        //验证没有登陆则跳登录页
        if(StringUtils.isEmpty(uid)){
            //重定向到指定外部登陆页面（请求转发只能跳转到web应用内的网页，不可行）
            ((HttpServletResponse) servletResponse).sendRedirect("https://www.baidu.com/");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}