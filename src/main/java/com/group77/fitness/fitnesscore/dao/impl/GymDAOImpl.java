package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.vo.Gym;

import java.io.IOException;

public class GymDAOImpl implements GymDAO {
    private Gym gym;

    public GymDAOImpl () throws Exception {  // Exception from JsonReader.readJsonToObject()
        this.gym = (Gym) JsonReader.readJsonToObject(JsonDataFile.GYM);
    }

    private void saveGym() throws IOException {  // IOException from JsonWriter.writeObjectToJson()
        JsonWriter.writeObjectToJson(JsonDataFile.GYM, this.gym);
    }

    @Override
    public boolean updateGym(Gym gym) throws IOException { // IOException from this.saveGym()
        this.saveGym();
        return true;
    }

    @Override
    public Gym getGym() {
        return this.gym;
    }
}
