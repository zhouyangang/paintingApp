package com.tumcca.api.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-27
 */
public class AlbumAddVO {
    @NotBlank
    @Length(max = 64)
    String title;
    @Length(max = 1024)
    String description;
    
    public AlbumAddVO(){
    }
    
    public AlbumAddVO(String title, String description){
    	this.title = title;
    	this.description = description;
    }

    @JsonProperty
	public String getTitle() {
		return title;
	}

    @JsonProperty
	public String getDescription() {
		return description;
	}
    
    
}
