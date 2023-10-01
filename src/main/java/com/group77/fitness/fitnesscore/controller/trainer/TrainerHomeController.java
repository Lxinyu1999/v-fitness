package com.group77.fitness.fitnesscore.controller.trainer;

import com.group77.fitness.fitnesscore.controller.HomeCommon;
import com.group77.fitness.fitnesscore.controller.common.TopBarController;
import com.group77.fitness.fitnesscore.controller.common.TrainerProfileController;
import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.SessionDAO;
import com.group77.fitness.fitnesscore.dao.impl.AccountDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SessionDAOImpl;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Session;
import com.group77.fitness.fitnesscore.vo.Trainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Set;


public class TrainerHomeController{
    @FXML private TopBarController topBarController;
    @FXML private TrainerProfileController trainerProfileController;
    @FXML private VBox vBoxButtonTab;
    @FXML private TabPane tabPane;
    @FXML private TableView<Session> tableViewMySchedule;
    @FXML private TableView<Member> tableViewMemberList;

    private Trainer curTrainer;
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    public TrainerHomeController() throws Exception {}

    @FXML public void initialize() {
        new HomeCommon().setButtonTabMouseClickedEvents(this.tabPane, this.vBoxButtonTab.getChildren());
    }

    public void loadDataUpdateUI() {
        this.curTrainer = (Trainer) this.topBarController.loadDataUpdateUI(this.tabPane);
        this.trainerProfileController.loadDataUpdateUI("Edit My Profile");
        this.trainerProfileController.loadProfileData(this.curTrainer);

        this.loadMySchedule();
        this.loadStudents();
    }

    private void loadMySchedule() {
        ObservableList<Session> observableList = FXCollections.observableArrayList(this.sessionDAO.getSessionsByTrainerID(this.curTrainer.getAccountID()));

        TableColumn memberIDColumn = new TableColumn("Member ID");
        memberIDColumn.setCellValueFactory(new PropertyValueFactory<>("memberID"));

        TableColumn sessionDateColumn = new TableColumn("Session Date");
        sessionDateColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));

        TableColumn sessionIntervalColumn = new TableColumn("Session Interval");
        sessionIntervalColumn.setCellValueFactory(new PropertyValueFactory<>("sessionIntervalString"));

        TableColumn sessionSubjectColumn = new TableColumn("Session Subject");
        sessionSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("sessionSubject"));

        this.tableViewMySchedule.getColumns().clear();
        this.tableViewMySchedule.setItems(null);

        this.tableViewMySchedule.getColumns().addAll(memberIDColumn, sessionDateColumn, sessionIntervalColumn, sessionSubjectColumn);
        this.tableViewMySchedule.setItems(observableList);
    }

    private void loadStudents() {
        ObservableList<Member> observableList = FXCollections.observableArrayList();

        Set<String> studentsIDs = this.sessionDAO.getStudentIDsByTrainerID(this.curTrainer.getAccountID());
        for (String studentID : studentsIDs) {
            observableList.add((Member) this.accountDAO.queryAccountByID(studentID));
        }

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
}
