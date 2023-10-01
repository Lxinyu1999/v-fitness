package com.group77.fitness.fitnesscore.json;

import com.alibaba.fastjson.JSON;
import com.group77.fitness.fitnesscore.vo.*;


public class JsonReaderTest {

    public static void main(String[] args) throws Exception {
//        AccountJson accountJson = (AccountJson) JsonReader.readJsonToObject(JsonDataFile.ACCOUNT);
//
//        System.out.println(accountJson.getManagers().get(0).getBirthYear());
//        System.out.println(accountJson.getTrainers());
//        System.out.println(accountJson.getMembers());
//
//        String jsonStr = JSON.toJSONString(accountJson);
//        System.out.println(jsonStr);
//        accountJson = JSON.parseObject(jsonStr, AccountJson.class);
//        System.out.println(accountJson.getMembers().get(1).getPhysicalAbilities());


        VideoJson videoJson = (VideoJson) JsonReader.readJsonToObject(JsonDataFile.VIDEO);
        System.out.println(videoJson.getVideoIdCounter());
        System.out.println(videoJson.getVideoList());

        System.out.println(videoJson.getVideoList().get(0).getVideoID());
        System.out.println(videoJson.getVideoList().get(0).getDisplayName());
    }
}
