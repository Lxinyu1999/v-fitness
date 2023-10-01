package com.group77.fitness.fitnesscore.json;


public enum JsonDataFile {
    // Registered new JSON with its filename here.
    // Note: after adding a new one, remember to also add it in JsonReader
    ACCOUNT("account.json"),
    VIDEO("video.json"),
    SERIES("series.json"),
    GYM("gym.json"),
    SESSION("session.json"),
    RECORD("record.json");

    public final String filename;

    public final String dataDirPath = System.getProperty("user.dir") + "/London-Fitness/data/";

    JsonDataFile(String filename) { this.filename = filename; }

    public String getPath() {
        return this.dataDirPath + this.filename;
    }
}
