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
 * @since 2015-06-23
 */
public class Admin {
    @NotBlank
    String email;
    @NotBlank
    String mobile;
    @NotBlank
    String password;
    @NotBlank
    Long homePageUid;

	public Admin() {
    }

    public Admin(String email, String mobile, String password, Long homePageUid) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.homePageUid = homePageUid;
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

    @JsonProperty
    public Long getHomePageUid() {
		return homePageUid;
	}
}
