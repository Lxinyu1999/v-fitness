package com.group77.fitness.fitnesscore.util;

import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.vo.Member;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.util.Calendar;

public class ProfileFormatChecker {

    private final AccountDAO accountDAO = new AccountDAOImpl();

    public ProfileFormatChecker() throws Exception {}

    public void setLabelWarningContent(Label label, String content, boolean isNormal) {
        if (label == null) return;
        if (isNormal) label.setTextFill(Paint.valueOf("GREEN"));
        else label.setTextFill(Paint.valueOf("RED"));
        label.setText(content);
    }

    /**
     * Check whether the accountID meets the format requirements: 3~30 alphanumeric characters.
     * If not, give warning to users.
     * */
    public boolean checkAccountID(Label labelWarning, String accountID) {
        if (accountID.length() < 3 || accountID.length() > 30) {
            this.setLabelWarningContent(labelWarning, "❌ The length of account ID is not inappropriate.", false);
            return false;
        }
        for (int i = 0; i < accountID.length(); i++) {
            if (!Character.isLetter(accountID.charAt(i)) && !Character.isDigit(accountID.charAt(i))) {
                this.setLabelWarningContent(labelWarning, "❌ The account ID contains illegal characters.", false);
                return false;
            }
        }

        if (this.accountDAO.queryAccountByID(accountID) != null) {
            this.setLabelWarningContent(labelWarning, "❌ The account ID has been registered.", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ Available account ID.", true);
        return true;
    }

    public boolean checkPassword(Label labelWarning, String password) {
        if (password.length() < 8 || password.length() > 30) {
            this.setLabelWarningContent(labelWarning, "❌ The length of password is not inappropriate.", false);
            return false;
        }

        boolean hasLetter = false, hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) hasLetter = true;
            else if (Character.isDigit(password.charAt(i))) hasDigit = true;
        }

        if (!hasLetter || !hasDigit) {
            this.setLabelWarningContent(labelWarning, "❌ The password doesn't meet the requirements", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ A valid password.", true);
        return true;
    }

    public boolean checkConfirmedPassword(Label labelWarning, String password, String confirmedPassword) {
        if (password.equals("") && confirmedPassword.equals("")) {
            this.setLabelWarningContent(labelWarning, "❌ Passwords are both empty.", false);
            return false;
        }

        if (!this.checkPassword(null, password)) {
            this.setLabelWarningContent(labelWarning, "❌ The password doesn't meet the requirements.", false);
            return false;
        }

        if (!confirmedPassword.equals(password)) {
            this.setLabelWarningContent(labelWarning, "❌ Passwords differ.", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ Passwords are same.", true);
        return true;
    }

    public boolean checkBirthYear(Label labelWarning, String birthYear) {
        try {
            int birthYearInt = Integer.parseInt(birthYear);
            int curYear = Calendar.getInstance().get(Calendar.YEAR);
            if (birthYearInt < (curYear - 150) || birthYearInt > curYear) {
                this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
                return false;
            }
        }
        catch (NumberFormatException e) {
            this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ Valid value.", true);
        return true;
    }

    public boolean checkHeight(Label labelWarning, String height) {
        try {
            int heightInt = Integer.parseInt(height);
            if (heightInt < 30 || heightInt > 250) {
                this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
                return false;
            }
        }
        catch (NumberFormatException e) {
            this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ Valid value.", true);
        return true;
    }

    public boolean checkWeight(Label labelWarning, String weight) {
        try {
            int weightInt = Integer.parseInt(weight);
            if (weightInt < 10 || weightInt > 400) {
                this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
                return false;
            }
        }
        catch (NumberFormatException e) {
            this.setLabelWarningContent(labelWarning, "❌ Invalid value.", false);
            return false;
        }

        this.setLabelWarningContent(labelWarning, "✔ Valid value.", true);
        return true;
    }

    public boolean checkInviterID(Label labelWarning, String inviterID) {
        if (this.accountDAO.queryAccountByID(inviterID) instanceof Member) {
            this.setLabelWarningContent(labelWarning, "✔ Valid.", true);
            return true;
        }
        else {
            this.setLabelWarningContent(labelWarning, "❌ Invalid.", false);
            return false;
        }
    }
}
