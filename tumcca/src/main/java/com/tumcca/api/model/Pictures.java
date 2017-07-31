package com.tumcca.api.model;

import java.util.Date;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class Pictures {
    Long id;
    String name;
    String bucketName;
    String ossKey;
    Date createTime;
    Integer status;
	Integer width;
    Integer height;

    public Pictures() {
    }

    public Pictures(Long id, String name, String bucketName, String ossKey, Date createTime, Integer status, Integer width, Integer height) {
        this.id = id;
        this.name = name;
        this.bucketName = bucketName;
        this.ossKey = ossKey;
        this.createTime = createTime;
        this.status = status;
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getOssKey() {
        return ossKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

    @Override
    public String toString() {
        return "Pictures{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", ossKey='" + ossKey + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", width=" + width + 
                ", height=" + height +
                '}';
    }
}
