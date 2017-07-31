package com.tumcca.api.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class Artists {
    Long uid;
    String pseudonym;
    Integer gender;
    String introduction;
    String title;
    String hobbies;
    String forte;
    Long avatar;
    String country;
    String province;
    String city;

    public Artists() {
    }

    public Artists(Long uid, String pseudonym, Integer gender, String introduction, String title, String hobbies, String forte, Long avatar, String country, String province, String city) {
        this.uid = uid;
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

    public Long getUid() {
        return uid;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public Integer getGender() {
        return gender;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getTitle() {
        return title;
    }

    public String getHobbies() {
        return hobbies;
    }

    public String getForte() {
        return forte;
    }

    public Long getAvatar() {
        return avatar;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Artists{" +
                "uid=" + uid +
                ", pseudonym='" + pseudonym + '\'' +
                ", gender=" + gender +
                ", introduction='" + introduction + '\'' +
                ", title='" + title + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", forte='" + forte + '\'' +
                ", avatar=" + avatar +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
