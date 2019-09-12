package com.lessons.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportsStatsDTO {
    Integer report;
    String displayName;
    @JsonProperty("num_indicators")
    Integer indicatorCount;

    public Integer getReport() {
        return report;
    }

    public void setReport(Integer report) {
        this.report = report;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getIndicatorCount() {
        return indicatorCount;
    }

    public void setIndicatorCount(Integer indicatorCount) {
        this.indicatorCount = indicatorCount;
    }
}
