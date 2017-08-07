package com.lee.culture.demo.po;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:06.
 * @Description:
 */
@Entity
@Table(name = "like_info", schema = "culture")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LikeInfoEntity {
    private Integer id;
    private UserInfoEntity user;
    private WorkInfoEntity work;
    private Date createTime;
    private int type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    public UserInfoEntity getUser() {
        return user;
    }

    public void setUser(UserInfoEntity user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    public WorkInfoEntity getWork() {
        return work;
    }

    public void setWork(WorkInfoEntity work) {
        this.work = work;
    }

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LikeInfoEntity that = (LikeInfoEntity) o;

        if (id != that.id) return false;
        if (!user.equals(that.user)) return false;
        if (!work.equals(that.work)) return false;
        if (type != that.type) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (work != null ? work.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }
}
