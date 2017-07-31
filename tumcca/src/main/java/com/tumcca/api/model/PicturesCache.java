package com.tumcca.api.model;

import java.util.Date;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-12
 */
public class PicturesCache {
    String uri;
    String storageName;
    Date createTime;

    public PicturesCache() {
    }

    public PicturesCache(String uri, String storageName, Date createTime) {
        this.uri = uri;
        this.storageName = storageName;
        this.createTime = createTime;
    }

    public String getUri() {
        return uri;
    }

    public String getStorageName() {
        return storageName;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
