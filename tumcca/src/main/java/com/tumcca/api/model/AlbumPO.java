package com.tumcca.api.model;

import java.sql.Timestamp;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-27
 */
public class AlbumPO {
    Long id;
    Long author;
    String title;
    String description;
    Timestamp createTime;
    
    public AlbumPO(){
    	
    }
    
	public AlbumPO(Long id, Long author, String title, String description,
			Timestamp createTime) {
		super();
		this.id = id;
		this.author = author;
		this.title = title;
		this.description = description;
		this.createTime = createTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAuthor() {
		return author;
	}
	public void setAuthor(Long author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
