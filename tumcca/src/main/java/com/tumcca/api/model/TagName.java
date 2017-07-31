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
public class TagName {
    @NotBlank
    String name;

    public TagName() {
    }

    public TagName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}
