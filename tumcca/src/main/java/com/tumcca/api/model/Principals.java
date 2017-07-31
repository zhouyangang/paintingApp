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
public class Principals {
    @JsonProperty
    Long uid;
    @JsonProperty
    String token;

    public Principals() {
    }

    public Principals(Long uid, String token) {
        this.uid = uid;
        this.token = token;
    }

    public Long getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Principals{" +
                "uid=" + uid +
                ", token='" + token + '\'' +
                '}';
    }
}
