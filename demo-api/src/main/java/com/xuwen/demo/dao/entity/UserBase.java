package com.xuwen.demo.dao.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * @Description
 *
 * @author XuWen
 * @Date 2018/4/14 18:05
 */
@Data
@Table(name = "user_base")
public class UserBase {
    /**
     * 用户ID
     */
    @Id
    private Long uid;

    /**
     * 1正常用户 2黑名单用户 
     */
    @Column(name = "user_role")
    private Integer userRole;

    /**
     * 注册来源：1手机号
     */
    @Column(name = "register_source")
    private Integer registerSource;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户昵称
     */
    @Column(name = "nick_name")
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
    @Column(name = "del")
    private Integer del;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}