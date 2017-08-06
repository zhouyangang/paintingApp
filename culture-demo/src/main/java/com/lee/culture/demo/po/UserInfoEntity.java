package com.lee.culture.demo.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:06.
 * @Description:
 */
@Entity
@Table(name = "user_info", schema = "culture")
public class UserInfoEntity {
    private Integer id;
    private String phone;
    private String pwd;
    private String aliasName;
    private String description;
    private Date registerTime;
    private String profilePath;

    private Set<WorkInfoEntity> workInfos;

    private Set<LikeInfoEntity> likeInfos;

    private Set<CommentInfoEntity> comments;

    @OneToMany(mappedBy = "user")
    public Set<LikeInfoEntity> getLikeInfos() {
        return likeInfos;
    }

    public void setLikeInfos(Set<LikeInfoEntity> likeInfos) {
        this.likeInfos = likeInfos;
    }

    @OneToMany(mappedBy = "user")
    public Set<CommentInfoEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentInfoEntity> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "author")
    public Set<WorkInfoEntity> getWorkInfos() {
        return workInfos;
    }

    public void setWorkInfos(Set<WorkInfoEntity> workInfos) {
        this.workInfos = workInfos;
    }

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "phone", nullable = false, length = 32)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "pwd", nullable = true, length = 32)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Basic
    @Column(name = "alias_name", nullable = true, length = 32)
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "register_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Basic
    @Column(name = "profile_path", nullable = true, length = 255)
    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfoEntity that = (UserInfoEntity) o;

        if (id != that.id) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (pwd != null ? !pwd.equals(that.pwd) : that.pwd != null) return false;
        if (aliasName != null ? !aliasName.equals(that.aliasName) : that.aliasName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (registerTime != null ? !registerTime.equals(that.registerTime) : that.registerTime != null) return false;
        if (profilePath != null ? !profilePath.equals(that.profilePath) : that.profilePath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        result = 31 * result + (aliasName != null ? aliasName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (registerTime != null ? registerTime.hashCode() : 0);
        result = 31 * result + (profilePath != null ? profilePath.hashCode() : 0);
        return result;
    }
}
