package com.lee.culture.demo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class WsResponse<T> {

    private MessageCode status;
    private String message;
    private T result;

    public WsResponse() {
    }

    public WsResponse(MessageCode status, T result) {
        this.status = status;
        message = status.getMsg();
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageCode getStatus() {
        return status;
    }

    public void setStatus(MessageCode status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static WsResponse failure(String msg) {
        WsResponse resp = new WsResponse();
        resp.status = MessageCode.COMMON_FAILURE;
        if (StringUtils.isBlank(msg)) {

        } else {
            resp.message = msg;
        }
        return resp;
    }

    public static WsResponse success() {
        WsResponse resp = new WsResponse();
        resp.status = MessageCode.COMMON_SUCCESS;
        return resp;
    }

    public static <K> WsResponse<K> success(K t) {
        WsResponse<K> resp = new WsResponse<>();
        resp.status = MessageCode.COMMON_SUCCESS;
        resp.result = t;

        return resp;
    }

    /**
     * 判断字符串是否已经是 WsResponse返回格式
     *
     * @param json
     * @return
     */
    public static boolean isWsResponseJson(String json) {
        if (json != null && json.indexOf("\"status\":") != -1
                && json.indexOf("\"message\":") != -1
                && json.indexOf("\"result\":") != -1) {
            return true;
        } else {
            return false;
        }
    }
}
