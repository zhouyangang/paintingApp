package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-05
 */
public class ErrorVO {
    Integer code;
    String message;

    public ErrorVO() {
    }

    public ErrorVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty
    public Integer getCode() {
        return code;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
