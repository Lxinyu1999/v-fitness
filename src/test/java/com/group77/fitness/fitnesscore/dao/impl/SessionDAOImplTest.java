package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.vo.Session;

import java.util.ArrayList;

public class SessionDAOImplTest {

    public static void main(String[] args) throws Exception {
        SessionDAOImpl sessionDAO = new SessionDAOImpl();

        ArrayList<Session> resSessions = sessionDAO.getSessionsByDate(2021, 4, 20);

        System.out.println(resSessions.size());
    }
}
