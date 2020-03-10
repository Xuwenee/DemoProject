package com.xuwen.demo.enums;

/**
 * ${DESCRIPTION}
 *
 * @author XuWen
 * @created 2018-04-14 18:07.
 */
public enum DeleteEnum {

    IS_EXIST(0,"未删除"),
    IS_DELETE(1,"已删除"),
    ;

    private Integer code;
    private String value;

    DeleteEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static DeleteEnum valueOf(int code) {
        for (DeleteEnum deleteEnum : DeleteEnum.values()) {
            if (deleteEnum.code == code) {
                return deleteEnum;
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
