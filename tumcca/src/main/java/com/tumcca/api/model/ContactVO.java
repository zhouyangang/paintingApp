package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-21
 */
public class ContactVO {
    String email;
    String mobile;

    public ContactVO() {
    }

    public ContactVO(String email, String mobile) {
        this.email = email;
        this.mobile = mobile;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getMobile() {
        return mobile;
    }
}
