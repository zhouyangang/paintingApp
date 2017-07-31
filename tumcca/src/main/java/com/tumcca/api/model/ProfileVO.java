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
public class ProfileVO {
    @Length(max = 32)
    @NotBlank
    String pseudonym;
    Integer gender;
    @Length(max = 1024)
    String introduction;
    @Length(max = 32)
    @NotBlank
    String title;
    @Length(max = 64)
    String hobbies;
    @Length(max = 64)
    String forte;
    Long avatar;
    @Length(max = 32)
    String country;
    @Length(max = 32)
    String province;
    @Length(max = 32)
    String city;

    public ProfileVO() {
    }

    public ProfileVO(String pseudonym, Integer gender, String introduction, String title, String hobbies, String forte, Long avatar, String country, String province, String city) {
        this.pseudonym = pseudonym;
        this.gender = gender;
        this.introduction = introduction;
        this.title = title;
        this.hobbies = hobbies;
        this.forte = forte;
        this.avatar = avatar;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    @JsonProperty
    public String getPseudonym() {
        return pseudonym;
    }

    @JsonProperty
    public Integer getGender() {
        return gender;
    }

    @JsonProperty
    public String getIntroduction() {
        return introduction;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getHobbies() {
        return hobbies;
    }

    @JsonProperty
    public String getForte() {
        return forte;
    }

    @JsonProperty
    public Long getAvatar() {
        return avatar;
    }

    @JsonProperty
    public String getCountry() {
        return country;
    }

    @JsonProperty
    public String getProvince() {
        return province;
    }

    @JsonProperty
    public String getCity() {
        return city;
    }
}
