package com.xuwen.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis基本操作工具类
 *
 * @author XuWen
 * @created 2018-04-14 16:13.
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据key删除
     * @param key
     */
    public void remove(String key){
        if(this.isExistKey(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * 根据key批量删除
     * @param keys
     */
    public void remove(String... keys){
        for(String key : keys){
            this.remove(key);
        }
    }

    /**
     * 根据key判断是否存在对应的value
     * @param key
     * @return
     */
    public boolean isExistKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public Object get(String key){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
        return true;
    }

    /**
     * 写入缓存，可自定义超时时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean setAndExpire(String key, Object value, Long expireTime, TimeUnit timeUnit){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
        redisTemplate.expire(key, expireTime, timeUnit);
        return true;
    }

}