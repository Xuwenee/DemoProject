package com.xuwen.demo.bean.dto;

import lombok.Data;

/**
 * 用户注册属性
 *
 * @author XuWen
 * @created 2018-04-16 16:32.
 */
@Data
public class RegisterDto {

    /**
     * 1手机号
     */
    private Integer identityType;

    /**
     * 手机号或第三方应用的登陆唯一标识
     */
    private String identifier;

    /**
     * 密码凭证(站内的保存密码，站外的不保存或保存token)
     */
    private String certificate;
}
