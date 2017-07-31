package com.tumcca.api.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class WorksSearchVO {
    Long id;
    
    Long category;
    
    String tags;
   
    String title;
    
    String description;
    
    Long author;
    
    WorksSearchPictureVO picture;
    
    Timestamp createTime;

    public WorksSearchVO() {
    }

    public WorksSearchVO(Long id, Long category, String tags, String title, String description, Long author, WorksSearchPictureVO picture, Timestamp createTime) {
        this.id = id;
        this.category = category;
        this.tags = tags;
        this.title = title;
        this.description = description;
        this.author = author;
        this.picture = picture;
        this.createTime = createTime;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public Long getCategory() {
        return category;
    }

    @JsonIgnore
    public String getTags() {
        return tags;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public Long getAuthor() {
        return author;
    }

    @JsonProperty
    public WorksSearchPictureVO getPicture() {
        return picture;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="CTT")
    public Timestamp getCreateTime() {
        return createTime;
    }
}
