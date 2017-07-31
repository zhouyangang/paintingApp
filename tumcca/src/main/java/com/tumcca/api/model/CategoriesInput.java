package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class CategoriesInput {
    Long parentId;
    @NotBlank
    String name;

    public CategoriesInput() {
    }

    public CategoriesInput(Long parentId, String name) {
        this.parentId = parentId;
        this.name = name;
    }

    @JsonProperty
    public Long getParentId() {
        return parentId;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
