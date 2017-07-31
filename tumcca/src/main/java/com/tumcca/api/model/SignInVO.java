package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SignInVO {
    @NotBlank
    String username;
    @NotBlank
    String password;

    public SignInVO() {
    }

    public SignInVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SignInVO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
