package com.xuwen.demo.controller;

import com.xuwen.demo.bean.dto.LoginDto;
import com.xuwen.demo.bean.dto.LoginResultDto;
import com.xuwen.demo.bean.dto.RegisterDto;
import com.xuwen.demo.bean.dto.UpdateCertificateDto;
import com.xuwen.demo.bean.request.LoginRequestBean;
import com.xuwen.demo.bean.request.RegisterRequestBean;
import com.xuwen.demo.bean.request.UpdateCertificateRequestBean;
import com.xuwen.demo.bean.response.BaseResponseBean;
import com.xuwen.demo.dao.entity.UserBase;
import com.xuwen.demo.enums.ReturnCodeEnum;
import com.xuwen.demo.exception.SsoException;
import com.xuwen.demo.service.UserAccountService;
import com.xuwen.demo.util.EncryptUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户账户API
 *
 * @author XuWen
 * @Date 2018/4/14 14:03
 */
@RestController
@RequestMapping("/user/account")
public class UserAccountController extends BaseController{

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private UserAccountService userAccountService;

	@ApiOperation(value="用户注册")
	@RequestMapping(value ="/register", method =RequestMethod.POST)
	public BaseResponseBean register(@RequestBody RegisterRequestBean registerRequestBean, HttpServletRequest request) {
		try {
			String clientIp = getClientIp(request);
			logger.info("client ip:{}", clientIp);
			RegisterDto registerDto = new RegisterDto();
			BeanUtils.copyProperties(registerRequestBean, registerDto);
			//转换为加密后的密码
			registerDto.setCertificate(EncryptUtil.md5Hash(registerRequestBean.getCertificate()));
			Boolean success = userAccountService.register(registerDto);
			ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.REGISTER_SUCCESS;
			if(!success){
				returnCodeEnum = ReturnCodeEnum.ACCOUNT_EXISTED;
			}
			return ok(returnCodeEnum);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.REGISTER_FAIL);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exception：{}, error message：{}", e, e.getMessage());
			return error(ReturnCodeEnum.REGISTER_ERROR);
		}
	}

	@ApiOperation(value="用户登入")
	@RequestMapping(value ="/login", method = RequestMethod.POST)
	public BaseResponseBean login(@RequestBody LoginRequestBean loginRequestBean) {
		try {
			//参数校验
//			if(!loginRequestBean.validate()){
//				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
//			}
			//登陆处理
			LoginDto loginDto = new LoginDto();
			BeanUtils.copyProperties(loginRequestBean, loginDto);
			//传递的是明文密码则转换为加密后的密码
			loginDto.setCertificate(EncryptUtil.md5Hash(loginRequestBean.getCertificate()));
			LoginResultDto loginResultDto = userAccountService.login(loginDto);
			ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.LOGIN_SUCCESS;
			if(loginResultDto == null){
				returnCodeEnum = ReturnCodeEnum.ACCOUNT_ERROR;
				return ok(returnCodeEnum, null);
			}
			return ok(returnCodeEnum, loginResultDto);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.LOGIN_FAIL, null);
		}
	}

	@ApiOperation(value="验证登入信息是否有效")
	@RequestMapping(value ="/validateLogin", method = RequestMethod.POST)
	public BaseResponseBean validateLogin(@RequestHeader String token) {
		try {
			//参数校验
			if(token == null && token.length() <= 0){
				return ok(ReturnCodeEnum.LOGIN_VALIDATE_FAIL, null);
			}
			LoginResultDto loginResultDto = userAccountService.validateLogin(token);
			ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.LOGIN_VALIDATE_SUCCESS;
			//返回空则表示未登录或者登录超时
			if(loginResultDto == null){
				returnCodeEnum = ReturnCodeEnum.LOGIN_VALIDATE_FAIL;
				return ok(returnCodeEnum, null);
			}
			return ok(returnCodeEnum, loginResultDto);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.LOGIN_VALIDATE_ERROR, null);
		}
	}

	@ApiOperation(value="用户登出")
	@RequestMapping(value ="/logout", method = RequestMethod.POST)
	public BaseResponseBean logout(@RequestHeader String token) {
		try {
			Boolean success = userAccountService.logout(token);
			ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.LOGOUT_VALIDATE_SUCCESS;
			if(!success){
				returnCodeEnum = ReturnCodeEnum.LOGOUT_VALIDATE_FAIL;
			}
			return ok(returnCodeEnum);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.LOGOUT_VALIDATE_ERROR);
		}
	}

	@ApiOperation(value="根据uid获取用户基本信息")
	@RequestMapping(value ="/baseInfoByUid/{uid}", method = RequestMethod.GET)
	public BaseResponseBean getBaseInfo(@PathVariable Long uid) {
		try {
			//参数校验
			if(uid == null || uid <= 0){
				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
			}
			UserBase userBase = userAccountService.getBaseInfo(uid);
			return ok(ReturnCodeEnum.REQUEST_SUCCESS, userBase);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.REQUEST_ERROR);
		}
	}

	@ApiOperation(value="根据uid获取用户扩展信息")
	@RequestMapping(value ="/extendInfo/{uid}", method = RequestMethod.GET)
	public String getExtendInfo(@PathVariable Long uid) {
//		redisTemplate.opsForValue().increment("count", 1);
		logger.info("uid:{}", uid);
//		try {
//			int i = new Random().nextInt(3);
//			Thread.sleep(7000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		try {
			//参数校验
//			if(uid == null || uid <= 0){
//				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
//			}
//			UserExtend userExtend = userAccountService.getExtendInfo(uid);
//			UserExtend userExtend = new UserExtend();

//			return ok(ReturnCodeEnum.REQUEST_SUCCESS, "success");
		return "test";
//		} catch (SsoException e) {
//			e.printStackTrace();
//			return error(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(ReturnCodeEnum.REQUEST_ERROR);
//		}
	}

	@ApiOperation(value="根据uid获取用户扩展信息")
	@RequestMapping(value ="/getExtendInfotest/{uid}", method = RequestMethod.GET)
	public String getExtendInfotest(@PathVariable Long uid) {
//		redisTemplate.opsForValue().increment("count", 1);
		logger.info("uid:{}");
//		try {
//			int i = new Random().nextInt(3);
//			Thread.sleep(15000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		try {
		//参数校验
//			if(uid == null || uid <= 0){
//				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
//			}
//			UserExtend userExtend = userAccountService.getExtendInfo(uid);
//			UserExtend userExtend = new UserExtend();

//			return ok(ReturnCodeEnum.REQUEST_SUCCESS, "success");
		return "test";
//		} catch (SsoException e) {
//			e.printStackTrace();
//			return error(e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return error(ReturnCodeEnum.REQUEST_ERROR);
//		}
	}

	@ApiOperation(value="修改密码")
	@RequestMapping(value ="/updateCertificate", method = RequestMethod.PUT)
	public BaseResponseBean updateCertificate(@RequestBody UpdateCertificateRequestBean updateCertificateRequestBean) {
		try {
			//参数校验
			if(!updateCertificateRequestBean.validate()){
				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
			}
			UpdateCertificateDto updateCertificateDto = new UpdateCertificateDto();
			BeanUtils.copyProperties(updateCertificateRequestBean, updateCertificateDto);
			updateCertificateDto.setOldCertificate(EncryptUtil.md5Hash(updateCertificateRequestBean.getOldCertificate()));
			updateCertificateDto.setCertificate(EncryptUtil.md5Hash(updateCertificateRequestBean.getCertificate()));
			Boolean success = userAccountService.updateCertificate(updateCertificateDto);
			ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.UPDATE_CERTIFICATE_SUCCESS;
			if(!success){
				returnCodeEnum = ReturnCodeEnum.UPDATE_CERTIFICATE_FAIL;
			}
			return ok(returnCodeEnum);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.UPDATE_CERTIFICATE_ERROR);
		}
	}

	@ApiOperation(value="根据token获取用户基本信息")
	@RequestMapping(value ="/baseInfoByToken/{token}", method = RequestMethod.GET)
	public BaseResponseBean getBaseInfoByToken(@PathVariable String token) {
		try {
			//参数校验
			if(token == null && token.length() <= 0){
				return ok(ReturnCodeEnum.PARAMETER_ERROR, null);
			}
			LoginResultDto loginResultDto = userAccountService.validateLogin(token);
			if(loginResultDto == null || loginResultDto.getUid() == null){
				return ok(ReturnCodeEnum.LOGIN_VALIDATE_FAIL, null);
			}
			Long uid = loginResultDto.getUid();
			//参数校验
			if(uid == null || uid <= 0){
				return error(ReturnCodeEnum.PARAMETER_ERROR, null);
			}
			UserBase userBase = userAccountService.getBaseInfo(uid);
			return ok(ReturnCodeEnum.REQUEST_SUCCESS, userBase);
		} catch (SsoException e) {
			e.printStackTrace();
			return error(e);
		} catch (Exception e) {
			e.printStackTrace();
			return error(ReturnCodeEnum.REQUEST_ERROR);
		}
	}

}
