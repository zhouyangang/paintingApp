package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Categories {
    Long id;
    String path;
    String name;

    public Categories() {
    }

    public Categories(Long id, String path, String name) {
        this.id = id;
        this.path = path;
        this.name = name;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
