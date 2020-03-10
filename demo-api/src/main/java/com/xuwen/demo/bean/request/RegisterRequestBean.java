package com.xuwen.demo.bean.request;

import lombok.Data;

/**
 * 注册请求信息
 *
 * @author XuWen
 * @created 2018-04-14 18:47.
 */
@Data
public class RegisterRequestBean {

    /**
     * 1手机号
     */
    private Integer identityType;

    /**
     * 手机号或第三方应用的唯一标识
     */
    private String identifier;

    /**
     * 密码凭证(站内的保存密码，站外的不保存或保存token)
     */
    private String certificate;
}
