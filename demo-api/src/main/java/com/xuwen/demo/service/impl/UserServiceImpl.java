package com.xuwen.demo.service.impl;

import com.xuwen.demo.async.AsyncLog;
import com.xuwen.demo.dao.mapper.UserAccountMapper;
import com.xuwen.demo.service.UserService;
import com.xuwen.demo.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author XuWen
 * @created 2018-04-14 13:34.
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AsyncLog asyncLog;

    @Override
    public void add() {
//        UserAccount userAccount = new UserAccount();
//        userAccount.setCertificate("ces");
//        userAccount.setCreateTime(new Date());
//        userAccount.setIdentifier("333");
//        userAccount.setIdentityType(111);
//        userAccount.setDel(DeleteEnum.IS_EXIST.getCode());
//        userAccount.setUpdateTime(new Date());
//        int insert = userAccountMapper.insert(userAccount);
        logger.info("");
        redisUtil.set("xuwen111", "许稳");
        redisUtil.setAndExpire("token", String.valueOf(12345L), 30L, TimeUnit.SECONDS);
//        logger.info("insert:{}", insert);

        asyncLog.addLog();
        logger.info("asyncLog.addLog()");

        //测试取redis中的值
        Object uidObject = redisUtil.get("token");
        if(uidObject == null || !(uidObject instanceof String)){
            logger.info("uidObject null");
        }
        Long uid = Long.valueOf(String.valueOf(uidObject));
        logger.info("uid:{}", uid);

    }
}
