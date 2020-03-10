package com.xuwen.demo.util;

import com.xuwen.demo.constant.CommonConstant;

import java.util.Random;

/**
 * token生成规则
 *
 * @author XuWen
 * @created 2018-04-16 10:48.
 */
public class TokenUtil {

    public static String generatorToken(){

        StringBuilder sbu = new StringBuilder(CommonConstant.TOKEN);
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        return sbu.append(System.currentTimeMillis()).append(randomNumber).toString();
    }

}
