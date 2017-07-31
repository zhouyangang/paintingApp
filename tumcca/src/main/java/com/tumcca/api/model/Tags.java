package com.tumcca.api.model;

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
public class Tags {
    Long id;
    String name;

    public Tags() {
    }

    public Tags(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
