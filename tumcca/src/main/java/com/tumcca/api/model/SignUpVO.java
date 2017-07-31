package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class SignUpVO {
    @Length(max = 32)
    String email;
    @Length(max = 32)
    String mobile;
    @Length(max = 32)
    @NotBlank
    String password;

    public SignUpVO() {
    }

    public SignUpVO(String email, String mobile, String password) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
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
    public String getPassword() {
        return password;
    }
}
