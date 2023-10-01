package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.group77.fitness.fitnesscore.media.MediaDirectory;


public class Video {
    @JSONField(name = "videoID", ordinal = 1)
    private String videoID;

    @JSONField(name = "displayName", ordinal = 2)
    private String displayName;

    @JSONField(name = "category", ordinal = 3)
    private String category;

    @JSONField(name = "seriesID", ordinal = 4)
    private String seriesID;

    @JSONField(name = "level", ordinal = 5)
    private String level;

    @JSONField(name = "lowestMembershipRequirement", ordinal = 6)
    private String lowestMembershipRequirement;

    @JSONField(name = "uploadTime", ordinal = 7)
    private String uploadTime;

    @JSONField(name = "coverFilename", ordinal = 8)
    private String coverFilename;

    @JSONField(name = "videoFilename", ordinal = 9)
    private String videoFilename;

    // 需要有一个无参构造方法, 看似never used,
    // 但若缺少此无参构造方法将会导致FastJson无法将videoID和uploadTime正确反序列化(导致其值为null)
    public Video() {}

    // 此带参构造方法不需要传入videoID和uploadTime, 它们将会在新增视频时自动补全
    public Video (String displayName, String category, String seriesID, String level, String lowestMembershipRequirement) {
        this.displayName = displayName;
        this.category = category;
        this.seriesID = seriesID;
        this.level = level;
        this.lowestMembershipRequirement = lowestMembershipRequirement;
    }

    public String getVideoID() { return videoID; }
    public void setVideoID(String videoID) { this.videoID = videoID; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSeriesID() { return seriesID; }
    public void setSeriesID(String seriesID) { this.seriesID = seriesID; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getLowestMembershipRequirement() { return lowestMembershipRequirement; }
    public void setLowestMembershipRequirement(String lowestMembershipRequirement) { this.lowestMembershipRequirement = lowestMembershipRequirement; }

    public String getUploadTime() { return uploadTime; }
    public void setUploadTime(String uploadTime) { this.uploadTime = uploadTime; }

    public String getCoverFilename() { return coverFilename; }
    public void setCoverFilename(String coverFilename) { this.coverFilename = coverFilename; }

    public String getVideoFilename() { return videoFilename; }
    public void setVideoFilename(String videoFilename) { this.videoFilename = videoFilename; }

    // Get the path in the data directory, only used when loading these resources the to GUI
    @JSONField(serialize = false, deserialize = false)
    public String getCoverAbsolutePath() {
        return MediaDirectory.COVER.getPath() + this.coverFilename;
    }

    @JSONField(serialize = false, deserialize = false)
    public String getVideoAbsolutePath() {
        String videoPath = MediaDirectory.VIDEO.getPath() + this.videoFilename;

        // It is vital to make sure all the path separators are "/"
        // when creating javafx.scene.media.Media object: new Media(url)
        return videoPath.replace("\\", "/");    // replace "\" with "/"
    }
}
