package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortReportDTO {

    private Integer id;

    private String description;

    //@JsonProperty("alternateName")
    private String displayName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
