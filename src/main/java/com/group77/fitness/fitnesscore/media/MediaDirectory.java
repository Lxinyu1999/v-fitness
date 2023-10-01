package com.group77.fitness.fitnesscore.media;

public enum MediaDirectory {
    AVATAR("avatars"),
    COVER("covers"),
    VIDEO("videos");

    public final String directoryName;
    public final String mediaDirPath = System.getProperty("user.dir") + "/London-Fitness/media/";

    MediaDirectory(String directoryName) { this.directoryName = directoryName; }

    public String getPath() { return this.mediaDirPath + this.directoryName + '/'; }
}
