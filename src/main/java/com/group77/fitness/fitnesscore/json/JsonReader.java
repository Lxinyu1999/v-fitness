package com.group77.fitness.fitnesscore.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.group77.fitness.fitnesscore.vo.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class JsonReader {

    public static Object readJsonToObject(JsonDataFile jsonDataFile) throws Exception{
        String jsonStr = FileUtils.readFileToString(new File(jsonDataFile.getPath()), StandardCharsets.UTF_8);
//        return JSON.parseObject(jsonStr, jsonDataFile.object.getClass());

        switch (jsonDataFile) {
            case ACCOUNT:
                return JSON.parseObject(jsonStr, AccountJson.class);
            case GYM:
                return JSON.parseObject(jsonStr, Gym.class);
            case VIDEO:
                return JSON.parseObject(jsonStr, VideoJson.class);
            case SERIES:
                return JSON.parseObject(jsonStr, SeriesJson.class);
            case SESSION:
                return JSON.parseObject(jsonStr, SessionJson.class);
            case RECORD:
                return JSON.parseArray(jsonStr, Record.class);
            default:
                throw new Exception("The type of JSON Data File is unknown.");
        }
    }
}
