package com.xuwen.demo.service.impl;

import com.xuwen.demo.bean.dto.LoginDto;
import com.xuwen.demo.bean.dto.LoginResultDto;
import com.xuwen.demo.bean.dto.RegisterDto;
import com.xuwen.demo.bean.dto.UpdateCertificateDto;
import com.xuwen.demo.constant.CommonConstant;
import com.xuwen.demo.dao.entity.UserAccount;
import com.xuwen.demo.dao.entity.UserBase;
import com.xuwen.demo.dao.entity.UserExtend;
import com.xuwen.demo.dao.mapper.UserAccountMapper;
import com.xuwen.demo.dao.mapper.UserBaseMapper;
import com.xuwen.demo.dao.mapper.UserExtendMapper;
import com.xuwen.demo.enums.DeleteEnum;
import com.xuwen.demo.enums.IdentityType;
import com.xuwen.demo.enums.ReturnCodeEnum;
import com.xuwen.demo.exception.SsoException;
import com.xuwen.demo.service.UserAccountService;
import com.xuwen.demo.util.RedisUtil;
import com.xuwen.demo.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户账户操作service
 *
 * @author XuWen
 * @created 2018-04-16 9:53.
 */
@Service
public class UserAccountServiceImpl implements UserAccountService{

    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private UserAccountMapper userAccountMapper;

    @Autowired(required = false)
    private UserBaseMapper userBaseMapper;

    @Autowired(required = false)
    private UserExtendMapper userExtendMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor=Exception.class,propagation= Propagation.REQUIRED)
    public Boolean register(RegisterDto registerDto) throws SsoException {

        //先判断账户是否已存在
        UserAccount queryAccount = new UserAccount();
        queryAccount.setIdentifier(registerDto.getIdentifier());
        UserAccount userAccount = userAccountMapper.selectOne(queryAccount);
        logger.info("userAccount:{}", userAccount);
        if(userAccount != null){
            logger.info("账户已存在");
            return false;
        }

        userAccount = new UserAccount();
        BeanUtils.copyProperties(registerDto, userAccount);
        userAccount.setDel(DeleteEnum.IS_EXIST.getCode());
        Date currentDate = new Date();
        userAccount.setCreateTime(currentDate);
        userAccount.setUpdateTime(currentDate);

        //插入账号信息
        userAccountMapper.insertSelective(userAccount);
        //插入用户基本信息
        UserBase userBase = new UserBase();
        BeanUtils.copyProperties(userAccount, userBase);
        //用户名做用户登录账号处理
        userBase.setUserName(userAccount.getIdentifier());
        userBase.setUid(userAccount.getId());
        //注册来源
        userBase.setRegisterSource(userAccount.getIdentityType());
        userBaseMapper.insertSelective(userBase);

        //插入用户扩展信息
        UserExtend userExtend = new UserExtend();
        BeanUtils.copyProperties(userAccount, userExtend);
        userExtend.setUid(userAccount.getId());
        userExtendMapper.insertSelective(userExtend);
        logger.info("注册成功");
        return true;
    }

    @Override
    public LoginResultDto login(LoginDto loginDto) throws SsoException {

        try {
            //根据IdentityType判断是否三方登陆，目前除了手机号登陆类型其他都视作三方登陆
            boolean mobileLogin = IdentityType.MOBILE.equals(IdentityType.valueOf(loginDto.getIdentityType()));

            //三方登陆先验证是否已经有三方用户信息，若有则登陆，若没有就先进行注册，然后再自动登录
            if(!mobileLogin){
                UserAccount thirdAccount = new UserAccount();
                BeanUtils.copyProperties(loginDto, thirdAccount);
                thirdAccount.setIdentifier(loginDto.getIdentifier());
                thirdAccount = userAccountMapper.selectOne(thirdAccount);
                logger.info("third openid:{}, thirdAccount:{}", loginDto.getIdentifier(), thirdAccount);
                if(thirdAccount.getId() != null && thirdAccount.getId() > 0){
                    RegisterDto registerDto = new RegisterDto();
                    BeanUtils.copyProperties(loginDto, registerDto);
                    register(registerDto);
                }
            }

            //查询账户和密码是否正确
            UserAccount userAccount = new UserAccount();
            BeanUtils.copyProperties(loginDto, userAccount);
            UserAccount userAccountResult = userAccountMapper.selectOne(userAccount);
            logger.info("userAccountResult:{}", userAccountResult);
            if(userAccountResult == null){
                return null;
            }

            //若查询正确匹配则生成token对应uid存入redis
            String token = TokenUtil.generatorToken();
            logger.info("token:{}", token);

            //redis中数据类型存String-String
            redisUtil.setAndExpire(token, String.valueOf(userAccountResult.getId()), CommonConstant.EXPIRE_TIME, CommonConstant.TIMEUNIT);
            LoginResultDto loginResultDto = new LoginResultDto();
            loginResultDto.setToken(token);
            loginResultDto.setUid(userAccountResult.getId());
            loginResultDto.setServiceUrl(loginDto.getServiceUrl());
            return loginResultDto;
        } catch (Exception e) {
            logger.error("exception：{}, error message：{}", e, e.getMessage());
            e.printStackTrace();
            throw new SsoException(ReturnCodeEnum.LOGIN_FAIL);
        }
    }

    @Override
    public LoginResultDto validateLogin(String token) throws SsoException {
        try {
            //取uid
            Object uidObject = redisUtil.get(token);
            if(uidObject == null || !(uidObject instanceof String)){
                return null;
            }
            Long uid = Long.valueOf(String.valueOf(uidObject));
            //重新设置过期时间
            redisUtil.setAndExpire(token, uidObject, CommonConstant.EXPIRE_TIME, CommonConstant.TIMEUNIT);
            LoginResultDto loginResultDto = new LoginResultDto();
            loginResultDto.setToken(token);
            loginResultDto.setUid(uid);
            logger.info("validateLogin loginResultDto:{}", loginResultDto);
            return loginResultDto;
        } catch (NumberFormatException e) {
            //类型转换异常说明token取出来的uid无效，则直接返回空表示未登录即可
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean logout(String token) throws SsoException {
        redisUtil.remove(token);
        logger.info("logOut success:{}", token);
        return true;
    }

    @Override
    public Boolean updateCertificate(UpdateCertificateDto updateCertificateDto) throws SsoException {
        //账户密码是否匹配
        UserAccount userAccount = new UserAccount();
        userAccount.setIdentifier(updateCertificateDto.getIdentifier());
        userAccount.setCertificate(updateCertificateDto.getOldCertificate());
        UserAccount userAccountResult = userAccountMapper.selectOne(userAccount);
        //账户密码不匹配直接返回
        if(userAccountResult == null){
            return false;
        }
        //若匹配则修改密码
        userAccountResult.setCertificate(updateCertificateDto.getCertificate());
        userAccountResult.setUpdateTime(new Date());
        userAccountMapper.updateByPrimaryKey(userAccountResult);
        // todo...
        //redis中登陆token是否需要删除待定，若修改密码后需重新登陆则需要删除，不需要重新登陆则不需要删除
        return true;
    }

    @Override
    public UserBase getBaseInfo(Long uid) throws SsoException {
        UserBase userBase = userBaseMapper.selectByPrimaryKey(uid);
        logger.info("userBase:{}", userBase);
        return userBase;
    }

    @Override
    public UserExtend getExtendInfo(Long uid) throws SsoException {
        UserExtend userExtend = userExtendMapper.selectByPrimaryKey(uid);
        logger.info("userExtend:{}", userExtend);
        return userExtend;
    }
}
