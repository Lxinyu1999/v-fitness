package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class Member extends Account{
    @JSONField(name = "height", ordinal = 6)
    private int height;

    @JSONField(name = "weight", ordinal = 7)
    private int weight;

    @JSONField(name = "membershipType", ordinal = 8)
    private String membershipType;

    @JSONField(name = "targets", ordinal = 9)
    private String targets;

    @JSONField(name = "physicalAbilities", ordinal = 10)
    private String physicalAbilities;

    @JSONField(name = "lastLogInDate", ordinal = 11)
    private String lastLogInDate;

    @JSONField(name = "inviteeCnt", ordinal = 12)
    private int inviteeCnt;

    public Member() {}

    public Member(String accountID, String password, String gender, int birthYear,
                  int height, int weight, String membershipType, String targets, String physicalAbilities) {
        super(accountID, password, gender, birthYear);
        this.height = height;
        this.weight = weight;
        this.membershipType = membershipType;
        this.targets = targets;
        this.physicalAbilities = physicalAbilities;
        this.lastLogInDate = LocalDate.now().toString();
        this.inviteeCnt = 0;
    }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public String getTargets() { return targets; }
    public void setTargets(String targets) { this.targets = targets; }

    public String getPhysicalAbilities() { return physicalAbilities; }
    public void setPhysicalAbilities(String physicalAbilities) { this.physicalAbilities = physicalAbilities; }

    public String getLastLogInDate() { return lastLogInDate; }
    public void setLastLogInDate(String lastLogInDate) { this.lastLogInDate = lastLogInDate; }

    public int getInviteeCnt() { return inviteeCnt; }
    public void setInviteeCnt(int inviteeCnt) { this.inviteeCnt = inviteeCnt; }

    @JSONField(serialize = false, deserialize = false)
    public int getInactiveDays() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate curDate = LocalDate.now();
        LocalDate lastLogInDate = simpleDateFormat.parse(this.getLastLogInDate())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) (curDate.toEpochDay() - lastLogInDate.toEpochDay());
    }
}
