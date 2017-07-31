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
public class MobileVO {
    String mobile;

    public MobileVO() {
    }

    public MobileVO(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty
    public String getMobile() {
        return mobile;
    }

    @Override
    public String toString() {
        return "MobileVO{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
