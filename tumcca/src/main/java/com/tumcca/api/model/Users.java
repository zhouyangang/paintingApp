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
public class Users {
    Long id;
    String email;
    String mobile;
    String passwordHash;

    public Users() {
    }

    public Users(Long id, String email, String mobile, String passwordHash) {
        this.id = id;
        this.email = email;
        this.mobile = mobile;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
