package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.SessionDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Session;
import com.group77.fitness.fitnesscore.vo.SessionJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SessionDAOImpl implements SessionDAO {
    private SessionJson sessionJson;


    public SessionDAOImpl() throws Exception {
        this.sessionJson = (SessionJson) JsonReader.readJsonToObject(JsonDataFile.SESSION);
    }

    private void saveSessionJson() throws IOException {
        JsonWriter.writeObjectToJson(JsonDataFile.SESSION, this.sessionJson);
    }

    @Override
    public boolean addSession(Session session) throws IOException {
        // Generate the sessionID
        this.sessionJson.setSessionIdCounter(this.sessionJson.getSessionIdCounter() + 1);
        session.setSessionID("LS" + this.sessionJson.getSessionIdCounter());

        this.sessionJson.getSessions().add(session);
        this.saveSessionJson();
        return true;
    }

    @Override
    public boolean deleteSession(String sessionID) throws IOException {
        Iterator<Session> iterator = this.sessionJson.getSessions().iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session.getSessionID().equals(sessionID)) {
                iterator.remove();
                this.saveSessionJson();
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Session> getSessionsByMemberID(String memberID) {
        ArrayList<Session> resSessions = new ArrayList<>();
        for (Session session : this.sessionJson.getSessions()) {
            if (session.getMemberID().equals(memberID))
                resSessions.add(session);
        }

        return resSessions;
    }

    @Override
    public ArrayList<Session> getSessionsByTrainerID(String trainerID) {
        ArrayList<Session> resSessions = new ArrayList<>();
        for (Session session : this.sessionJson.getSessions()) {
            if (session.getTrainerID().equals(trainerID))
                resSessions.add(session);
        }

        return resSessions;
    }

    @Override
    public ArrayList<Session> getSessionsByDate(int year, int month, int day) {
        String date = String.format("%d-%02d-%02d", year, month, day);
        ArrayList<Session> resSessions = new ArrayList<>();
        for (Session session : this.sessionJson.getSessions()) {
            if (session.getSessionDate().equals(date))
                resSessions.add(session);
        }

        return resSessions;
    }

    @Override
    public ArrayList<Session> getSessionsByDateAndTrainerID(int year, int month, int day, String trainerID) {
        // Another way by utilizing the existing getSessionsByDate() but with lower efficiency
//        ArrayList<Session> resSessions = this.getSessionsByDate(year, month, day);
//        resSessions.removeIf(session -> !session.getTrainerID().equals(trainerID));
//        return resSessions;

        String date = String.format("%d-%02d-%02d", year, month, day);
        ArrayList<Session> resSessions = new ArrayList<>();
        for (Session session : this.sessionJson.getSessions()) {
            if (session.getSessionDate().equals(date) && session.getTrainerID().equals(trainerID))
                resSessions.add(session);
        }

        return resSessions;
    }

    @Override
    public ArrayList<Session> getSessionsByDateAndMemberID(int year, int month, int day, String memberID) {
//        ArrayList<Session> resSessions = this.getSessionsByDate(year, month, day);
//        resSessions.removeIf(session -> !session.getMemberID().equals(memberID));
//        return resSessions;

        String date = String.format("%d-%02d-%02d", year, month, day);
        ArrayList<Session> resSessions = new ArrayList<>();
        for (Session session : this.sessionJson.getSessions()) {
            if (session.getSessionDate().equals(date) && session.getMemberID().equals(memberID))
                resSessions.add(session);
        }

        return resSessions;
    }

    @Override
    public Set<String> getStudentIDsByTrainerID(String trainerID) {
        Set<String> studentIDs = new HashSet<>();

        for (Session session : this.getSessionsByTrainerID(trainerID)) {
            studentIDs.add(session.getMemberID());
        }

        return studentIDs;
    }
}
