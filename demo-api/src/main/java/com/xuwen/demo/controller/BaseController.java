package com.xuwen.demo.controller;

import com.xuwen.demo.bean.response.BaseResponseBean;
import com.xuwen.demo.bean.response.CommonResponseBean;
import com.xuwen.demo.enums.ReturnCodeEnum;
import com.xuwen.demo.exception.SsoException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller基类
 *
 * @author XuWen
 * @Date 2018/4/16 9:09
 */
public class BaseController {

	protected final transient Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * 正常响应（返回对象包含实体结果信息）-200
	 *
	 * @param returnCodeEnum
	 * @return
	 */
	public <T> BaseResponseBean ok(ReturnCodeEnum returnCodeEnum, T t) {
		BaseResponseBean baseResponseBean = getBaseResponseBean(returnCodeEnum);
		CommonResponseBean<T> responseBean = new CommonResponseBean();
		BeanUtils.copyProperties(baseResponseBean, responseBean);
		responseBean.setResult(t);
		return responseBean;
	}

	/**
	 * 正常响应（返回对象为基本响应信息）-200
	 *
	 * @param returnCodeEnum
	 * @ren
	 */
	public BaseResponseBean ok(ReturnCodeEnum returnCodeEnum) {
		BaseResponseBean responseBean = getBaseResponseBean(returnCodeEnum);
		return responseBean;
	}

	private BaseResponseBean getBaseResponseBean(ReturnCodeEnum returnCodeEnum) {
		BaseResponseBean responseBean = new BaseResponseBean();
		responseBean.setHttpStatus(returnCodeEnum.getHttpStatus().getStatusCode());
		responseBean.setReturnCode(returnCodeEnum.getReturnCode());
		responseBean.setMessage(returnCodeEnum.getMessage());
		return responseBean;
	}

	/**
	 * 服务器内部错误
	 *
	 * @param e 异常
	 */
	public BaseResponseBean error(SsoException e) {
		BaseResponseBean responseBean = getErrorBaseResponseBean(e);
		return responseBean;
	}

	private BaseResponseBean getErrorBaseResponseBean(SsoException e) {
		logger.error("SSOException:{}", e.toResponseJson());
		BaseResponseBean responseBean = new BaseResponseBean();
		responseBean.setHttpStatus(e.getStatus().getStatusCode());
		responseBean.setReturnCode(e.getErrorCode());
		responseBean.setMessage(e.getMessage());
		return responseBean;
	}


	/**
	 * 服务器内部错误
	 *
	 * @param e 异常
	 */
	public <T> BaseResponseBean error(SsoException e, T t) {
		BaseResponseBean baseResponseBean = getErrorBaseResponseBean(e);
		CommonResponseBean<T> responseBean = new CommonResponseBean();
		BeanUtils.copyProperties(baseResponseBean, responseBean);
		responseBean.setResult(t);
		return baseResponseBean;
	}

	/**
	 * 服务器内部错误
	 *
	 * @param returnCodeEnum 错误消息
	 */
	public BaseResponseBean error(ReturnCodeEnum returnCodeEnum) {
		logger.error("error message:{}", returnCodeEnum.getMessage());
		BaseResponseBean baseResponseBean = getBaseResponseBean(returnCodeEnum);
		return baseResponseBean;
	}


	/**
	 * 服务器内部错误
	 *
	 * @param returnCodeEnum 错误消息
	 */
	public <T> BaseResponseBean error(ReturnCodeEnum returnCodeEnum, T t) {
		logger.info("error message:{}", t == null? null : t.toString());
		BaseResponseBean baseResponseBean = getBaseResponseBean(returnCodeEnum);
		CommonResponseBean<T> responseBean = new CommonResponseBean();
		BeanUtils.copyProperties(baseResponseBean, responseBean);
		responseBean.setResult(t);
		return baseResponseBean;
	}

	/**
	 * 获取客户端真实ip
	 *
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
}
