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
@Table(name = "user_extend")
public class UserExtend {
    /**
     * 用户 ID
     */
    @Id
    private Long uid;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 所属国家
     */
    private String nation;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 县区
     */
    private String district;

    /**
     * 具体地址
     */
    private String location;

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
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}