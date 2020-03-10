package com.xuwen.demo.bean.response;

import lombok.Data;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author XuWen
 * @created 2018-04-10 14:44.
 */
@Data
public class BaseResponseBean implements Serializable {

    private static final long serialVersionUID = -6221018956291221771L;

    /**
     * http状态码
     */
    private int httpStatus;

    /**
     * 状态码
     */
    private int returnCode;

    /**
     * 描述信息
     */
    private String message;
}
