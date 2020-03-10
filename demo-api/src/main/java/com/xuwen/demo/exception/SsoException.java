package com.xuwen.demo.exception;

import com.xuwen.demo.enums.ReturnCodeEnum;
import org.springframework.util.StringUtils;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * 车小秘服务统一异常
 *
 * @author XuWen
 * @Date 2018/4/11 10:11
 */
public class SsoException extends ParentException {

    private static final long serialVersionUID = -6215751052263475161L;

    /**
     * http状态码
     */
    private Response.Status status;

    public SsoException(ReturnCodeEnum err) {
        super();
        setErrorCode(err.getReturnCode());
        setErrorMsg(err.getMessage());
        setErrorId(UUID.randomUUID().toString());
        setStatus(err.getHttpStatus());
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        StringBuilder sb = new StringBuilder(super.getErrorMsg());
        if (!StringUtils.isEmpty(msg)) {
            sb.append(" ").append(msg);
        }
        return sb.toString();
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

}
