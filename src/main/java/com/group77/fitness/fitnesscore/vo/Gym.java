package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Gym {
    @JSONField(name = "gymName", ordinal = 1)
    private String gymName;

    @JSONField(name = "address", ordinal = 2)
    private String address;

    @JSONField(name = "telephone", ordinal = 3)
    private String telephone;

    @JSONField(name = "email", ordinal = 4)
    private String email;

    @JSONField(name = "openingHours", ordinal = 5)
    private ArrayList<String> openingHours;

    @JSONField(name = "trainerWorkingHours", ordinal = 6)
    private ArrayList<String> trainerWorkingHours;

    @JSONField(name = "sportsCategories", ordinal = 7)
    private ArrayList<String> sportsCategories;

    @JSONField(name = "membershipTypes", ordinal = 8)
    private ArrayList<String> membershipTypes;

    @JSONField(name = "courseLevels", ordinal = 9)
    private ArrayList<String> courseLevels;

    public String getGymName() { return gymName; }
    public void setGymName(String gymName) { this.gymName = gymName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public ArrayList<String> getOpeningHours() { return openingHours; }
    public void setOpeningHours(ArrayList<String> openingHours) { this.openingHours = openingHours; }

    public ArrayList<String> getTrainerWorkingHours() { return trainerWorkingHours; }
    public void setTrainerWorkingHours(ArrayList<String> trainerWorkingHours) { this.trainerWorkingHours = trainerWorkingHours; }

    public ArrayList<String> getSportsCategories() { return sportsCategories; }
    public void setSportsCategories(ArrayList<String> sportsCategories) { this.sportsCategories = sportsCategories; }

    public ArrayList<String> getMembershipTypes() { return membershipTypes; }
    public void setMembershipTypes(ArrayList<String> membershipTypes) { this.membershipTypes = membershipTypes; }

    public ArrayList<String> getCourseLevels() { return courseLevels; }
    public void setCourseLevels(ArrayList<String> courseLevels) { this.courseLevels = courseLevels; }
}
