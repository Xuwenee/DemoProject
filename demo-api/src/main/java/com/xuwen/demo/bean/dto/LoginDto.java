package com.xuwen.demo.bean.dto;

import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author XuWen
 * @created 2018-04-16 9:56.
 */
@Data
public class LoginDto {

    /**
     * 1 手机号 后期会接入三方登陆
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

    /**
     * 登陆成功需跳转的服务地址
     */
    private String serviceUrl;
}
