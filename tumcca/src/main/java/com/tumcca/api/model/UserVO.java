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
public class UserVO {
    String email;
    String mobile;
    String authority;

    public UserVO() {
    }

    public UserVO(String email, String mobile, String authority) {
        this.email = email;
        this.mobile = mobile;
        this.authority = authority;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getMobile() {
        return mobile;
    }

    @JsonProperty
    public String getAuthority() {
        return authority;
    }
}
