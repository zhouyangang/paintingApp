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
 * @since 2015-06-06
 */
public class AndroidLogsVO {
    String description;
    Timestamp ts;

    public AndroidLogsVO() {
    }

    public AndroidLogsVO(String description, Timestamp ts) {
        this.description = description;
        this.ts = ts;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="CTT")
    public Timestamp getTs() {
        return ts;
    }
}
