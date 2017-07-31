package com.tumcca.api.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
public class WorksAddVO {
    @NotNull
    Long category;
    @Length(max = 1024)
    String tags;
    @Length(max = 64)
    String title;
    @Length(max = 1024)
    String description;
    @Valid
    @NotNull
    List<Long> pictures;
    Long albumId;

    public WorksAddVO() {
    }

    public WorksAddVO(Long category, String tags, String title, String description, List<Long> pictures, Long albumId) {
        this.category = category;
        this.tags = tags;
        this.title = title;
        this.description = description;
        this.pictures = pictures;
        this.albumId = albumId;
    }

    @JsonProperty
    public Long getCategory() {
        return category;
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
    public List<Long> getPictures() {
        return pictures;
    }

    @JsonProperty
	public Long getAlbumId() {
		return albumId;
	}
}
