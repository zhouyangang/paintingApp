package com.lee.culture.demo.po;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:06.
 * @Description:
 */
@Entity
@Table(name = "work_info", schema = "culture")
public class WorkInfoEntity {
    private Integer id;
    private String workName;
    private Timestamp uploadTime;
    private String workPath;
    private UserInfoEntity author;
    private String workDescription;

    private Set<LikeInfoEntity> likeInfos;

    private Set<CommentInfoEntity> comments;

    @OneToMany(mappedBy = "work")
    public Set<LikeInfoEntity> getLikeInfos() {
        return likeInfos;
    }

    public void setLikeInfos(Set<LikeInfoEntity> likeInfos) {
        this.likeInfos = likeInfos;
    }

    @OneToMany(mappedBy = "work")
    public Set<CommentInfoEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentInfoEntity> comments) {
        this.comments = comments;
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
    @Column(name = "work_name", nullable = false, length = 100)
    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    @Basic
    @Column(name = "upload_time", nullable = true)
    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Basic
    @Column(name = "work_path", nullable = true, length = 255)
    public String getWorkPath() {
        return workPath;
    }

    public void setWorkPath(String workPath) {
        this.workPath = workPath;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    public UserInfoEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserInfoEntity author) {
        this.author = author;
    }

    @Basic
    @Column(name = "work_description", nullable = true, length = 100)
    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkInfoEntity that = (WorkInfoEntity) o;

        if (id != that.id) return false;
        if (workName != null ? !workName.equals(that.workName) : that.workName != null) return false;
        if (uploadTime != null ? !uploadTime.equals(that.uploadTime) : that.uploadTime != null) return false;
        if (workPath != null ? !workPath.equals(that.workPath) : that.workPath != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (workDescription != null ? !workDescription.equals(that.workDescription) : that.workDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (workName != null ? workName.hashCode() : 0);
        result = 31 * result + (uploadTime != null ? uploadTime.hashCode() : 0);
        result = 31 * result + (workPath != null ? workPath.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (workDescription != null ? workDescription.hashCode() : 0);
        return result;
    }
}
