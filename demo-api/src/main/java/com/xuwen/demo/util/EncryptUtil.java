package com.xuwen.demo.util;

import com.xuwen.demo.constant.CommonConstant;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密工具类
 *
 * @author XuWen
 * @created 2018-04-16 15:23.
 */
public class EncryptUtil {



    /**
     * md5加盐加密，不可逆加密
     * @param password
     * @return String
     * @author xuwen
     * @Date 2018/4/16 15:23
     */
    public static String md5Hash(String password){
        return new Md5Hash(password, CommonConstant.SALT, CommonConstant.HASH_ITERATIONS).toString();
    }


}
