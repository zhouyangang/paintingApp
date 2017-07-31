package com.tumcca.api.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author wanzhi
 * @version 1.0
 * @since 2015-07-05
 */
public class FollowCategory {
    @NotNull
    Long follower;
    @NotNull
    Long category;

    public FollowCategory() {
    }

    public FollowCategory(Long follower, Long category) {
        this.follower = follower;
        this.category = category;
    }

    @JsonProperty
    public Long getFollower() {
        return follower;
    }

    @JsonProperty
    public Long getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "FollowCategory{" +
                "follower=" + follower +
                ", category=" + category +
                '}';
    }
}
