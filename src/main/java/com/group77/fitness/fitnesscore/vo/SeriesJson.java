package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class SeriesJson {
    @JSONField(name = "seriesIdCounter", ordinal = 1)
    private int seriesIdCounter;

    @JSONField(name = "seriesList", ordinal = 2)
    private ArrayList<Series> seriesList;

    public int getSeriesIdCounter() { return seriesIdCounter; }
    public void setSeriesIdCounter(int seriesIdCounter) { this.seriesIdCounter = seriesIdCounter; }

    public ArrayList<Series> getSeriesList() { return seriesList; }
    public void setSeriesList(ArrayList<Series> seriesList) { this.seriesList = seriesList; }
}
