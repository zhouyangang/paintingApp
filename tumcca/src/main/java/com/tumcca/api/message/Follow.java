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
 * @since 2015-06-22
 */
public class Follow {
    @NotNull
    Long follower;
    @NotNull
    Long toFollow;

    public Follow() {
    }

    public Follow(Long follower, Long toFollow) {
        this.follower = follower;
        this.toFollow = toFollow;
    }

    @JsonProperty
    public Long getFollower() {
        return follower;
    }

    @JsonProperty
    public Long getToFollow() {
        return toFollow;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "follower=" + follower +
                ", toFollow=" + toFollow +
                '}';
    }
}
