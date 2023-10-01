package com.group77.fitness.fitnesscore.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.group77.fitness.fitnesscore.media.MediaDirectory;

import java.util.Calendar;

public class Account {
    @JSONField(name = "accountID", ordinal = 1)
    private String accountID;

    @JSONField(name = "password", ordinal = 2)
    private String password;

    @JSONField(name = "gender", ordinal = 3)
    private String gender;

    @JSONField(name = "birthYear", ordinal = 4)
    private int birthYear;

    @JSONField(name = "avatarFilename", ordinal = 5)
    private String avatarFilename;

    public Account() {}
    public Account(String accountID, String password, String gender, int birthYear) {
        this.accountID = accountID;
        this.password = password;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    public String getAccountID() { return accountID; }
    public void setAccountID(String accountID) { this.accountID = accountID; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    public String getAvatarFilename() { return avatarFilename; }
    public void setAvatarFilename(String avatarFilename) { this.avatarFilename = avatarFilename; }

    // 因为FastJson 在进行操作时，是根据 getter 和 setter 的方法而不是依据 Field 进行, 因此
    // 必须通过在此getter前设定serialize = false, deserialize = false才能使age不参与 序列化&反序列化
    @JSONField(serialize = false, deserialize = false)
    public int getAge() { return Calendar.getInstance().get(Calendar.YEAR) - birthYear; }
    // age不是field, 只需要在取出的时候通过birthYear进行计算得到，因此不必设置setter

    @JSONField(serialize = false, deserialize = false)
    public String getAvatarAbsolutePath() {
        return MediaDirectory.AVATAR.getPath() + this.avatarFilename;
    }
}
