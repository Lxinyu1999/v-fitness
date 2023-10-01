package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class Series {
    @JSONField(name = "seriesID", ordinal = 1)
    private String seriesID;

    @JSONField(name = "seriesName", ordinal = 2)
    private String seriesName;

    public Series() {}

    // 此带参构造方法不需要传入seriesID, 它们将会在新增Series时自动补全
    public Series (String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesID() { return seriesID; }
    public void setSeriesID(String seriesID) { this.seriesID = seriesID; }

    public String getSeriesName() { return seriesName; }
    public void setSeriesName(String seriesName) { this.seriesName = seriesName; }
}
