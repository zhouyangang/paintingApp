package com.lee.culture.demo.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:06.
 * @Description:
 */
@Entity
@Table(name = "comment_info", schema = "culture")
public class CommentInfoEntity {
    private Integer id;
    private UserInfoEntity user;
    private WorkInfoEntity work;
    private String comment;
    private Date commentTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Basic
    @Column(name = "comment", nullable = false, length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "comment_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentInfoEntity that = (CommentInfoEntity) o;

        if (id != that.id) return false;
        if (!user.equals(that.user)) return false;
        if (!work.equals(that.work)) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (commentTime != null ? !commentTime.equals(that.commentTime) : that.commentTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (work != null ? work.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (commentTime != null ? commentTime.hashCode() : 0);
        return result;
    }
}
