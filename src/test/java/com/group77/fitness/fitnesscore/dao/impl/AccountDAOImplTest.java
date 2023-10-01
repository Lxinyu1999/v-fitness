package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.vo.Account;
import com.group77.fitness.fitnesscore.vo.Manager;
import com.group77.fitness.fitnesscore.vo.Member;


public class AccountDAOImplTest {
    public static void main(String[] args) throws Exception {
        AccountDAO accountDAO = new AccountDAOImpl();
//        System.out.println(accountDAO.getAllAccountID());
//
//        accountDAO.addAccount(new Manager("test", "123456", "Male", 2002));
//        System.out.println(accountDAO.getAllAccountID());

//        System.out.println(accountDAO.getAllAccountObject());

//        accountDAO.deleteAccountByID("Alex");
//        accountDAO.updateAccount(new Manager("Boss", "", "Male", 2000));

        Account account = accountDAO.queryAccountByID("Boss");
        System.out.println(account.getAvatarFilename());
    }
}
