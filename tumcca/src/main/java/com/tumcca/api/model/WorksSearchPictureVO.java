package com.tumcca.api.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-30
 */
public class WorksSearchPictureVO {
	Long id;
	int width;
	int height;
	

	public WorksSearchPictureVO() {
	}
	
	public WorksSearchPictureVO(Long id, int width, int height){
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


}
