package com.xuwen.demo.service;

import com.xuwen.demo.bean.dto.LoginDto;
import com.xuwen.demo.bean.dto.LoginResultDto;
import com.xuwen.demo.bean.dto.RegisterDto;
import com.xuwen.demo.bean.dto.UpdateCertificateDto;
import com.xuwen.demo.dao.entity.UserBase;
import com.xuwen.demo.dao.entity.UserExtend;
import com.xuwen.demo.exception.SsoException;

/**
 * 用户账户操作
 *
 * @author XuWen
 * @created 2018-04-16 9:53.
 */
public interface UserAccountService {

    /**
     * 用户注册
     * @param registerDto
     * @return Boolean
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/16 16:35
     */
    Boolean register(RegisterDto registerDto) throws SsoException;
    
    /**
     * 用户登入
     * @param loginDto
     * @return LoginResultDto
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/16 10:08
     */
    LoginResultDto login(LoginDto loginDto) throws SsoException;

    /**
     * 验证登陆token是否有效
     * @param token
     * @return LoginResultDto
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/16 13:32
     */
    LoginResultDto validateLogin(String token) throws SsoException;

    /**
     * 用户登出
     * @param token
     * @return Boolean
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/16 14:26
     */
    Boolean logout(String token) throws SsoException;

    /**
     * 修改密码
     * @param updateCertificateDto
     * @return Boolean
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/16 20:52
     */
    Boolean updateCertificate(UpdateCertificateDto updateCertificateDto) throws SsoException;

    /**
     * 获取用户基本信息
     * @param uid
     * @return UserBase
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/17 10:09
     */
    UserBase getBaseInfo(Long uid) throws SsoException;

    /**
     * 获取用户扩展信息
     * @param uid
     * @return UserExtend
     * @throws SsoException
     * @author xuwen
     * @Date 2018/4/17 10:09
     */
    UserExtend getExtendInfo(Long uid) throws SsoException;
}
