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
public class Sessions {
    Long uid;
    Date signInTime;
    Integer status;

    public Sessions() {
    }

    public Sessions(Long uid, Date signInTime, Integer status) {
        this.uid = uid;
        this.signInTime = signInTime;
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public Date getSignInTime() {
        return signInTime;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Sessions{" +
                "uid=" + uid +
                ", signInTime=" + signInTime +
                ", status=" + status +
                '}';
    }
}
