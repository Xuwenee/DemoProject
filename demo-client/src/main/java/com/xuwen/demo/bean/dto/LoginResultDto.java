package com.xuwen.demo.bean.dto;

import lombok.Data;

/**
 * 登陆业务操作返回结果
 *
 * @author XuWen
 * @created 2018-04-16 9:58.
 */
@Data
public class LoginResultDto {

    /**
     * 登陆用户id
     */
    private Long uid;

    /**
     * 登陆票据
     */
    private String token;
}
