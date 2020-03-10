package com.xuwen.demo.util;

import java.io.Serializable;

/**
 * 用于httpclient请求的response封装类
 *
 * @author XuWen
 * @Date 2018/4/17 10:55
 */
public class HttpResponse implements Serializable{

	/**
	 * @fields serialVersionUID TODO
	 */
	private static final long serialVersionUID = 8140069301565768594L;
	
	
	private int status;
	
	private String content;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "HttpResponse [status=" + status + ", content=" + content + "]";
	}
}
