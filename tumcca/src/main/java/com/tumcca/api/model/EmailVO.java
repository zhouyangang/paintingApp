package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class EmailVO {
    String email;

    public EmailVO() {
    }

    public EmailVO(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "EmailVO{" +
                "email='" + email + '\'' +
                '}';
    }
}
