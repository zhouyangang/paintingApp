package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public class UserNotifications {
    Long id;
    String action;
    String message;
    Timestamp createTime;

    public UserNotifications() {
    }

    public UserNotifications(Long id, String action, String message, Timestamp createTime) {
        this.id = id;
        this.action = action;
        this.message = message;
        this.createTime = createTime;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonProperty
    public String getAction() {
        return action;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "CTT")
    public Timestamp getCreateTime() {
        return createTime;
    }
}
