package com.group77.fitness.fitnesscore.controller.manager;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.VideoDAOImpl;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Gym;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class GymManagementController {
    @FXML private TextField textFieldGymName;
    @FXML private TextField textFieldAddress;
    @FXML private TextField textFieldTelephone;
    @FXML private TextField textFieldEmail;

    @FXML private Button buttonBasicInfoSaveChanges;

    @FXML private ListView<String> listViewSportsCategories;
    @FXML private Button buttonSportsCategoriesSaveChanges;

    @FXML private ListView<String> listViewMembershipTypes;
    @FXML private Button buttonMembershipTypesSaveChanges;

    @FXML private ListView<String> listViewCourseLevels;
    @FXML private Button buttonCourseLevelsSaveChanges;

    private final GymDAO gymDAO = new GymDAOImpl();
    private final VideoDAO videoDAO = new VideoDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    public GymManagementController() throws Exception {}
    @FXML public void initialize() {}

    public void loadDataUpdateUI() {
        this.textFieldGymName.setText(this.gymDAO.getGym().getGymName());
        this.textFieldAddress.setText(this.gymDAO.getGym().getAddress());
        this.textFieldTelephone.setText(this.gymDAO.getGym().getTelephone());
        this.textFieldEmail.setText(this.gymDAO.getGym().getEmail());

        // Add Change Listeners to the TextField (must after the setText())
        this.textFieldGymName.textProperty().addListener((observableValue, s, t1) -> {
            this.buttonBasicInfoSaveChanges.setDisable(false);
        });
        this.textFieldAddress.textProperty().addListener((observableValue, s, t1) -> {
            this.buttonBasicInfoSaveChanges.setDisable(false);
        });
        this.textFieldTelephone.textProperty().addListener((observableValue, s, t1) -> {
            this.buttonBasicInfoSaveChanges.setDisable(false);
        });
        this.textFieldEmail.textProperty().addListener((observableValue, s, t1) -> {
            this.buttonBasicInfoSaveChanges.setDisable(false);
        });

        // ------------------------------------------ ListView ------------------------------------------
        ObservableList<String> observableListSportsCategories = FXCollections.observableArrayList(this.gymDAO.getGym().getSportsCategories());
        this.listViewSportsCategories.setItems(observableListSportsCategories);
        // Must set the cell factory if wanna edit
        this.listViewSportsCategories.setCellFactory(TextFieldListCell.forListView());
        this.listViewSportsCategories.setEditable(true);
        this.listViewSportsCategories.setOnEditCommit(stringEditEvent -> {
            buttonSportsCategoriesSaveChanges.setDisable(false);
            listViewSportsCategories.getItems().set(stringEditEvent.getIndex(), stringEditEvent.getNewValue());
        });

        ObservableList<String> observableListMembershipTypes = FXCollections.observableArrayList(this.gymDAO.getGym().getMembershipTypes());
        this.listViewMembershipTypes.setItems(observableListMembershipTypes);
        this.listViewMembershipTypes.setCellFactory(TextFieldListCell.forListView());
        this.listViewMembershipTypes.setEditable(true);
        this.listViewMembershipTypes.setOnEditCommit(stringEditEvent -> {
            buttonMembershipTypesSaveChanges.setDisable(false);
            listViewMembershipTypes.getItems().set(stringEditEvent.getIndex(), stringEditEvent.getNewValue());
        });

        ObservableList<String> observableListCourseLevels = FXCollections.observableArrayList(this.gymDAO.getGym().getCourseLevels());
        this.listViewCourseLevels.setItems(observableListCourseLevels);
        this.listViewCourseLevels.setCellFactory(TextFieldListCell.forListView());
        this.listViewCourseLevels.setEditable(true);
        this.listViewCourseLevels.setOnEditCommit(stringEditEvent -> {
            buttonCourseLevelsSaveChanges.setDisable(false);
            listViewCourseLevels.getItems().set(stringEditEvent.getIndex(), stringEditEvent.getNewValue());
        });
    }

    private boolean createConfirmDialog(String oldVal, String newVal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Related items change confirmation");
        alert.setContentText(String.format("Are you sure change %s -> %s?", oldVal, newVal));

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    private String createInputDialog(String type) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Add A New Item");
        dialog.setContentText(String.format("Enter a new %s here: ", type));

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void createAddFailNotification() throws IOException {
        new GUIScheduler().createNotificationWindow(this.textFieldGymName.getScene().getWindow(), NotificationType.ERROR, "Failed. Repetitive item!");
    }

    private void createOperationSuccessNotification() throws IOException {
        new GUIScheduler().createNotificationWindow(this.textFieldGymName.getScene().getWindow(), NotificationType.SUCCESS, "Operation Successes!");
    }

    @FXML public void buttonBasicInfoSaveChangesMouseClicked() throws IOException {   // IOException from this.gymDAO.updateGym()
        Gym gym = this.gymDAO.getGym();
        gym.setGymName(this.textFieldGymName.getText());
        gym.setAddress(this.textFieldAddress.getText());
        gym.setTelephone(this.textFieldTelephone.getText());
        gym.setEmail(this.textFieldEmail.getText());

        this.gymDAO.updateGym(gym);
    }

    @FXML public void buttonSportsCategoriesSaveChangesMouseClicked() throws IOException {  // IOException from updateVideoCategory
        ObservableList<String> items = this.listViewSportsCategories.getItems();
        Gym gym = this.gymDAO.getGym();
        ArrayList<String> originalSportCategories = gym.getSportsCategories();

        for (int i = 0; i < items.size(); i++) {
            String original = originalSportCategories.get(i);

            if (!items.get(i).equals(original)) {     // if changes
                if (this.videoDAO.getVideoListByCategory(original).size() != 0) {
                    if (this.createConfirmDialog(original, items.get(i))) {     // Confirmation accepts
                        this.videoDAO.updateVideoCategory(original, items.get(i));
                        gym.setSportsCategories(new ArrayList<>(items));
                        this.gymDAO.updateGym(gym);
                        this.createOperationSuccessNotification();
                    }
                    else {  // Operation cancels
                        this.listViewSportsCategories.getItems().set(i, original);
                    }
                }
                else {
                    gym.setSportsCategories(new ArrayList<>(items));
                    this.gymDAO.updateGym(gym);
                    this.createOperationSuccessNotification();
                }
            }
        }
    }

    @FXML public void buttonMembershipTypesSaveChangesMouseClicked() throws IOException {
        ObservableList<String> items = this.listViewMembershipTypes.getItems();
        Gym gym = this.gymDAO.getGym();
        ArrayList<String> originalMembershipTypes = gym.getMembershipTypes();

        for (int i = 0; i < items.size(); i++) {
            String original = originalMembershipTypes.get(i);

            if (!items.get(i).equals(original)) {
                if (this.accountDAO.getMembersByMembershipType(original).size() != 0) {
                    if (this.createConfirmDialog(original, items.get(i))) {
                        this.accountDAO.updateMemberMembershipType(original, items.get(i));
                        gym.setMembershipTypes(new ArrayList<>(items));
                        this.gymDAO.updateGym(gym);
                        this.createOperationSuccessNotification();
                    }
                    else {
                        this.listViewMembershipTypes.getItems().set(i, original);
                    }
                }
                else {
                    gym.setMembershipTypes(new ArrayList<>(items));
                    this.gymDAO.updateGym(gym);
                    this.createOperationSuccessNotification();
                }
            }
        }
    }

    @FXML public void buttonCourseLevelsSaveChangesMouseClicked() throws IOException {
        ObservableList<String> items = this.listViewCourseLevels.getItems();
        Gym gym = this.gymDAO.getGym();
        ArrayList<String> originalMembershipTypes = gym.getCourseLevels();

        for (int i = 0; i < items.size(); i++) {
            String original = originalMembershipTypes.get(i);

            if (!items.get(i).equals(original)) {
                if (this.videoDAO.getVideoListByCourseLevel(original).size() != 0) {
                    if (this.createConfirmDialog(original, items.get(i))) {
                        this.videoDAO.updateVideoLevel(original, items.get(i));
                        gym.setCourseLevels(new ArrayList<>(items));
                        this.gymDAO.updateGym(gym);
                        this.createOperationSuccessNotification();
                    }
                    else {
                        this.listViewCourseLevels.getItems().set(i, original);
                    }
                }
                else {
                    gym.setCourseLevels(new ArrayList<>(items));
                    this.gymDAO.updateGym(gym);
                    this.createOperationSuccessNotification();
                }
            }
        }
    }

    @FXML public void buttonSportsCategoriesAddMouseClicked() throws IOException {
        String input = this.createInputDialog("Sport Category");
        if (input != null) {
            if (this.gymDAO.getGym().getSportsCategories().contains(input)) this.createAddFailNotification();
            else {
                Gym gym = this.gymDAO.getGym();
                this.listViewSportsCategories.getItems().add(input);
                gym.setSportsCategories(new ArrayList<>(this.listViewSportsCategories.getItems()));
                this.gymDAO.updateGym(gym);
                this.createOperationSuccessNotification();
            }
        }
    }

    @FXML public void buttonMembershipTypesAddMouseClicked() throws IOException {
        String input = this.createInputDialog("Membership Type");
        if (input != null) {
            if (this.gymDAO.getGym().getMembershipTypes().contains(input)) this.createAddFailNotification();
            else {
                Gym gym = this.gymDAO.getGym();
                this.listViewMembershipTypes.getItems().add(input);
                gym.setMembershipTypes(new ArrayList<>(this.listViewMembershipTypes.getItems()));
                this.gymDAO.updateGym(gym);
                this.createOperationSuccessNotification();
            }
        }
    }

    @FXML public void buttonCourseLevelsAddMouseClicked() throws IOException {
        String input = this.createInputDialog("Course Level");
        if (input != null) {
            if (this.gymDAO.getGym().getCourseLevels().contains(input)) this.createAddFailNotification();
            else {
                Gym gym = this.gymDAO.getGym();
                this.listViewCourseLevels.getItems().add(input);
                gym.setCourseLevels(new ArrayList<>(this.listViewCourseLevels.getItems()));
                this.gymDAO.updateGym(gym);
                this.createOperationSuccessNotification();
            }
        }
    }

    @FXML public void buttonSportsCategoriesDeleteMouseClicked() throws IOException {
        String selectedItem = this.listViewSportsCategories.getSelectionModel().getSelectedItem();
        if (this.videoDAO.getVideoListByCategory(selectedItem).size() != 0) {
            new GUIScheduler().createNotificationWindow(this.textFieldGymName.getScene().getWindow(), NotificationType.ERROR, "Failed. There exist video(s) that belong to this category, please delete it(them) first.");
        }
        else {
            this.listViewSportsCategories.getItems().remove(selectedItem);
            Gym gym = this.gymDAO.getGym();
            gym.setSportsCategories(new ArrayList<>(this.listViewSportsCategories.getItems()));
            this.gymDAO.updateGym(gym);
            this.createOperationSuccessNotification();
        }
    }

    @FXML public void buttonMembershipTypesDeleteMouseClicked() throws IOException {
        String selectedItem = this.listViewMembershipTypes.getSelectionModel().getSelectedItem();
        if (this.accountDAO.getMembersByMembershipType(selectedItem).size() != 0) {
            new GUIScheduler().createNotificationWindow(this.textFieldGymName.getScene().getWindow(), NotificationType.ERROR, "Failed. There exist member(s) that belong to this membership type, please solve this first.");
        }
        else {
            this.listViewMembershipTypes.getItems().remove(selectedItem);
            Gym gym = this.gymDAO.getGym();
            gym.setMembershipTypes(new ArrayList<>(this.listViewMembershipTypes.getItems()));
            this.gymDAO.updateGym(gym);
            this.createOperationSuccessNotification();
        }
    }

    @FXML public void buttonCourseLevelsDeleteMouseClicked() throws IOException {
        String selectedItem = this.listViewCourseLevels.getSelectionModel().getSelectedItem();
        if (this.videoDAO.getVideoListByCourseLevel(selectedItem).size() != 0) {
            new GUIScheduler().createNotificationWindow(this.textFieldGymName.getScene().getWindow(), NotificationType.ERROR, "Failed. There exist video(s) that belong to this level, please delete it(them) first.");
        }
        else {
            this.listViewCourseLevels.getItems().remove(selectedItem);
            Gym gym = this.gymDAO.getGym();
            gym.setCourseLevels(new ArrayList<>(this.listViewCourseLevels.getItems()));
            this.gymDAO.updateGym(gym);
            this.createOperationSuccessNotification();
        }
    }
}
