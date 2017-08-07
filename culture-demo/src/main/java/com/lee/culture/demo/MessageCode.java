package com.lee.culture.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by zhengjun.jing on 7/13/2017.
 */
public enum MessageCode {
    /** ===========系统公用 Message Code===================*/
    /** 前缀 0000*/
    COMMON_SUCCESS("000","执行成功"),
    COMMON_FAILURE("001", "执行失败");

    //Message 编码
    private String code;
    //Message 描叙
    private String message;

    MessageCode(String code){
        this.code = code;
    }
    
    MessageCode(String code,String message){
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }


    @JsonCreator
    public static MessageCode getStatusCode(String status) {
        for (MessageCode unit : MessageCode.values()) {
            if (unit.getCode().equals(status)) {
                return unit;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return "{code:'" + code + '\'' +
                ", message:'" + message + '\'' +
                '}';
    }
}
