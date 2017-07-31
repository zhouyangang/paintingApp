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
public class CategoriesUpdateInput {
    Long id;
    @NotBlank
    String name;

    public CategoriesUpdateInput() {
    }

    public CategoriesUpdateInput(Long id, String name) {
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
