package com.xuwen.demo.bean.request;

import com.xuwen.demo.enums.IdentityType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 登陆请求信息
 *
 * @author XuWen
 * @created 2018-04-16 9:24.
 */
@Data
public class LoginRequestBean {

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

    /**
     * 若不是三方登陆，验证密码不能为空
     * @return
     */
    public boolean validate(){
        if(StringUtils.isEmpty(identifier)){
            return false;
        }
        IdentityType identityType = IdentityType.valueOf(this.identityType);
        if(identityType == null){
            return false;
        }
        if(IdentityType.MOBILE.equals(identityType) && StringUtils.isEmpty(certificate)){
            return false;
        }
        return true;
    }

}
