package com.lee.culture.demo.bean.request;

/**
 * @Author: joe
 * @Date: 17-8-6 下午8:10.
 * @Description:
 */
public class UserProfileDto {

    private Integer gender;

    private String alias;

    private String description;

    private String profileUrl;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
