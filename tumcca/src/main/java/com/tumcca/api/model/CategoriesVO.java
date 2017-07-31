package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class CategoriesVO {
    Long id;
    String name;
    List<CategoriesVO> children;

    public CategoriesVO() {
    }

    public CategoriesVO(Long id, String name, List<CategoriesVO> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public List<CategoriesVO> getChildren() {
        return children;
    }
}
