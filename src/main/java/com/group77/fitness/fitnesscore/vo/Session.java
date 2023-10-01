package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class Session {
    @JSONField(name = "sessionID", ordinal = 1)
    private String sessionID;

    @JSONField(name = "memberID", ordinal = 2)
    private String memberID;

    @JSONField(name = "trainerID", ordinal = 3)
    private String trainerID;

    @JSONField(name = "sessionDate", ordinal = 4)
    private String sessionDate;

    @JSONField(name = "sessionInterval", ordinal = 5)
    private String[] sessionInterval;

    @JSONField(name = "sessionSubject", ordinal = 6)
    private String sessionSubject;

    @JSONField(serialize = false, deserialize = false)
    private String sessionIntervalString;

    public Session() {}
    public Session(String memberID, String trainerID, String sessionDate, String[] sessionInterval, String sessionSubject) {
        this.memberID = memberID;
        this.trainerID = trainerID;
        this.sessionDate = sessionDate;
        this.sessionInterval = sessionInterval;
        this.sessionSubject = sessionSubject;
    }

    public String getSessionID() { return sessionID; }
    public void setSessionID(String sessionID) { this.sessionID = sessionID; }

    public String getMemberID() { return memberID; }
    public void setMemberID(String memberID) { this.memberID = memberID; }

    public String getTrainerID() { return trainerID; }
    public void setTrainerID(String trainerID) { this.trainerID = trainerID; }

    public String getSessionDate() { return sessionDate; }
    public void setSessionDate(String sessionDate) { this.sessionDate = sessionDate; }

    public String[] getSessionInterval() { return sessionInterval; }
    public void setSessionInterval(String[] sessionInterval) { this.sessionInterval = sessionInterval; }

    public String getSessionSubject() { return sessionSubject; }
    public void setSessionSubject(String sessionSubject) { this.sessionSubject = sessionSubject; }

    // This getter is used by "schedule" to display the session interval in the TableView.
    public String getSessionIntervalString() {
        return this.sessionInterval[0] + " ~ " + this.sessionInterval[1];
    }
}
