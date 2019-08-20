package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class IndicatorDTO {

//    @JsonProperty("total")
//    private Integer count;
//
//    public Integer getCount() {
//        return count;
//    }
//
//    public void setCount(Integer count) {
//        this.count = count;
//    }

    private int id;
    private String value;

    @JsonProperty("createdDate")
    private Timestamp createdDate;
    //private String createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
}
