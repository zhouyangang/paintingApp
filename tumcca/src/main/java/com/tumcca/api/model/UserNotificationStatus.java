package com.tumcca.api.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public class UserNotificationStatus {
    Long uid;
    Integer status;

    public UserNotificationStatus() {
    }

    public UserNotificationStatus(Long uid, Integer status) {
        this.uid = uid;
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public Integer getStatus() {
        return status;
    }
}
