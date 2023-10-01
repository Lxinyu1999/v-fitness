package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class SessionJson {
    @JSONField(name = "sessionIdCounter", ordinal = 1)
    private int sessionIdCounter;

    @JSONField(name = "sessions", ordinal = 2)
    private ArrayList<Session> sessions;

    public int getSessionIdCounter() { return sessionIdCounter; }
    public void setSessionIdCounter(int sessionIdCounter) { this.sessionIdCounter = sessionIdCounter; }

    public ArrayList<Session> getSessions() { return sessions; }
    public void setSessions(ArrayList<Session> sessions) { this.sessions = sessions; }
}
