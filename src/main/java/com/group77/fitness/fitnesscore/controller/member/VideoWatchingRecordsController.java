package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.RecordDAO;
import com.group77.fitness.fitnesscore.dao.SeriesDAO;
import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.dao.impl.RecordDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SeriesDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.VideoDAOImpl;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Record;
import com.group77.fitness.fitnesscore.vo.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class VideoWatchingRecordsController {
    @FXML private VBox vBoxStartFrom;
    @FXML private Label labelTimeProgress;
    @FXML private ListView<String> listViewLearningSeries;
    @FXML private Button buttonRemove;

    private String accountID;
    private final RecordDAO recordDAO = new RecordDAOImpl();
    private final VideoDAO videoDAO = new VideoDAOImpl();
    private final SeriesDAO seriesDAO = new SeriesDAOImpl();

    public VideoWatchingRecordsController() throws Exception {}
    @FXML public void initialize() {}

    public void loadDataUpdateUI(Member member, TabPane tabPane) throws IOException {
        this.accountID = member.getAccountID();
        Record record = this.recordDAO.getRecordByAccountID(this.accountID);

        ObservableList<Node> vBoxStartFromNodes = this.vBoxStartFrom.getChildren();

        if (record.getLastWatchedVideoID().equals("")) {
            if (vBoxStartFromNodes.size() == 2) {
                vBoxStartFromNodes.add(new Label("Not yet"));
            }
            return;
        }

        Video video = this.videoDAO.queryVideoByID(record.getLastWatchedVideoID());

        // load the progress
        this.labelTimeProgress.setText(String.format("Watched to %d seconds", record.getLastWatchedVideoProgress()));

        // load the Video Card
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/member/VideoCard.fxml"));
        fxmlLoader.load();
        VideoCardController videoCardController = fxmlLoader.getController();

        if (vBoxStartFromNodes.size() == 3) {
            vBoxStartFromNodes.set(2, videoCardController.createVideoCard(video, member, tabPane));
        }
        else {
            vBoxStartFromNodes.add(videoCardController.createVideoCard(video, member, tabPane));
        }

        // load the learning series
        ObservableList<String> learningSeries = FXCollections.observableArrayList();
        for (String seriesId: record.getLearningSeriesIdList()) {
            learningSeries.add(this.seriesDAO.querySeriesByID(seriesId).getSeriesName());
        }
        this.listViewLearningSeries.setItems(learningSeries);
        this.listViewLearningSeries.setOnMouseClicked(mouseEvent -> {
            this.buttonRemove.setDisable(false);
        });
    }

    @FXML public void buttonRemove() throws Exception {
        int selectedIndex = this.listViewLearningSeries.getSelectionModel().getSelectedIndex();

        Record record = this.recordDAO.getRecordByAccountID(this.accountID);
        ArrayList<String> learningSeriesIdList = record.getLearningSeriesIdList();
        learningSeriesIdList.remove(selectedIndex);
        record.setLearningSeriesIdList(learningSeriesIdList);
        if (this.recordDAO.updateRecord(record)) {
            new GUIScheduler().createNotificationWindow(this.listViewLearningSeries.getScene().getWindow(), NotificationType.SUCCESS, "Delete successfully!");
        }
        this.listViewLearningSeries.getItems().remove(selectedIndex);
    }
}
