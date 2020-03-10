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
@Table(name = "user_account")
public class UserAccount {
    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 1手机号
     */
    @Column(name = "identity_type")
    private Integer identityType;

    /**
     * 手机号或第三方应用的唯一标识
     */
    private String identifier;

    /**
     * 密码凭证(站内的保存密码，站外的不保存或保存token)
     */
    private String certificate;

    /**
     * 创建或绑定时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 逻辑删除 0未删除；1已删除
     */
    @Column(name = "del")
    private Integer del;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}