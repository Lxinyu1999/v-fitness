package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.media.MediaDirectory;
import com.group77.fitness.fitnesscore.media.MediaRemover;
import com.group77.fitness.fitnesscore.media.MediaWriter;
import com.group77.fitness.fitnesscore.vo.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;

public class AccountDAOImpl implements AccountDAO {
    private final AccountJson accountJson;

    public AccountDAOImpl() throws Exception {
        // Read the json file
        this.accountJson = (AccountJson) JsonReader.readJsonToObject(JsonDataFile.ACCOUNT);
    }

    // Write to the json file after modification
    private void saveAccountJson() throws IOException {
        JsonWriter.writeObjectToJson(JsonDataFile.ACCOUNT, this.accountJson);
    }

    @Override
    public boolean addAccount(Account account, String avatarFilePath) throws Exception {
        this.uploadAccountAvatar(avatarFilePath, account);

        if (account instanceof Manager) {
            Manager manager = (Manager) account;
            this.accountJson.getManagers().add(manager);
        }
        else if (account instanceof Trainer) {
            Trainer trainer = (Trainer) account;
            this.accountJson.getTrainers().add(trainer);
        }
        else if (account instanceof Member) {
            Member member = (Member) account;
            this.accountJson.getMembers().add(member);
        }
        else
            throw new Exception("Unspecific account type. Note that DO NOT use class Account directly.");

        this.saveAccountJson();
        return true;
    }

    @Override
    public boolean deleteAccountByID(String accountID) throws IOException {
        // A concise way by using Collection.removeIf(), but won't return right away if hit
//        this.accountJson.getManagers().removeIf(Manager -> Manager.getAccountID().equals(accountID));
//        this.accountJson.getTrainers().removeIf(Trainer -> Trainer.getAccountID().equals(accountID));
//        this.accountJson.getMembers().removeIf(Member -> Member.getAccountID().equals(accountID));
//        this.saveAccountJson();

        class SaveChanges {
            //
            public SaveChanges(AccountDAOImpl accountDAO) throws IOException {
                if (!accountDAO.deleteAccountAvatar(accountID)) {
                    System.err.println("Delete account avatar failed.");
                }
                accountDAO.saveAccountJson();
            }
        }

        Iterator<Manager> managerIterator = this.accountJson.getManagers().iterator();
        while (managerIterator.hasNext()) {
            Manager manager = managerIterator.next();
            if (manager.getAccountID().equals(accountID)) {
                managerIterator.remove();
                new SaveChanges(this);
                return true;
            }
        }

        Iterator<Trainer> trainerIterator = this.accountJson.getTrainers().iterator();
        while (trainerIterator.hasNext()) {
            Trainer trainer = trainerIterator.next();
            if (trainer.getAccountID().equals(accountID)) {
                trainerIterator.remove();
                new SaveChanges(this);
                return true;
            }
        }

        Iterator<Member> memberIterator = this.accountJson.getMembers().iterator();
        while (memberIterator.hasNext()) {
            Member member = memberIterator.next();
            if (member.getAccountID().equals(accountID)) {
                memberIterator.remove();
                new SaveChanges(this);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updateAccount(Account account) throws Exception {
        String accountID = account.getAccountID();

        ArrayList<Manager> managers = this.accountJson.getManagers();
        for (int i = 0; i < managers.size(); i++) {
            if (managers.get(i).getAccountID().equals(accountID)) {
                if (account instanceof Manager) {   // 确保向下转型成功进行
                    managers.set(i, (Manager) account);
                    this.saveAccountJson();
                    return true;
                }
                else {
                    throw new Exception("Incorrect account type. Use a Manager object instead.");
                }
            }
        }

        ArrayList<Trainer> trainers = this.accountJson.getTrainers();
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getAccountID().equals(accountID)) {
                if (account instanceof Trainer) {
                    trainers.set(i, (Trainer) account);
                    this.saveAccountJson();
                    return true;
                }
                else {
                    throw new Exception("Incorrect account type. Use a Trainer object instead.");
                }
            }
        }

        ArrayList<Member> members = this.accountJson.getMembers();
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getAccountID().equals(accountID)) {
                if (account instanceof Member) {
                    members.set(i, (Member) account);
                    this.saveAccountJson();
                    return true;
                }
                else {
                    throw new Exception("Incorrect account type. Use a Member object instead.");
                }
            }
        }

        return false;
    }

    @Override
    public boolean updateAccount(Account account, String avatarFilePath) throws Exception {
        this.deleteAccountAvatar(account.getAccountID());
        this.uploadAccountAvatar(avatarFilePath, account);
        return this.updateAccount(account);
    }

    @Override
    public Account queryAccountByID(String accountID) {
        for (Manager manager : this.accountJson.getManagers()) {
            if (manager.getAccountID().equals(accountID)) {
                return manager;
            }
        }

        for (Trainer trainer : this.accountJson.getTrainers()) {
            if (trainer.getAccountID().equals(accountID)) {
                return trainer;
            }
        }

        for (Member member : this.accountJson.getMembers()) {
            if (member.getAccountID().equals(accountID)) {
                return member;
            }
        }

        return null;    // return null if there is no matched account
    }

    @Override
    public ArrayList<String> getAllAccountID() {
        ArrayList<String> allAccountID = new ArrayList<>();
        for (Manager manager : this.accountJson.getManagers()) allAccountID.add(manager.getAccountID());
        for (Trainer trainer : this.accountJson.getTrainers()) allAccountID.add(trainer.getAccountID());
        for (Member member : this.accountJson.getMembers()) allAccountID.add(member.getAccountID());

        return allAccountID;
    }

    @Override
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> allAccounts = new ArrayList<>();
        allAccounts.addAll(this.accountJson.getManagers());
        allAccounts.addAll(this.accountJson.getTrainers());
        allAccounts.addAll(this.accountJson.getMembers());

        return allAccounts;
    }

    @Override
    public ArrayList<Trainer> getAllTrainers() {
        return this.accountJson.getTrainers();
    }

    @Override
    public ArrayList<Member> getAllMembers() {
        return this.accountJson.getMembers();
    }

    @Override
    public ArrayList<Member> getMembersByMembershipType(String membershipType) {
        ArrayList<Member> result = new ArrayList<>();
        for (Member member : this.accountJson.getMembers()) {
            if (member.getMembershipType().equals(membershipType)) {
                result.add(member);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Member> getInactiveMembers(int inactiveDays) throws ParseException {
        ArrayList<Member> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate curDate = LocalDate.now();
        for (Member member : this.accountJson.getMembers()) {
            LocalDate lastLogInDate = simpleDateFormat.parse(member.getLastLogInDate())
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (curDate.toEpochDay() - lastLogInDate.toEpochDay() >= inactiveDays) result.add(member);
        }

        return result;
    }

    @Override
    public boolean updateMemberMembershipType(String oldVal, String newVal) throws IOException {
        for (Member member : this.accountJson.getMembers()) {
            if (member.getMembershipType().equals(oldVal)) {
                member.setMembershipType(newVal);
            }
        }
        this.saveAccountJson();
        return true;
    }

    @Override
    public boolean updateMemberLastLogInDate(String accountID) throws IOException {
        for (Member member : this.accountJson.getMembers()) {
            if (member.getAccountID().equals(accountID)) {
                member.setLastLogInDate(LocalDate.now().toString());
                break;
            }
        }
        this.saveAccountJson();
        return true;
    }

    @Override
    public boolean updateMemberInviteeCntAddOne(String accountID) throws IOException {
        for (Member member : this.accountJson.getMembers()) {
            if (member.getAccountID().equals(accountID)) {
                member.setInviteeCnt(member.getInviteeCnt() + 1);
                break;
            }
        }
        this.saveAccountJson();
        return true;
    }

    @Override
    public boolean uploadAccountAvatar(String filePath, Account account) throws IOException {
        String newFileName = MediaWriter.copyFileToMediaDir(MediaDirectory.AVATAR, filePath, account.getAccountID());
        account.setAvatarFilename(newFileName);
        return true;
    }

    @Override
    public boolean deleteAccountAvatar(String accountID) throws IOException {
        MediaRemover.deleteMedia(MediaDirectory.AVATAR, accountID);
        return true;
    }
}
