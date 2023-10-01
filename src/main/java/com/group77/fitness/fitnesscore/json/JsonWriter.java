package com.group77.fitness.fitnesscore.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.group77.fitness.fitnesscore.vo.Record;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JsonWriter {
    public static void writeObjectToJson(JsonDataFile jsonDataFile, Object object) throws IOException {
        String jsonStr = JSON.toJSONString(object);
        FileUtils.writeStringToFile(new File(jsonDataFile.getPath()), jsonStr, StandardCharsets.UTF_8);
    }
}
