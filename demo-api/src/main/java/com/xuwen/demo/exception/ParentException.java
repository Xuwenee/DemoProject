package com.xuwen.demo.exception;

import com.xuwen.demo.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 自定义异常统一父类
 *
 * @author XuWen
 * @Date 2018/4/11 10:30
 */
public abstract class ParentException extends Exception {
    private static final long serialVersionUID = 713616801491210431L;
    private ErrorMsgBean errorMsgBean = new ErrorMsgBean();

    public ParentException() {
    }

    public ErrorMsgBean getErrorMsgBean() {
        return this.errorMsgBean;
    }

    public void setErrorMsgBean(ErrorMsgBean errorMsgBean) {
        this.errorMsgBean = errorMsgBean;
    }

    public int getErrorCode() {
        return this.errorMsgBean.getErrorCode();
    }

    public void setErrorCode(int errorCode) {
        this.errorMsgBean.setErrorCode(errorCode);
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsgBean.setErrorMsg(errorMsg);
    }

    public String getErrorMsg() {
        return this.errorMsgBean.getErrorMsg();
    }

    public String getErrorId() {
        return this.errorMsgBean.getErrorId();
    }

    public void setErrorId(String errorId) {
        this.errorMsgBean.setErrorId(errorId);
    }

    public String toResponseJson() {
        if(StringUtils.isBlank(this.errorMsgBean.getErrorId())) {
            this.errorMsgBean.setErrorId(UUID.randomUUID().toString());
        }

        String responseStr = JsonUtil.objectToJsonString(this.errorMsgBean);
        return responseStr;
    }
}