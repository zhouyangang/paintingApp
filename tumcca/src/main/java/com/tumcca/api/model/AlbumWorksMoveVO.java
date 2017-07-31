package com.tumcca.api.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-28
 */
public class AlbumWorksMoveVO {

	@Valid
    @NotNull
	List<Long> workses;
	
	public AlbumWorksMoveVO(){
		
	}

    public AlbumWorksMoveVO(List<Long> workses) {
		this.workses = workses;
	}

    @JsonProperty
	public List<Long> getWorkses() {
		return workses;
	}
}
