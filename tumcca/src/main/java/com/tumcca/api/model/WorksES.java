package com.tumcca.api.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-17
 */
public class WorksES {
    String tags;
    String title;
    String description;
    //Last update date
    Date updateDate;
    Long authorId;
	String author;
    Long likes;
    Long collects;
    Long category;
	Integer status;
    Long albumId;

	public WorksES() {
    }

    public WorksES(String tags, String title, String description
    		, Date updateDate, Long authorId, String author, Long likes, Long collects, Long category, Integer status, Long albumId) {
        this.tags = tags;
        this.title = title;
        this.description = description;
        this.updateDate = updateDate;
        this.authorId = authorId;
        this.author = author;
        this.likes = likes;
        this.collects = collects;
        this.category = category;
        this.status = status;
        this.albumId = albumId;
    }

    @JsonProperty
    public String getTags() {
        return tags;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone="CTT")
    public Date getUpdateDate() {
		return updateDate;
	}
    
    @JsonProperty
    public String getAuthor() {
		return author;
	}

    @JsonProperty
	public Long getLikes() {
		return likes;
	}

    @JsonProperty
	public Long getCollects() {
		return collects;
	}

    @JsonProperty
	public Long getCategory() {
		return category;
	}
    
    @JsonProperty
    public Integer getStatus() {
		return status;
	}

    @JsonProperty
	public Long getAlbumId() {
		return albumId;
	}

    @JsonProperty
	public Long getAuthorId() {
		return authorId;
	}
}
