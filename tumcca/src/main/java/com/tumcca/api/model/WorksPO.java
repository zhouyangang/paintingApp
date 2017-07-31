package com.tumcca.api.model;

import java.sql.Timestamp;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class WorksPO {
    Long id;
    Long category;
    Long author;
    Integer status;
    //add by Neil 6/28/2015
    Long albumId;
	String title;
    String description;
    String tags;
    //end
	Timestamp createTime;
	
	WorksSearchPictureVO picture;

	public WorksPO() {
    }

    public WorksPO(Long id, Long category, Long author, Integer status, Timestamp createTime, Long albumId
    		,String title, String description, String tags) {
        this.id = id;
        this.category = category;
        this.author = author;
        this.status = status;
        this.createTime = createTime;
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public Long getCategory() {
        return category;
    }

    public Long getAuthor() {
        return author;
    }

    public Integer getStatus() {
        return status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public Long getAlbumId() {
		return albumId;
	}
    
    public String getTitle() {
		return title;
	}
    
	public String getDescription() {
		return description;
	}

	public String getTags() {
		return tags;
	}

    public WorksSearchPictureVO getPicture() {
		return picture;
	}
    
    public void setPicture(WorksSearchPictureVO picture){
    	this.picture = picture;
    }
}
