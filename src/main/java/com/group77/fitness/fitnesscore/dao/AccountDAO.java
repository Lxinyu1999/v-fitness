package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Account;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Trainer;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public interface AccountDAO {
    boolean addAccount(Account account, String avatarFilePath) throws Exception;
    boolean deleteAccountByID(String accountID) throws IOException;
    boolean updateAccount(Account account) throws Exception;
    boolean updateAccount(Account account, String avatarFilePath) throws Exception;
    Account queryAccountByID(String accountID);
    
    ArrayList<String> getAllAccountID();
    ArrayList<Account> getAllAccounts();
    ArrayList<Trainer> getAllTrainers();
    ArrayList<Member> getAllMembers();
    ArrayList<Member> getMembersByMembershipType(String membershipType);
    ArrayList<Member> getInactiveMembers(int inactiveDays) throws ParseException;

    boolean updateMemberMembershipType(String oldVal, String newVal) throws IOException;
    boolean updateMemberLastLogInDate(String accountID) throws IOException;
    boolean updateMemberInviteeCntAddOne(String accountID) throws IOException;

    // File operations
    boolean uploadAccountAvatar(String filePath, Account account) throws IOException;
    boolean deleteAccountAvatar(String accountID) throws IOException;
}
