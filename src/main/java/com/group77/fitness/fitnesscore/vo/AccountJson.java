package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

// 对应数据文件account.json, 作为读写此json文件的中间类
public class AccountJson {
    @JSONField(name = "managers", ordinal = 1)
    private ArrayList<Manager> managers;

    @JSONField(name = "trainers", ordinal = 2)
    private ArrayList<Trainer> trainers;

    @JSONField(name = "members", ordinal = 3)
    private ArrayList<Member> members;

    // FastJson 在进行操作时，是根据 getter 和 setter 的方法进行的，并不是依据 Field 进行
    // 对于FastJson, 若属性是私有的，必须有 set 方法。否则无法反序列化

    public ArrayList<Manager> getManagers() { return managers; }
    public void setManagers(ArrayList<Manager> managers) { this.managers = managers; }

    public ArrayList<Trainer> getTrainers() { return trainers; }
    public void setTrainers(ArrayList<Trainer> trainers) { this.trainers = trainers; }

    public ArrayList<Member> getMembers() { return members; }
    public void setMembers(ArrayList<Member> members) { this.members = members; }
}
