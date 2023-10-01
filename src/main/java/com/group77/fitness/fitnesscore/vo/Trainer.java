package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Trainer extends Account{
    @JSONField(name = "teachingSubjects", ordinal = 6)
    private ArrayList<String> teachingSubjects;

    public Trainer() {}

    public Trainer(String accountID, String password, String gender, int birthYear,
                   ArrayList<String> teachingSubjects) {
        super(accountID, password, gender, birthYear);
        this.teachingSubjects = teachingSubjects;
    }

    public ArrayList<String> getTeachingSubjects() { return teachingSubjects; }
    public void setTeachingSubjects(ArrayList<String> teachingSubjects) { this.teachingSubjects = teachingSubjects; }

    // This getter is used to display the teaching subjects in the TableView.
    @JSONField(serialize = false, deserialize = false)
    public String getTeachingSubjectsString() {
        return String.join(", ", this.teachingSubjects);
    }
}
