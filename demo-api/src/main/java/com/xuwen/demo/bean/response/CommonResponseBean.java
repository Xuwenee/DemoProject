package com.xuwen.demo.bean.response;

import lombok.Data;

/**
 * 请求返回通用类
 *
 * @author XuWen
 * @created 2018-04-10 14:44.
 */
@Data
public class CommonResponseBean <T> extends BaseResponseBean {

    /**
     * 返回结果实体信息
     */
    private T result;
}
