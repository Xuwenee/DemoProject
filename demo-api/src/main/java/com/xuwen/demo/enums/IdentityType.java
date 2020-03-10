package com.xuwen.demo.enums;

/**
 * 账号类型
 *
 * @author XuWen
 * @created 2018-04-16 9:27.
 */
public enum IdentityType {

    MOBILE(1,"手机号，或未知"),

    QQ(2,"QQ登陆"),

    WECHAT(3,"微信"),

    SINA(4,"新浪微博"),
    ;

    private Integer code;
    private String value;

    IdentityType(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static IdentityType valueOf(Integer code) {
        if(code == null){
            return null;
        }
        for (IdentityType type : IdentityType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
