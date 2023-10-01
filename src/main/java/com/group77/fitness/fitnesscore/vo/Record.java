package com.group77.fitness.fitnesscore.vo;

import java.util.ArrayList;

public class Record {
    String accountID;
    String lastWatchedVideoID;
    int lastWatchedVideoProgress;
    ArrayList<String> learningSeriesIdList;
    ArrayList<String> watchedOverVideoIdList;

    public Record() {}
    public Record(String accountID, String lastWatchedVideoID, int lastWatchedVideoProgress, ArrayList<String> learningSeriesIdList, ArrayList<String> watchedOverVideoIdList) {
        this.accountID = accountID;
        this.lastWatchedVideoID = lastWatchedVideoID;
        this.lastWatchedVideoProgress = lastWatchedVideoProgress;
        this.learningSeriesIdList = learningSeriesIdList;
        this.watchedOverVideoIdList = watchedOverVideoIdList;
    }

    public String getAccountID() { return accountID; }
    public void setAccountID(String accountID) { this.accountID = accountID; }

    public String getLastWatchedVideoID() { return lastWatchedVideoID; }
    public void setLastWatchedVideoID(String lastWatchedVideoID) { this.lastWatchedVideoID = lastWatchedVideoID; }

    public int getLastWatchedVideoProgress() { return lastWatchedVideoProgress; }
    public void setLastWatchedVideoProgress(int lastWatchedVideoProgress) { this.lastWatchedVideoProgress = lastWatchedVideoProgress; }

    public ArrayList<String> getLearningSeriesIdList() { return learningSeriesIdList; }
    public void setLearningSeriesIdList(ArrayList<String> learningSeriesIdList) { this.learningSeriesIdList = learningSeriesIdList; }

    public ArrayList<String> getWatchedOverVideoIdList() { return watchedOverVideoIdList; }
    public void setWatchedOverVideoIdList(ArrayList<String> watchedOverVideoIdList) { this.watchedOverVideoIdList = watchedOverVideoIdList; }
}
