package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.controller.HomeCommon;
import com.group77.fitness.fitnesscore.controller.common.TopBarController;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.VideoDAOImpl;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Trainer;
import com.group77.fitness.fitnesscore.vo.Video;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;


public class MemberHomeController {
    // flags to sign whether a  tab has been loaded
    private boolean tabTrainersLoaded = false;
    private boolean tabMembershipLoaded = false;
    private boolean tabMineLoaded = false;

    // Nested controllers. Inject the controllers here
    @FXML private TopBarController topBarController;
    @FXML private MemberProfileController memberProfileController;
    @FXML private VideoWatchingRecordsController videoWatchingRecordsController;

    @FXML private VBox vBoxButtonTab;
    @FXML private TabPane tabPane;

    // --------- Tab: Sports Videos ---------
    @FXML private ScrollPane scrollPaneSportsVideo;
    @FXML private FlowPane flowPaneSportsCategoryFilter;
    @FXML private FlowPane flowPaneCourseLevelFilter;
    @FXML private FlowPane flowPaneSportsVideo;

    // --------- Tab: Trainers ---------
    @FXML private ScrollPane scrollPaneTrainer;
    @FXML private FlowPane flowPaneTrainer;

    // --------- Tab: Membership ---------
    @FXML private Label labelInviteeCnt;
    @FXML private Label labelMembershipState;
    @FXML private ChoiceBox<String> choiceBoxOptionalMemberships;
    @FXML private Label labelWarningMembership;
    @FXML private Button buttonChangeMembership;


    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final VideoDAO videoDAO = new VideoDAOImpl();
    private final GymDAO gymDAO = new GymDAOImpl();

    private Member curMember;

    public MemberHomeController() throws Exception {}

    @FXML
    public void initialize() {
        new HomeCommon().setButtonTabMouseClickedEvents(this.tabPane, this.vBoxButtonTab.getChildren(), "clearUserTabs");

        // add a listener to load data when switching tabs ("Delayed loading")
        this.tabPane.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> {
            int index = newIndex.intValue();

            if (index == 1) {
                try {
                    this.videoWatchingRecordsController.loadDataUpdateUI(this.curMember, this.tabPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (!this.tabTrainersLoaded && index == 2) {
                this.tabTrainersLoaded = true;
                try {
                    this.loadTrainerData();
                } catch (IOException | CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            else if (!this.tabMembershipLoaded && index == 3) {
                this.tabMembershipLoaded = true;
                this.loadMembershipData();
            }
            else if (!this.tabMineLoaded && index == 4) {
                this.tabMineLoaded = true;
                this.memberProfileController.loadProfileData(curMember);
            }
        });
    }

    public void loadDataUpdateUI() throws IOException {  // IOException from loadVideoData()
        // Make TopBarController loads data and update UI
        this.curMember = (Member) this.topBarController.loadDataUpdateUI(this.tabPane);
        this.accountDAO.updateMemberLastLogInDate(this.curMember.getAccountID());
        this.loadSportsCategoryFilter();
        this.loadCourseLevelFilter();
        this.loadVideoData(videoDAO.getVideoList());
    }

    // ---------------------------------- Tab: Sports Videos ----------------------------------
    private void loadSportsCategoryFilter() {
        ToggleGroup toggleGroupSportsCategories = new ToggleGroup();
        ArrayList<String> sportsCategories = this.gymDAO.getGym().getSportsCategories();
        RadioButton radioButtonAll = (RadioButton) this.flowPaneSportsCategoryFilter.getChildren().get(1);
        radioButtonAll.setToggleGroup(toggleGroupSportsCategories);

        for (String cat : sportsCategories) {
            RadioButton radioButton = new RadioButton(cat);
            radioButton.setToggleGroup(toggleGroupSportsCategories);
            this.flowPaneSportsCategoryFilter.getChildren().add(radioButton);
        }

        toggleGroupSportsCategories.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroupSportsCategories.getSelectedToggle();
            String cat = selectedRadioButton.getText();
            try {
                ArrayList<Video> videoArrayList =  cat.equals("All") ?
                        this.videoDAO.getVideoList() :
                        this.videoDAO.getVideoListByCategory(cat);

                if (videoArrayList.size() == 0) {
                    this.flowPaneSportsVideo.getChildren().clear();
                    this.flowPaneSportsVideo.getChildren().add(new Label("There is no this category of video yet."));
                }
                else
                    this.loadVideoData(videoArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCourseLevelFilter() {
        ToggleGroup toggleGroupLevels = new ToggleGroup();
        ArrayList<String> courseLevels = this.gymDAO.getGym().getCourseLevels();
        RadioButton radioButtonAll = (RadioButton) this.flowPaneCourseLevelFilter.getChildren().get(1);
        radioButtonAll.setToggleGroup(toggleGroupLevels);

        for (String level : courseLevels) {
            RadioButton radioButton = new RadioButton(level);
            radioButton.setToggleGroup(toggleGroupLevels);
            this.flowPaneCourseLevelFilter.getChildren().add(radioButton);
        }

        toggleGroupLevels.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroupLevels.getSelectedToggle();
            String level = selectedRadioButton.getText();
            try {
                ArrayList<Video> videoArrayList =  level.equals("All") ?
                        this.videoDAO.getVideoList() :
                        this.videoDAO.getVideoListByCourseLevel(level);

                if (videoArrayList.size() == 0) {
                    this.flowPaneSportsVideo.getChildren().clear();
                    this.flowPaneSportsVideo.getChildren().add(new Label("There is no this level of video yet."));
                }
                else
                    this.loadVideoData(videoArrayList);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadVideoData(ArrayList<Video> videoList) throws IOException {
        this.flowPaneSportsVideo.getChildren().clear();

        for (Video video : videoList) {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/member/VideoCard.fxml"));
            fxmlLoader.load();
            VideoCardController videoCardController = fxmlLoader.getController();
            this.flowPaneSportsVideo.getChildren().add(videoCardController.createVideoCard(video, this.curMember, this.tabPane));
        }
    }

    // ---------------------------------- Tab: Trainers ----------------------------------

    private void loadTrainerData() throws IOException, CloneNotSupportedException {
        ArrayList<Trainer> allTrainers = this.accountDAO.getAllTrainers();
        for (Trainer trainer : allTrainers) {
            Parent trainerCard = FXMLLoader.load(getClass().getResource("/fxml/member/TrainerCard.fxml"));

            ImageView imageViewAvatar = (ImageView) trainerCard.lookup("#imageViewAvatar");
            imageViewAvatar.setImage(new Image("file:///" + trainer.getAvatarAbsolutePath()));

            Label labelAccountID = (Label) trainerCard.lookup("#labelAccountID");
            labelAccountID.setText(trainer.getAccountID());

            Label labelGender = (Label) trainerCard.lookup("#labelGender");
            labelGender.setText(trainer.getGender());

            FlowPane flowPaneTeachingSubjects = (FlowPane) trainerCard.lookup("#flowPaneTeachingSubjects");

            ArrayList<String> teachingSubjects = trainer.getTeachingSubjects();
            for (String teachSubject : teachingSubjects) {
                Label label = new Label(teachSubject);
                label.setStyle("-fx-background-radius: 2em;\n" +
                        "-fx-padding: 6px;" +
                        "-fx-text-fill: WHITE;" +
                        "-fx-background-color: #6f60aa");
                flowPaneTeachingSubjects.getChildren().add(label);
            }

            trainerCard.setOnMouseClicked(mouseEvent -> {
                try {
                    Tab tab = this.createTrainerDetailTab(trainer);
                    this.tabPane.getTabs().add(tab);
                    this.tabPane.getSelectionModel().selectLast();
                    this.tabPane.requestFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            this.flowPaneTrainer.getChildren().add(trainerCard);
        }
    }

    private Tab createTrainerDetailTab(Trainer trainer) throws Exception {
        Tab tab = new Tab(trainer.getAccountID());

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/member/TrainerDetail.fxml"));
        Parent rootNode = fxmlLoader.load();
        rootNode.setUserData(trainer);
        TrainerDetailController trainerDetailController  = fxmlLoader.getController();
        trainerDetailController.loadDataUpdateUI();  // Invoke this method to load data only after initializing completes
        tab.setContent(rootNode);

        Button buttonBook = (Button) rootNode.lookup("#buttonBook");
        // The first membership type of gym.json MembershipTypes is non-premier-member
        if (this.curMember.getMembershipType().equals(this.gymDAO.getGym().getMembershipTypes().get(0))) {
            buttonBook.setDisable(true);
            buttonBook.setText(" For Premier Members Only");
        }
        return tab;
    }


    // ---------------------------------- Tab: Membership ----------------------------------

    private void loadMembershipData() {
        this.labelInviteeCnt.setText(String.valueOf(this.curMember.getInviteeCnt()));

        this.labelMembershipState.setText(this.curMember.getMembershipType());

        ArrayList<String> membershipTypes = this.gymDAO.getGym().getMembershipTypes();
        for (String membershipType : membershipTypes)
            this.choiceBoxOptionalMemberships.getItems().add(membershipType);
    }

    public void buttonChangeMembershipMouseClicked() throws Exception {     // Exception from accountDAO.updateAccount()
        this.labelWarningMembership.setText("");

        String chosenItem = this.choiceBoxOptionalMemberships.getValue();
        if (chosenItem == null) {
            this.labelWarningMembership.setText("Please choose one item.");
        }
        else {
            GUIScheduler guiScheduler = new GUIScheduler();
            guiScheduler.createNotificationWindow(this.choiceBoxOptionalMemberships.getScene().getWindow(), NotificationType.INFO, "Payment in progress");
            this.curMember.setMembershipType(chosenItem);
            this.accountDAO.updateAccount(this.curMember);
            Thread.sleep(1000);
            this.topBarController.loadDataUpdateUI(this.tabPane);
            guiScheduler.createNotificationWindow(this.choiceBoxOptionalMemberships.getScene().getWindow(), NotificationType.SUCCESS, "Change successful!");
        }
    }
}
