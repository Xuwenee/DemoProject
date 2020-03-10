package com.xuwen.demo.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 错误定义
 *
 * @author XuWen
 * @Date 2018/4/11 10:25
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMsgBean implements Serializable {
	
	private static final long serialVersionUID = -7447689818750410956L;

	@JsonProperty("error_id")
	private String errorId;
	
	@JsonProperty("error_code")
	private int errorCode;
	
	@JsonProperty("msg")
	private String errorMsg;

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
