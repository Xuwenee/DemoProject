package com.xuwen.demo.enums;

import javax.ws.rs.core.Response;

/**
 * SSO服务统一错误码定义
 *
 * @author XuWen
 * @Date 2018/4/11 10:11
 */
public enum ReturnCodeEnum {

    REQUEST_SUCCESS(100100000, "请求成功", Response.Status.OK),
    PARAMETER_ERROR(100100001, "参数错误", Response.Status.BAD_REQUEST),
    ACCOUNT_ERROR(100100002, "用户名或密码错误", Response.Status.OK),
    LOGIN_SUCCESS(100100003, "登入成功", Response.Status.OK),
    LOGIN_FAIL(100100004, "登入失败", Response.Status.INTERNAL_SERVER_ERROR),
    LOGIN_VALIDATE_SUCCESS(100100005, "现为登入状态", Response.Status.OK),
    LOGIN_VALIDATE_FAIL(100100006, "未登入或登入超时", Response.Status.OK),
    LOGIN_VALIDATE_ERROR(100100007, "验证出错", Response.Status.INTERNAL_SERVER_ERROR),
    LOGOUT_VALIDATE_SUCCESS(100100008, "登出成功", Response.Status.OK),
    LOGOUT_VALIDATE_FAIL(100100009, "登出失败", Response.Status.OK),
    LOGOUT_VALIDATE_ERROR(100100010, "登出出错", Response.Status.OK),
    REGISTER_SUCCESS(100100011, "注册成功", Response.Status.OK),
    REGISTER_FAIL(100100012, "注册失败", Response.Status.OK),
    REGISTER_ERROR(100100013, "注册出错", Response.Status.OK),
    ACCOUNT_EXISTED(100100014, "账户已存在", Response.Status.OK),
    UPDATE_CERTIFICATE_SUCCESS(100100015, "密码修改成功", Response.Status.OK),
    UPDATE_CERTIFICATE_FAIL(100100016, "密码修改失败", Response.Status.OK),
    UPDATE_CERTIFICATE_ERROR(100100017, "密码修改出错", Response.Status.OK),
    REQUEST_ERROR(100100018, "请求出错", Response.Status.INTERNAL_SERVER_ERROR),
    ;

    private final int returnCode;
    private String message;
    private Response.Status httpStatus;

    ReturnCodeEnum(int errorCode, String errorMsg, Response.Status httpStatus) {
        this.returnCode = errorCode;
        this.message = errorMsg;
        this.httpStatus = httpStatus;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getMessage() {
        return message;
    }

    public static ReturnCodeEnum valueOf(int errorCode) {
        for (ReturnCodeEnum accountError : ReturnCodeEnum.values()) {
            if (accountError.returnCode == errorCode) {
                return accountError;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return Integer.toString(this.returnCode);
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public void setErrorMsg(String errorMsg) {
        this.message = errorMsg;
    }

    public void setStatus(Response.Status httpStatus) {
        this.httpStatus = httpStatus;
    }
}
