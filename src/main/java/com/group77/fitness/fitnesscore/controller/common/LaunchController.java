package com.group77.fitness.fitnesscore.controller.common;


import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.util.PasswordMD5;
import com.group77.fitness.fitnesscore.vo.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Optional;

public class LaunchController {
    @FXML private TextField textFieldAccountID;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordPlainText;
    @FXML private Label labelPasswordVisibility;
    @FXML private Label labelAccountWarning, labelPasswordWarning;
    @FXML private Label labelGymName, labelAddress, labelTelephone, labelEmail;

    private boolean isPasswordVisible = false;
    private final Gym gym = new GymDAOImpl().getGym();

    public LaunchController() throws Exception {}

    @FXML
    public void initialize() {
        this.labelGymName.setText(this.gym.getGymName());
        this.labelAddress.setText(this.gym.getAddress());
        this.labelTelephone.setText(this.gym.getTelephone());
        this.labelEmail.setText(this.gym.getEmail());

        this.passwordField.textProperty().bindBidirectional(this.passwordPlainText.textProperty());

        this.passwordField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                this.buttonSignInMouseClicked();
            }
        });
        this.passwordPlainText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                this.buttonSignInMouseClicked();
            }
        });
    }

    @FXML public void buttonExitMouseClicked() {
        Platform.exit();
    }

    @FXML
    public void buttonSignInMouseClicked() {
        this.labelAccountWarning.setText("");   // Refresh its content
        this.labelPasswordWarning.setText("");

        boolean isEmpty = false;
        String accountID = this.textFieldAccountID.getText();
        if (accountID.equals("")) {
            this.labelAccountWarning.setText("Your account ID can't be empty!");
            isEmpty = true;
        }

        String password = this.passwordField.getText();
        if (password.equals("")) {
            this.labelPasswordWarning.setText("Your password can't be empty!");
            isEmpty = true;
        }

        if (isEmpty) return;

        String passwordMD5 = PasswordMD5.getMD5(password);
        try {
            AccountDAO accountDAO = new AccountDAOImpl();
            Account account = accountDAO.queryAccountByID(accountID);

            if (account == null) {  // There isn't such an Account
//                new GUIScheduler().createPopUp2("There is no such an account. You can sign up one.",
//                        GUIScheduler.class.getDeclaredMethod("openSignUpWindow"));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("There is no such an account. You can sign up one.");
                alert.setContentText("Do you want to sign up for an account?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) new GUIScheduler().openSignUpWindow();
                else alert.close();
            }
            else {
                if (account.getPassword().equals(passwordMD5)) {    // Password is correct
                    Stage stage = (Stage) this.textFieldAccountID.getScene().getWindow();
                    stage.close();

                    GUIScheduler guiScheduler = new GUIScheduler();
                    if (account instanceof Member) guiScheduler.openMemberHomeWindow(account);
                    else if (account instanceof Manager) guiScheduler.openManagerHomeWindow(account);
                    else if (account instanceof Trainer) guiScheduler.openTrainerHomeWindow(account);
                }
                else {  // Password is wrong
                    this.labelPasswordWarning.setText("The password is wrong!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonSignUpMouseClicked() throws IOException {
        Stage stage = (Stage) this.textFieldAccountID.getScene().getWindow();
        stage.close();
        new GUIScheduler().openSignUpWindow();
    }

    @FXML public void labelPasswordShowMouseClicked() {
        if (!this.isPasswordVisible) {
            this.isPasswordVisible = true;
            this.labelPasswordVisibility.setGraphic(new FontIcon("fa-eye:16"));
            this.passwordField.setVisible(false);
            this.passwordPlainText.setVisible(true);
        }

        else {
            this.isPasswordVisible = false;
            this.labelPasswordVisibility.setGraphic(new FontIcon("fa-eye-slash:16"));
            this.passwordField.setVisible(true);
            this.passwordPlainText.setVisible(false);
        }
    }
}
