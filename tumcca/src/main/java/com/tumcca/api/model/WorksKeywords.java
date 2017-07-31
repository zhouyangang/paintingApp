package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class WorksKeywords {
    @NotBlank
    String keywords;
    @NotNull
    @Min(1)
    Integer page;
    @NotNull
    @Min(1)
    @Max(100)
    Integer size;
    @NotNull
    Integer width;

    public WorksKeywords() {
    }

    public WorksKeywords(String keywords) {
        this.keywords = keywords;
    }

    @JsonProperty
    public String getKeywords() {
        return keywords;
    }

    @JsonProperty
    public Integer getPage() {
        return page;
    }

    @JsonProperty
    public Integer getSize() {
        return size;
    }

    @JsonIgnore
    public Integer getStart() {
        return (page - 1) * size;
    }
    
    @JsonProperty
    public Integer getWidth() {
    	return width;
    }
}
