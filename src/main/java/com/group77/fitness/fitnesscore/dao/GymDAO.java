package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Gym;

import java.io.IOException;

public interface GymDAO {
    boolean updateGym(Gym gym) throws IOException;
    Gym getGym();
}
