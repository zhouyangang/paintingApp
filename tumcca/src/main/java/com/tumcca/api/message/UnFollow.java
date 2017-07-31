package com.tumcca.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-23
 */
public class UnFollow {
    @NotNull
    Long follower;
    @NotNull
    Long following;

    public UnFollow() {
    }

    public UnFollow(Long follower, Long following) {
        this.follower = follower;
        this.following = following;
    }

    @JsonProperty
    public Long getFollower() {
        return follower;
    }

    @JsonProperty
    public Long getFollowing() {
        return following;
    }

    @Override
    public String toString() {
        return "UnFollow{" +
                "follower=" + follower +
                ", following=" + following +
                '}';
    }
}
