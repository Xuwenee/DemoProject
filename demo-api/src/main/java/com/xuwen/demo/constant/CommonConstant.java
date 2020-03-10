package com.xuwen.demo.constant;

import java.util.concurrent.TimeUnit;

/**
 * 通用常量
 *
 * @author XuWen
 * @created 2018-04-16 10:54.
 */
public interface CommonConstant {

    /**
     * token前缀
     */
    String TOKEN = "token_";

    /**
     * 过期时间
     */
    Long EXPIRE_TIME = 24L;

    /**
     * 过期时间单位
     */
    TimeUnit TIMEUNIT = TimeUnit.HOURS;

    /**
     * md5加盐hash次数
     */
    Integer HASH_ITERATIONS = 2;

    /**
     * md5加盐
     */
    String SALT = "DATA_CENTER_SSO";

    /**
     * 防刷字段
     */
    String SGIN = "DATA_CENTER_SSO";
}
