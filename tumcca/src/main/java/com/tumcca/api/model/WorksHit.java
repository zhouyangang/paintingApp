package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-17
 */
public class WorksHit {
    String id;
    WorksES works;

    public WorksHit() {
    }

    public WorksHit(String id, WorksES works) {
        this.id = id;
        this.works = works;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public WorksES getWorks() {
        return works;
    }
}
