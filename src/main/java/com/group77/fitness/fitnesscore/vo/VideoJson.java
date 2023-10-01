package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

// 对应数据文件video.json, 作为读写此json文件的中间类
public class VideoJson {
    @JSONField(name = "videoIdCounter", ordinal = 1)
    private int videoIdCounter;

    @JSONField(name = "videoList", ordinal = 2)
    private ArrayList<Video> videoList;

    public int getVideoIdCounter() { return videoIdCounter; }
    public void setVideoIdCounter(int videoIdCounter) { this.videoIdCounter = videoIdCounter; }

    public ArrayList<Video> getVideoList() { return videoList; }
    public void setVideoList(ArrayList<Video> videoList) { this.videoList = videoList; }
}
