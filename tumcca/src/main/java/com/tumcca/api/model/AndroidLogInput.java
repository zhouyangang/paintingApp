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
 * @since 2015-06-06
 */
public class AndroidLogInput {
    @NotBlank
    String description;

    public AndroidLogInput() {
    }

    public AndroidLogInput(String description) {
        this.description = description;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
