package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.RecordDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.RecordDAOImpl;
import com.group77.fitness.fitnesscore.util.MediaFileChooser;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.util.PasswordMD5;
import com.group77.fitness.fitnesscore.util.ProfileFormatChecker;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Record;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class MemberProfileController {
    @FXML private Label labelTitle;
    @FXML private ImageView imageViewAvatar;
    @FXML private Button buttonUploadAvatar;
    @FXML private TextField textFieldAccountID;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordFieldConfirm;
    @FXML private ToggleGroup radioButtonGroupGender;
    @FXML private TextField textFieldBirthYear;
    @FXML private TextField textFieldHeight;
    @FXML private TextField textFieldWeight;
    @FXML private TextArea textAreaTargets;
    @FXML private TextArea textAreaPhysicalAbilities;
    @FXML private Button buttonConfirm;
    @FXML private Button buttonLogOff;

    @FXML private HBox hBoxInvitation;
    @FXML private TextField textFieldInviterID;
    @FXML private Label labelWarningInviterID;

    @FXML private Label labelWarningAccountID;
    @FXML private Label labelWarningPassword;
    @FXML private Label labelWarningPasswordConfirm;
    @FXML private Label labelWarningBirthYear;
    @FXML private Label labelWarningHeight;
    @FXML private Label labelWarningWeight;
    @FXML private Label labelWarningAvatar;

    private Member curMember;
    private String avatarFilePath = null;
    private boolean isRegister = true;
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final RecordDAO recordDAO = new RecordDAOImpl();
    private final ProfileFormatChecker profileFormatChecker = new ProfileFormatChecker();
    private final MediaFileChooser mediaFileChooser = new MediaFileChooser();

    public MemberProfileController() throws Exception {}

    @FXML
    public void initialize() {
        // Add a listener to the event that textFieldAccountID lost focus
        this.textFieldAccountID.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkAccountID(this.labelWarningAccountID, this.textFieldAccountID.getText());
        });

        this.passwordField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkPassword(this.labelWarningPassword, this.passwordField.getText());
        });

        this.passwordFieldConfirm.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkConfirmedPassword(this.labelWarningPasswordConfirm, this.passwordField.getText(), this.passwordFieldConfirm.getText());
        });

        this.textFieldBirthYear.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkBirthYear(this.labelWarningBirthYear, this.textFieldBirthYear.getText());
        });

        this.textFieldHeight.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkHeight(this.labelWarningHeight, this.textFieldHeight.getText());
        });

        this.textFieldWeight.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkWeight(this.labelWarningWeight, this.textFieldWeight.getText());
        });

        this.textFieldInviterID.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) this.profileFormatChecker.checkInviterID(this.labelWarningInviterID, this.textFieldInviterID.getText());
        });
    }

    @FXML
    public void buttonUploadAvatarMouseClicked() {
        this.avatarFilePath = this.mediaFileChooser.chooseImageFile("Choose your avatar", this.imageViewAvatar);
        if (this.avatarFilePath != null) this.labelWarningAvatar.setText("");
    }

    @FXML
    public void buttonConfirmMouseClicked() throws Exception {
        if (this.isRegister) {
            if (this.avatarFilePath == null) {
                this.labelWarningAvatar.setText("Please choose your avatar.");
                return;
            }

            if (this.profileFormatChecker.checkAccountID(this.labelWarningAccountID, this.textFieldAccountID.getText()) &&
                    this.profileFormatChecker.checkPassword(this.labelWarningPassword, this.passwordField.getText()) &&
                    this.profileFormatChecker.checkConfirmedPassword(this.labelWarningPasswordConfirm, this.passwordField.getText(), this.passwordFieldConfirm.getText()) &&
                    this.profileFormatChecker.checkBirthYear(this.labelWarningBirthYear, this.textFieldBirthYear.getText()) &&
                    this.profileFormatChecker.checkHeight(this.labelWarningHeight, this.textFieldHeight.getText()) &&
                    this.profileFormatChecker.checkWeight(this.labelWarningWeight, this.textFieldWeight.getText())) {

                String accountID = this.textFieldAccountID.getText();
                String password = PasswordMD5.getMD5(this.passwordField.getText());

                RadioButton radioButton = (RadioButton) this.radioButtonGroupGender.getSelectedToggle();
                String gender = radioButton.getText();

                int birthYear = Integer.parseInt(this.textFieldBirthYear.getText());
                int height = Integer.parseInt(this.textFieldHeight.getText());
                int weight = Integer.parseInt(this.textFieldWeight.getText());
                String membershipType = "Ordinary";
                String targets = this.textAreaTargets.getText();
                String physicalAbilities = this.textAreaPhysicalAbilities.getText();

                if (this.profileFormatChecker.checkInviterID(this.labelWarningInviterID, this.textFieldInviterID.getText())) {
                    this.accountDAO.updateMemberInviteeCntAddOne(this.textFieldInviterID.getText());
                }

                try {
                    Member newMember = new Member(accountID, password, gender, birthYear,
                            height, weight, membershipType, targets, physicalAbilities);
                    this.accountDAO.addAccount(newMember, this.avatarFilePath);
                    this.recordDAO.addRecord(new Record(accountID, "", 0, new ArrayList<>(), new ArrayList<>()));
                    Stage stage = (Stage) this.buttonConfirm.getScene().getWindow();
                    stage.close();
                    new GUIScheduler().openMemberHomeWindow(newMember);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if (this.profileFormatChecker.checkBirthYear(this.labelWarningBirthYear, this.textFieldBirthYear.getText()) &&
                    this.profileFormatChecker.checkHeight(this.labelWarningHeight, this.textFieldHeight.getText()) &&
                    this.profileFormatChecker.checkWeight(this.labelWarningWeight, this.textFieldWeight.getText())) {

                Member member = (Member) this.accountDAO.queryAccountByID(this.textFieldAccountID.getText());

                if (!this.passwordField.getText().equals("") && !passwordFieldConfirm.getText().equals("")) {
                    if (this.profileFormatChecker.checkPassword(this.labelWarningPassword, this.passwordField.getText()) &&
                            this.profileFormatChecker.checkConfirmedPassword(this.labelWarningPasswordConfirm,
                                    this.passwordField.getText(), this.passwordFieldConfirm.getText())) {
                        member.setPassword(PasswordMD5.getMD5(this.passwordField.getText()));
                    }
                }

                RadioButton radioButton = (RadioButton) this.radioButtonGroupGender.getSelectedToggle();
                member.setGender(radioButton.getText());
                member.setBirthYear(Integer.parseInt(this.textFieldBirthYear.getText()));
                member.setHeight(Integer.parseInt(this.textFieldHeight.getText()));
                member.setWeight(Integer.parseInt(this.textFieldWeight.getText()));
                member.setTargets(this.textAreaTargets.getText());
                member.setPhysicalAbilities(this.textAreaPhysicalAbilities.getText());

                boolean result = this.avatarFilePath != null ? this.accountDAO.updateAccount(member, this.avatarFilePath) : this.accountDAO.updateAccount(member);
                if (result) new GUIScheduler().createNotificationWindow(this.labelTitle.getScene().getWindow(), NotificationType.SUCCESS, "Update succeeded!");
                else new GUIScheduler().createNotificationWindow(this.labelTitle.getScene().getWindow(), NotificationType.ERROR, "Update failed, please contact to Admin.");
            }
        }
    }


    @FXML
    public void loadProfileData(Member member) {
        this.isRegister = false;
        this.hBoxInvitation.setVisible(false);

        this.labelTitle.setText("Update My Profile");
        this.imageViewAvatar.setImage(new Image("file:///" + member.getAvatarAbsolutePath()));
        this.textFieldAccountID.setText(member.getAccountID());
        this.textFieldAccountID.setDisable(true);
        ObservableList<Toggle> genderRadioButtons = this.radioButtonGroupGender.getToggles();
        for (Toggle toggle : genderRadioButtons) {
            RadioButton radioButton = (RadioButton) toggle;
            if (radioButton.getText().equals(member.getGender())) {
                this.radioButtonGroupGender.selectToggle(toggle);
            }
        }
        this.textFieldBirthYear.setText(String.valueOf(member.getBirthYear()));
        this.textFieldHeight.setText(String.valueOf(member.getHeight()));
        this.textFieldWeight.setText(String.valueOf(member.getWeight()));
        this.textAreaTargets.setText(member.getTargets());
        this.textAreaPhysicalAbilities.setText(member.getPhysicalAbilities());

        this.buttonLogOff.setVisible(true);
        this.curMember = member;
    }

    @FXML
    public void buttonLogOffMouseClicked() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Log off means deleting all of the data related to thc current account");
        alert.setContentText("Do you want to continue with this operation?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.accountDAO.deleteAccountByID(curMember.getAccountID());
            Stage stage = (Stage) this.buttonLogOff.getScene().getWindow();
            stage.close();
            new GUIScheduler().openSignInWindow();
        }
    }
}
