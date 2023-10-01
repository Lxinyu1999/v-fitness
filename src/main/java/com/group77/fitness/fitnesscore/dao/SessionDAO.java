package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface SessionDAO {
    boolean addSession(Session session) throws IOException;
    boolean deleteSession(String sessionID) throws IOException;
    ArrayList<Session> getSessionsByMemberID(String memberID);
    ArrayList<Session> getSessionsByTrainerID(String trainerID);
    ArrayList<Session> getSessionsByDate(int year, int month, int day);
    ArrayList<Session> getSessionsByDateAndTrainerID(int year, int month, int day, String trainerID);
    ArrayList<Session> getSessionsByDateAndMemberID(int year, int month, int day, String memberID);

    Set<String> getStudentIDsByTrainerID(String trainerID);
}
