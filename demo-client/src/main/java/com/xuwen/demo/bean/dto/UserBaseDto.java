package com.xuwen.demo.bean.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户基本信息
 *
 * @author XuWen
 * @Date 2018/4/14 18:05
 */
@Data
public class UserBaseDto {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 1正常用户 2黑名单用户 
     */
    private Integer userRole;

    /**
     * 注册来源：1手机号
     */
    private Integer registerSource;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别 0-女性 1-男性
     */
    private Boolean gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 头像图片地址
     */
    private String face;

    /**
     * 逻辑删除 0未删除；1已删除
     */
    private Integer del;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}