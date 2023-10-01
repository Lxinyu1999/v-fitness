package com.group77.fitness.fitnesscore.controller.manager;

import com.group77.fitness.fitnesscore.controller.HomeCommon;
import com.group77.fitness.fitnesscore.controller.common.TopBarController;
import com.group77.fitness.fitnesscore.controller.common.TrainerProfileController;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.SeriesDAO;
import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SeriesDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.VideoDAOImpl;
import com.group77.fitness.fitnesscore.util.MediaFileChooser;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Series;
import com.group77.fitness.fitnesscore.vo.Trainer;
import com.group77.fitness.fitnesscore.vo.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

public class ManagerHomeController {
    // flags to sign whether a  tab has been loaded
    private boolean tabVideoManagementLoaded = false;
    private boolean tabMemberListLoaded = false;
    private boolean tabTrainerListLoaded = false;
    private boolean tabGymManagementLoaded = false;

    @FXML private TopBarController topBarController;
    @FXML private TrainerProfileController trainerProfileController;
    @FXML private GymManagementController gymManagementController;
    @FXML private VBox vBoxButtonTab;
    @FXML private TabPane tabPane;

    // ---------- Tab: Video Management ----------
    @FXML private TableView<Video> tableViewVideo;
    @FXML private ImageView imageViewVideoCover;
    @FXML private Button buttonUploadVideoCover;
    @FXML private TextField textFieldVideoFilePath;
    @FXML private Button buttonUploadVideoFile;
    @FXML private TextField textFieldDisplayName;
    @FXML private ChoiceBox<String> choiceBoxBelongingSeries;
    @FXML private ChoiceBox<String> choiceBoxCategories;
    @FXML private ChoiceBox<String> choiceBoxLevels;
    @FXML private ChoiceBox<String> choiceBoxLowestMembershipRequirement;

    // ---------- Tab: User List ----------
    @FXML private TableView<Member> tableViewMemberList;

    // ---------- Tab: Trainer List ----------
    @FXML private TableView<Trainer> tableViewTrainerList;

    // ---------- Tab: Inactive Members ----------
    @FXML private Spinner<Integer> spinnerInactiveDaysThreshold;
    @FXML private TableView<Member> tableViewInactiveMembers;

    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final VideoDAO videoDAO = new VideoDAOImpl();
    private final GymDAO gymDAO = new GymDAOImpl();
    private final SeriesDAO seriesDAO = new SeriesDAOImpl();
    private String videoCoverFilePath = null;
    private String videoFilePath = null;

    private final MediaFileChooser mediaFileChooser = new MediaFileChooser();

    public ManagerHomeController() throws Exception {}

    @FXML
    public void initialize() {
        new HomeCommon().setButtonTabMouseClickedEvents(this.tabPane, this.vBoxButtonTab.getChildren());

        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observableValue, oldTab, newTab) -> {
            String tabText = newTab.getText();

            if (!this.tabVideoManagementLoaded && tabText.equals("Video Management")) {
                this.tabVideoManagementLoaded = true;
                this.loadVideoManagementData();
            }
            else if (!this.tabMemberListLoaded && tabText.equals("Member List")) {
                this.tabMemberListLoaded = true;
                this.loadMemberListData();
            }
            else if (!this.tabTrainerListLoaded && tabText.equals("Trainer List")) {
                this.tabTrainerListLoaded = true;
                this.loadTrainerListData();
            }
            else if (!this.tabGymManagementLoaded && tabText.equals("Gym Management")) {
                this.tabGymManagementLoaded = true;
                this.gymManagementController.loadDataUpdateUI();
            }
        });

        this.spinnerInactiveDaysThreshold.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 10));
        this.spinnerInactiveDaysThreshold.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                if (newValue == null) this.spinnerInactiveDaysThreshold.getValueFactory().setValue(oldValue);
            }
            catch (Exception ignored) {}
        });
    }

    public void loadDataUpdateUI() {
        this.topBarController.loadDataUpdateUI(this.tabPane);
        this.trainerProfileController.loadDataUpdateUI("Register A New Trainer");
    }

    // --------------------------------- Tab: Video Management ---------------------------------
    private void loadVideoManagementData() {
        this.loadVideoDataToTableView();

        ArrayList<Series> allSeries = this.seriesDAO.getAllSeries();
        this.choiceBoxBelongingSeries.getItems().add("--None--");
        for (Series series : allSeries) {
            this.choiceBoxBelongingSeries.getItems().add(String.format("%s | %s", series.getSeriesID(), series.getSeriesName()));
        }
        this.choiceBoxBelongingSeries.getSelectionModel().selectFirst();

        this.choiceBoxCategories.getItems().addAll(this.gymDAO.getGym().getSportsCategories());
        this.choiceBoxCategories.getSelectionModel().selectFirst();

        this.choiceBoxLevels.getItems().addAll(this.gymDAO.getGym().getCourseLevels());
        this.choiceBoxLevels.getSelectionModel().selectFirst();

        this.choiceBoxLowestMembershipRequirement.getItems().addAll(this.gymDAO.getGym().getMembershipTypes());
        this.choiceBoxLowestMembershipRequirement.getSelectionModel().selectFirst();
    }

    public void loadVideoDataToTableView() {
        ObservableList<Video> observableList = FXCollections.observableArrayList(this.videoDAO.getVideoList());

        TableColumn videoIDColumn = new TableColumn("Video ID");
        videoIDColumn.setCellValueFactory(new PropertyValueFactory<>("videoID"));

        TableColumn displayNameColumn = new TableColumn("Name");
        displayNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));

        TableColumn uploadTimeColumn = new TableColumn("Upload Time");
        uploadTimeColumn.setCellValueFactory(new PropertyValueFactory<>("uploadTime"));


        this.tableViewVideo.getColumns().clear();   // clear the table heads
        this.tableViewVideo.setItems(null);         // clear the table rows

        this.tableViewVideo.getColumns().addAll(videoIDColumn, displayNameColumn, uploadTimeColumn);
        this.tableViewVideo.setItems(observableList);
    }

    @FXML
    public void buttonDeleteVideoMouseClicked() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning: This operation is not reversible!");
        alert.setContentText("Are you sure to delete the selected video?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Video video = this.tableViewVideo.getSelectionModel().getSelectedItem();
            videoDAO.deleteVideoByID(video.getVideoID());
            this.loadVideoDataToTableView();    // Refresh the table
        }
    }

    @FXML
    public void buttonUploadVideoCoverMouseClicked() {
        this.videoCoverFilePath = this.mediaFileChooser.chooseImageFile("Choose the video cover", this.imageViewVideoCover);
    }

    @FXML
    public void buttonUploadVideoFileMouseClicked() {
        this.videoFilePath = this.mediaFileChooser.chooseVideoFile("Choose the video file", this.textFieldVideoFilePath);
    }

    @FXML
    public void buttonCreateNewSeries() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input dialog");
        dialog.setHeaderText("Create a new video series");
        dialog.setContentText("Input the name of new series:");

        Optional<String> seriesName = dialog.showAndWait();
        seriesName.ifPresent(name -> {
            try {
                this.seriesDAO.addSeries(new Series(name));
                this.choiceBoxBelongingSeries.getItems().add(name);
                this.choiceBoxBelongingSeries.getSelectionModel().selectLast();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void buttonConfirmUploadMouseClicked() throws IOException {
        if (!this.textFieldDisplayName.getText().equals("") && this.videoCoverFilePath != null && this.videoFilePath != null) {
            String seriesID;
            if (this.choiceBoxBelongingSeries.getValue().equals("--None--")) {
                seriesID = "";
            }
            else {
                String val = this.choiceBoxBelongingSeries.getValue();
                int end = val.indexOf(" | ");
                seriesID = val.substring(0, end);
            }
            this.videoDAO.addVideo(new Video(this.textFieldDisplayName.getText(), this.choiceBoxCategories.getSelectionModel().getSelectedItem(),
                            seriesID, this.choiceBoxLevels.getSelectionModel().getSelectedItem(),
                            this.choiceBoxLowestMembershipRequirement.getSelectionModel().getSelectedItem()),
                    this.videoCoverFilePath, this.videoFilePath);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("The information is incomplete!");
            alert.setContentText("Please check: Display Name, Video Cover and Video File.");
            alert.showAndWait();
        }
    }


    // --------------------------------- Tab: User List ---------------------------------
    private void loadMemberListData() {
        ObservableList<Member> observableList = FXCollections.observableArrayList(this.accountDAO.getAllMembers());

        TableColumn accountIDColumn = new TableColumn("ID");
        accountIDColumn.setCellValueFactory(new PropertyValueFactory<>("accountID"));

        TableColumn genderIDColumn = new TableColumn("Gender");
        genderIDColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn birthYearColumn = new TableColumn("Birth Year");
        birthYearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        TableColumn heightColumn = new TableColumn("Height");
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn weightColumn = new TableColumn("Weight");
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn membershipTypeColumn = new TableColumn("Membership Type");
        membershipTypeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        TableColumn targetsColumn = new TableColumn("Targets");
        targetsColumn.setCellValueFactory(new PropertyValueFactory<>("targets"));

        TableColumn physicalAbilitiesColumn = new TableColumn("Physical Abilities");
        physicalAbilitiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalAbilities"));

        this.tableViewMemberList.getColumns().clear();
        this.tableViewMemberList.setItems(null);

        this.tableViewMemberList.getColumns().addAll(accountIDColumn, genderIDColumn, birthYearColumn, heightColumn,
                weightColumn, membershipTypeColumn, targetsColumn, physicalAbilitiesColumn);
        this.tableViewMemberList.setItems(observableList);
    }


    // --------------------------------- Tab: Trainer List ---------------------------------
    private void loadTrainerListData() {
        ObservableList<Trainer> observableList = FXCollections.observableArrayList(this.accountDAO.getAllTrainers());

        TableColumn accountIDColumn = new TableColumn("ID");
        accountIDColumn.setCellValueFactory(new PropertyValueFactory<>("accountID"));

        TableColumn genderIDColumn = new TableColumn("Gender");
        genderIDColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn birthYearColumn = new TableColumn("Birth Year");
        birthYearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        TableColumn teachingSubjectsColumn = new TableColumn("Teaching Subjects");
        teachingSubjectsColumn.setCellValueFactory(new PropertyValueFactory<>("teachingSubjectsString"));

        this.tableViewMemberList.getColumns().clear();
        this.tableViewMemberList.setItems(null);

        this.tableViewTrainerList.getColumns().addAll(accountIDColumn, genderIDColumn, birthYearColumn, teachingSubjectsColumn);
        this.tableViewTrainerList.setItems(observableList);
    }

    // --------------------------------- Tab: Inactive Members ---------------------------------
    @FXML public void buttonInactiveMembersMouseClicked() throws ParseException {

        ArrayList<Member> inactiveMembers = this.accountDAO.getInactiveMembers(this.spinnerInactiveDaysThreshold.getValue());

        TableColumn accountIDColumn = new TableColumn("ID");
        accountIDColumn.setCellValueFactory(new PropertyValueFactory<>("accountID"));

        TableColumn lastLogInDateColumn = new TableColumn("Last Log In");
        lastLogInDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogInDate"));

        TableColumn inactiveDaysColumn = new TableColumn("Inactive Days");
        inactiveDaysColumn.setCellValueFactory(new PropertyValueFactory<>("inactiveDays"));

        this.tableViewInactiveMembers.getColumns().clear();
        this.tableViewInactiveMembers.setItems(null);

        this.tableViewInactiveMembers.getColumns().addAll(accountIDColumn, lastLogInDateColumn, inactiveDaysColumn);
        this.tableViewInactiveMembers.setItems(FXCollections.observableArrayList(inactiveMembers));
    }
}
