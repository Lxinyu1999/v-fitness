package com.group77.fitness.fitnesscore.controller.common;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.util.MediaFileChooser;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.util.PasswordMD5;
import com.group77.fitness.fitnesscore.util.ProfileFormatChecker;
import com.group77.fitness.fitnesscore.vo.Trainer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TrainerProfileController {
    @FXML private Label labelTitle;
    @FXML private ImageView imageViewAvatar;
    @FXML private Button buttonUploadAvatar;
    @FXML private TextField textFieldAccountID;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordFieldConfirm;
    @FXML private ToggleGroup radioButtonGroupGender;
    @FXML private TextField textFieldBirthYear;
    @FXML private Button buttonConfirm;
    @FXML private Label labelWarningAccountID;
    @FXML private Label labelWarningPassword;
    @FXML private Label labelWarningPasswordConfirm;
    @FXML private Label labelWarningBirthYear;
    @FXML private Label labelWarningAvatar;
    @FXML private VBox vBoxTeachingSubjects;

    private String avatarFilePath = null;
    private boolean isRegister = true;
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final GymDAO gymDAO = new GymDAOImpl();

    private final ProfileFormatChecker profileFormatChecker = new ProfileFormatChecker();
    private final MediaFileChooser mediaFileChooser = new MediaFileChooser();

    public TrainerProfileController() throws Exception {}

    @FXML
    public void initialize() {
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
    }

    public void loadDataUpdateUI(String title) {
        this.labelTitle.setText(title);
        this.loadSportsCategoriesToVBoxTeachingSubjects();
    }

    public void loadSportsCategoriesToVBoxTeachingSubjects() {
        ArrayList<String> sportsCategories = this.gymDAO.getGym().getSportsCategories();

        for (String sportCat : sportsCategories) {
            this.vBoxTeachingSubjects.getChildren().add(new CheckBox(sportCat));
        }
    }

    public void buttonUploadAvatarMouseClicked() {
        this.avatarFilePath = this.mediaFileChooser.chooseImageFile("Choose your avatar", this.imageViewAvatar);
        if (this.avatarFilePath != null) this.labelWarningAvatar.setText("");
    }

    public void buttonConfirmMouseClicked() throws Exception {  // Exception from this.accountDAO.updateAccount()
        if (this.isRegister) {
            if (this.avatarFilePath == null) {
                this.labelWarningAvatar.setText("Please upload an avatar.");
                return;
            }

            if (this.profileFormatChecker.checkAccountID(this.labelWarningAccountID, this.textFieldAccountID.getText()) &&
                    this.profileFormatChecker.checkPassword(this.labelWarningPassword, this.passwordField.getText()) &&
                    this.profileFormatChecker.checkConfirmedPassword(this.labelWarningPasswordConfirm, this.passwordField.getText(), this.passwordFieldConfirm.getText()) &&
                    this.profileFormatChecker.checkBirthYear(this.labelWarningBirthYear, this.textFieldBirthYear.getText())) {
                String accountID = this.textFieldAccountID.getText();
                String password = PasswordMD5.getMD5(this.passwordField.getText());

                RadioButton radioButton = (RadioButton) this.radioButtonGroupGender.getSelectedToggle();
                String gender = radioButton.getText();

                int birthYear = Integer.parseInt(this.textFieldBirthYear.getText());

                ObservableList<Node> nodes = this.vBoxTeachingSubjects.getChildren();
                ArrayList<String> teachingSubjects = new ArrayList<>();
                for (Node node : nodes) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        teachingSubjects.add(checkBox.getText());
                    }
                }

                try {
                    Trainer newTrainer = new Trainer(accountID, password, gender, birthYear,
                            teachingSubjects);
                    this.accountDAO.addAccount(newTrainer, this.avatarFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if (this.profileFormatChecker.checkBirthYear(this.labelWarningBirthYear, this.textFieldBirthYear.getText())) {
                Trainer trainer = (Trainer) this.accountDAO.queryAccountByID(this.textFieldAccountID.getText());

                if (!this.passwordField.getText().equals("") && !passwordFieldConfirm.getText().equals("")) {
                    if (this.profileFormatChecker.checkPassword(this.labelWarningPassword, this.passwordField.getText()) &&
                            this.profileFormatChecker.checkConfirmedPassword(this.labelWarningPasswordConfirm,
                                    this.passwordField.getText(), this.passwordFieldConfirm.getText())) {
                        trainer.setPassword(PasswordMD5.getMD5(this.passwordField.getText()));
                    }
                }

                RadioButton radioButton = (RadioButton) this.radioButtonGroupGender.getSelectedToggle();
                trainer.setGender(radioButton.getText());
                trainer.setBirthYear(Integer.parseInt(this.textFieldBirthYear.getText()));

                ObservableList<Node> nodes = this.vBoxTeachingSubjects.getChildren();
                ArrayList<String> teachingSubjects = new ArrayList<>();
                for (Node node : nodes) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        teachingSubjects.add(checkBox.getText());
                    }
                }
                trainer.setTeachingSubjects(teachingSubjects);

                boolean result = this.avatarFilePath != null ? this.accountDAO.updateAccount(trainer, this.avatarFilePath) : this.accountDAO.updateAccount(trainer);
                if (result) new GUIScheduler().createNotificationWindow(this.labelTitle.getScene().getWindow(), NotificationType.SUCCESS, "Update succeeded!");
                else new GUIScheduler().createNotificationWindow(this.labelTitle.getScene().getWindow(), NotificationType.ERROR, "Update failed, please contact to Admin.");
            }
        }
    }

    /**
     * For Trainer Home's Mine tab to load profile data.
     * */
    public void loadProfileData(Trainer trainer) {
        this.isRegister = false;

        this.imageViewAvatar.setImage(new Image("file:///" + trainer.getAvatarAbsolutePath()));
        this.textFieldAccountID.setText(trainer.getAccountID());
        this.textFieldAccountID.setDisable(true);
        ObservableList<Toggle> genderRadioButtons = this.radioButtonGroupGender.getToggles();
        for (Toggle toggle : genderRadioButtons) {
            RadioButton radioButton = (RadioButton) toggle;
            if (radioButton.getText().equals(trainer.getGender())) {
                this.radioButtonGroupGender.selectToggle(toggle);
            }
        }
        this.textFieldBirthYear.setText(String.valueOf(trainer.getBirthYear()));
        ObservableList<Node> checkBoxes = this.vBoxTeachingSubjects.getChildren();
        ArrayList<String> teachingSubjects = trainer.getTeachingSubjects();
        for (String teachingSubject : teachingSubjects) {
            for (Node node : checkBoxes) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.getText().equals(teachingSubject)) checkBox.setSelected(true);
            }
        }
    }
}
