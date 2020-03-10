package com.xuwen.demo.sdk;

import com.xuwen.demo.bean.dto.LoginResultDto;
import com.xuwen.demo.bean.dto.UserBaseDto;
import com.xuwen.demo.bean.response.BaseResponseBean;
import com.xuwen.demo.bean.response.CommonResponseBean;
import com.xuwen.demo.util.HttpResponse;
import com.xuwen.demo.util.HttpSendUtils;
import com.xuwen.demo.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 单点登录sdk
 * 本地测试时候需配置host（例如127.0.0.1 xuwen.com.cn），这样才能保证可将Cookie写入domain（xuwen.com.cn），
 * @author XuWen
 * @created 2018-04-17 9:17.
 */
public class SsoSdk {

    private static final Logger logger = LoggerFactory.getLogger(SsoSdk.class);

    /**
     * 验证登录信息有效状态码
     */
    private static final int LOGIN_VALIDATE_SUCCESS = 100100005;

    /**
     * 退出登陆操作成功错误码
     */
    private static final int LOGOUT_VALIDATE_SUCCESS = 100100008;

    /**
     * 获取用户基本信息请求成功状态码
     */
    private static final int REQUEST_SUCCESS = 100100000;

    /**
     * 验证是否登陆请求地址
     */
    private static final String VALIDATE_URL = "http://sso.c.bdp.xuwen.com.cn/user/account/validateLogin";

    /**
     * 根据uid获取用户基础信息地址
     */
    private static final String GET_USER_BASE_URL = "http://sso.c.bdp.xuwen.com.cn/user/account/baseInfoByUid/";

    /**
     * 根据token获取用户基础信息地址
     */
    private static final String GET_USER_BASE_BY_TOKEN = "http://sso.c.bdp.xuwen.com.cn/user/account/baseInfoByToken/";

    /**
     * 退出登录
     */
    private static final String LOGOUT_URL = "http://sso.c.bdp.xuwen.com.cn/user/account/logout";

    /**
     * 加个sign字段做防刷验证
     */
    private static final String SIGN = "DATA_CENTER_SSO";

    /**
     * 统一登录页地址
     */
    private static final String LOGIN_URL = "http://c.bdp.xuwen.com.cn/login/login.html?serviceUrl=";

    /**
     * 验证是否登录（包含了登陆超时验证）
     * @param token
     * @return Boolean true表示已登陆，false表示未登录（可设置为登陆页地址）
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static Boolean validateToken(String token){
        if(StringUtils.isEmpty(token)){
            return false;
        }
        HashMap<String, String> headers = new HashMap<>(16);
        headers.put("token", token);
        headers.put("sign", SIGN);
        HttpResponse httpResponse = HttpSendUtils.sendHttpPost(VALIDATE_URL, headers, null);
        logger.info("validate response:{}", JsonUtil.objectToJsonString(httpResponse));
        if(httpResponse != null && !StringUtils.isEmpty(httpResponse.getContent())){
            String content = httpResponse.getContent();
            CommonResponseBean commonResponseBean = JsonUtil.jsonStringToObject(content, CommonResponseBean.class);
            if(commonResponseBean != null && LOGIN_VALIDATE_SUCCESS == commonResponseBean.getReturnCode()){
                return true;
            }
        }
        return false;
    }



    /**
     * token换uid
     * @param token
     * @return
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static Long getUid(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        HashMap<String, String> headers = new HashMap<>(16);
        headers.put("token", token);
        headers.put("sign", SIGN);
        HttpResponse httpResponse = HttpSendUtils.sendHttpPost(VALIDATE_URL, headers, null);
        logger.info("validate response:{}", JsonUtil.objectToJsonString(httpResponse));
        if(httpResponse != null && !StringUtils.isEmpty(httpResponse.getContent())){
            String content = httpResponse.getContent();
            CommonResponseBean commonResponseBean = JsonUtil.jsonStringToObject(content, CommonResponseBean.class);
            if(commonResponseBean != null
                    && LOGIN_VALIDATE_SUCCESS == commonResponseBean.getReturnCode()
                    && commonResponseBean.getResult() != null){
                String jsonString = JsonUtil.objectToJsonString(commonResponseBean.getResult());
                LoginResultDto dto = JsonUtil.jsonStringToObject(jsonString, LoginResultDto.class);
                return dto.getUid();
            }
        }
        return null;
    }

    /**
     * 验证是否登录（包含了登陆超时验证），没登录就自动返回登录页
     * @param token
     * @return "success"表示已登陆，其他为登陆页
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static String validateTokenAndRedirect(String token){
        boolean success = validateToken(token);
        if(success){
            return "success";
        }
        return LOGIN_URL;
    }

    /**
     * 退出登陆
     * @param token
     * @return Boolean true表示退出成功，false表示退出失败
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static Boolean logout(String token){
        if(StringUtils.isEmpty(token)){
            return false;
        }
        HashMap<String, String> headers = new HashMap<>(16);
        headers.put("token", token);
        headers.put("sign", SIGN);
        HttpResponse httpResponse = HttpSendUtils.sendHttpPost(LOGOUT_URL, headers, null);
        logger.info("logout response:{}", JsonUtil.objectToJsonString(httpResponse));
        if(httpResponse != null && !StringUtils.isEmpty(httpResponse.getContent())){
            String content = httpResponse.getContent();
            BaseResponseBean baseResponseBean = JsonUtil.jsonStringToObject(content, CommonResponseBean.class);
            //当登出返回指定状态码则退出成功
            if(baseResponseBean != null && LOGOUT_VALIDATE_SUCCESS == baseResponseBean.getReturnCode()){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用户基础信息
     * @param uid
     * @return  CommonResponseBean为null表示参数错误
     *          CommonResponseBean的result属性为null表示没有获取到用户信息或程序异常
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static CommonResponseBean<UserBaseDto> getUserBaseInfo(Long uid){
        if(uid == null || uid <= 0){
            return null;
        }
        return getUserBaseDto(GET_USER_BASE_URL, uid.toString());
    }

    private static CommonResponseBean<UserBaseDto> getUserBaseDto(String urlPath, String suffixes) {
        CommonResponseBean<UserBaseDto> commonResponseBean = null;
        if(suffixes == null){
            return commonResponseBean;
        }
        HashMap<String, String> headers = new HashMap<>(16);
        headers.put("sign", SIGN);
        String url = new StringBuilder(urlPath).append(suffixes).toString();
        HttpResponse httpResponse = HttpSendUtils.sendHttpGet(url, headers, null);
        logger.info("getUserBaseDto response:{}", JsonUtil.objectToJsonString(httpResponse));
        if(httpResponse != null && !StringUtils.isEmpty(httpResponse.getContent())){
            String content = httpResponse.getContent();
            commonResponseBean = JsonUtil.jsonStringToObject(content, CommonResponseBean.class);
            //当登出返回指定状态码则退出成功
            if(commonResponseBean != null && REQUEST_SUCCESS == commonResponseBean.getReturnCode()){
                String jsonString = JsonUtil.objectToJsonString(commonResponseBean.getResult());
                UserBaseDto result = JsonUtil.jsonStringToObject(jsonString, UserBaseDto.class);
                commonResponseBean.setResult(result);
                return commonResponseBean;
            }
        }
        return commonResponseBean;
    }

    /**
     * 根据token获取用户基本信息
     * @param token
     * @return  CommonResponseBean为null表示参数错误
     *          CommonResponseBean的result属性为null表示没有获取到用户信息或程序异常
     * @throws
     * @author xuwen
     * @Date 2018/4/17 10:38
     */
    public static CommonResponseBean<UserBaseDto> getBaseInfoByToken(String token){
        return getUserBaseDto(GET_USER_BASE_BY_TOKEN, token);
    }

    public static void main(String[] args){
        Boolean success = validateToken("token_1523875528798236");
        System.out.println(success);
        Boolean success1 = logout("token_1523875528798236");
        System.out.println(success1);
        Boolean success2 = validateToken("token_1523875528798236");
        System.out.println(success2);
        getUserBaseInfo(17L);
//        System.out.println(userBaseInfo);
    }

}
