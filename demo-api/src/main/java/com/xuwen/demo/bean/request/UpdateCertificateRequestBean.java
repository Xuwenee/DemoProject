package com.xuwen.demo.bean.request;

import lombok.Data;

/**
 * 修改密码请求信息
 *
 * @author XuWen
 * @created 2018-04-16 9:24.
 */
@Data
public class UpdateCertificateRequestBean {

    /**
     * 手机号或第三方应用的登陆唯一标识
     */
    private String identifier;

    /**
     * 旧密码凭证(站内的保存密码，站外的不保存或保存token)
     */
    private String oldCertificate;

    /**
     * 新密码凭证(站内的保存密码，站外的不保存或保存token)
     */
    private String certificate;

    public boolean validate(){
        return true;
    }

}
