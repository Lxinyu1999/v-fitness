package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import com.group77.fitness.fitnesscore.dao.GymDAO;
import com.group77.fitness.fitnesscore.dao.RecordDAO;
import com.group77.fitness.fitnesscore.dao.SeriesDAO;
import com.group77.fitness.fitnesscore.dao.impl.GymDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.RecordDAOImpl;
import com.group77.fitness.fitnesscore.dao.impl.SeriesDAOImpl;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Record;
import com.group77.fitness.fitnesscore.vo.Video;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class VideoPlayerController {
    @FXML private VBox vBoxVideoPlayer;
    @FXML private Label labelVideoDisplayName;
    @FXML private Label labelSeriesProgress;
    @FXML private Label labelSeriesName;
    @FXML private ListView<String> listViewSeries;
    @FXML private Button buttonAddToLearningList;

    @FXML private MediaView mediaViewVideoPlayer;
    @FXML private Slider sliderTimeProgress;
    @FXML private HBox hBoxVideoControlBar;
    @FXML private Label labelTimeProgress;
    @FXML private Button buttonReset;
    @FXML private Button buttonPlay;
    @FXML private Slider sliderVolume;
    @FXML private Button buttonFullScreen;

    private Scene mainScene;
    private Stage mainStage;
    private boolean isFullScreen = false;
    private double mediaViewVideoPlayerFitHeight;
    private double mediaViewVideoPlayerFitWidth;

    private Member curMember;
    private Video curVideo;
    private final SeriesDAO seriesDAO = new SeriesDAOImpl();
    private final RecordDAO recordDAO = new RecordDAOImpl();
    private final GymDAO gymDAO = new GymDAOImpl();

    private Timeline timeline;

    public VideoPlayerController() throws Exception {}

    @FXML public void initialize() {}

    public void setCurMember(Member curMember) { this.curMember = curMember; }
    public void setCurVideo(Video video) {this.curVideo = video;}

    private boolean checkVideoPlayQualification() {
        ArrayList<String> membershipTypes = this.gymDAO.getGym().getMembershipTypes();
        int lowestRequirementIndex = membershipTypes.indexOf(this.curVideo.getLowestMembershipRequirement());
        int curMembershipIndex = membershipTypes.indexOf(this.curMember.getMembershipType());

        if (curMembershipIndex >= lowestRequirementIndex) {
            this.labelVideoDisplayName.setText(this.curVideo.getDisplayName());
            return true;
        }
        else {
            this.labelVideoDisplayName.setText(this.curVideo.getDisplayName() +
                    " - Your member level is not enough to watch this video.");
            this.mediaViewVideoPlayer.setMediaPlayer(null);
            return false;
        }
    }

    private String durationToString(Duration duration){
        int time = (int) duration.toSeconds();
        int hour = time /3600;
        int minute = (time-hour * 3600) / 60;
        int second = time % 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    private void loadMediaToMediaView(String mediaPath) throws Exception {    // IOException from this.recordDAO.updateRecord()
        Media media = new Media("file:///" + mediaPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        this.buttonPlay.setText("⏸");
        this.mediaViewVideoPlayer.setMediaPlayer(null);     // Clear the original Media if any
        this.mediaViewVideoPlayer.setMediaPlayer(mediaPlayer);

        // When the video is playing, update the process slider and process label
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Duration totalDuration = mediaPlayer.getTotalDuration();
            this.sliderTimeProgress.setValue(newValue.toSeconds() / totalDuration.toSeconds() * 100);
            this.labelTimeProgress.setText(this.durationToString(newValue) + " / " + this.durationToString(totalDuration));
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                mediaPlayer.pause();
                this.buttonPlay.setText("▶");
                this.watchedOver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        this.buttonPlay.setOnMouseClicked(mouseEvent ->  {
            if (this.buttonPlay.getText().equals("▶")) {
                // Start to play
                mediaPlayer.play();
                this.buttonPlay.setText("⏸");
            } else {
                // Stop playing
                mediaPlayer.pause();
                this.timeline.pause();
                this.buttonPlay.setText("▶");
            }
        });

        this.mediaViewVideoPlayer.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1)    // Take the same action as buttonPlay when the MediaView is clicked once
                this.buttonPlay.getOnMouseClicked().handle(mouseEvent);
            else if (mouseEvent.getClickCount() == 2)  // Take the same action as buttonFullScreenMouseClicked when the MediaView is clicked twice
                this.buttonFullScreenMouseClicked();
        });

        // When the processSlider is moved
        this.sliderTimeProgress.setOnMouseDragged(mouseEvent -> {
            mediaPlayer.seek(Duration.seconds(mediaPlayer.getTotalDuration().toSeconds() * this.sliderTimeProgress.getValue() / 100));
        });

        // When the processSlider is clicked
        this.sliderTimeProgress.setOnMouseClicked(mouseEvent -> {
            // Can't use this way, it may be invalid sometime, even though with the method .setOnMouseReleased()
            // mediaPlayer.seek(Duration.seconds(mediaPlayer.getTotalDuration().toSeconds() * this.sliderTimeProgress.getValue() / 100));

            // Change the position of sliderTimeProgress's to where the mouse is clicked
            this.sliderTimeProgress.setValue(mouseEvent.getX() / this.sliderTimeProgress.getWidth() * this.sliderTimeProgress.getMax());    // in percent
            // Change the video player's frame
            mediaPlayer.seek(Duration.seconds(mediaPlayer.getTotalDuration().toSeconds() * mouseEvent.getX() / this.sliderTimeProgress.getWidth()));
        });

        // Button to return the beginning
        this.buttonReset.setOnAction(e -> mediaPlayer.seek(Duration.ZERO));

        // Bind the slider to the volume (by percentage)
        mediaPlayer.volumeProperty().bind(this.sliderVolume.valueProperty().divide(100));

        // update the video watching records
        Record record = this.recordDAO.getRecordByAccountID(this.curMember.getAccountID());
        record.setLastWatchedVideoID(this.curVideo.getVideoID());
        this.recordDAO.updateRecord(record);

        // update the lastWatchedVideoProgress every 3 seconds
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent -> {
            Record tempRecord = this.recordDAO.getRecordByAccountID(this.curMember.getAccountID());
            tempRecord.setLastWatchedVideoProgress((int) mediaPlayer.getCurrentTime().toSeconds());
            try {
                this.recordDAO.updateRecord(tempRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    private void initSliderVolume() {
        // Slider to control the volume
        this.sliderVolume.setMinWidth(30);
        this.sliderVolume.setPrefWidth(150);
        this.sliderVolume.setValue(50);  // Set the slider at the half (max value is 100)
    }

    public Tab creatVideoPlayerTab() throws Exception {
        this.labelVideoDisplayName.setText(this.curVideo.getDisplayName());

        if (this.curVideo.getSeriesID().equals("")) {
            this.labelSeriesProgress.setText("None");
            this.buttonAddToLearningList.setDisable(true);
        }
        else {
            ArrayList<Video> seriesVideos = this.seriesDAO.getAllVideosByID(this.curVideo.getSeriesID());
            ObservableList<String> observableList = FXCollections.observableArrayList();
            int ordinal = 0;
            for (int i = 0; i < seriesVideos.size(); i++) {
                Video seriesVideo = seriesVideos.get(i);
                observableList.add(seriesVideo.getDisplayName());
                if (seriesVideo.getVideoID().equals(this.curVideo.getVideoID())) {
                    ordinal = i + 1;
                }
            }

            // if the current series has existed in the list
            if (this.recordDAO.getRecordByAccountID(this.curMember.getAccountID())
                    .getLearningSeriesIdList()
                    .contains(this.curVideo.getSeriesID())) {
                this.buttonAddToLearningList.setText("In My Learning List");
                this.buttonAddToLearningList.setDisable(true);
            }

            this.listViewSeries.setItems(observableList);
            this.listViewSeries.getSelectionModel().select(ordinal-1);

            this.labelSeriesName.setText(this.seriesDAO.querySeriesByID(this.curVideo.getSeriesID()).getSeriesName());
            this.labelSeriesProgress.setText(String.format("%d / %d", ordinal, seriesVideos.size()));

            this.listViewSeries.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> {
                if (!oldIndex.equals(newIndex)) {
                    try {
                        this.setCurVideo(seriesVideos.get(newIndex.intValue()));
                        if (checkVideoPlayQualification())
                            this.loadMediaToMediaView(curVideo.getVideoAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.labelSeriesProgress.setText(String.format("%d / %d", newIndex.intValue() + 1, seriesVideos.size()));
                }
            });
        }

        if (this.checkVideoPlayQualification())
            this.loadMediaToMediaView(this.curVideo.getVideoAbsolutePath());
        this.initSliderVolume();

        Tab tab = new Tab(this.curVideo.getDisplayName());
        tab.setContent(this.vBoxVideoPlayer.getParent());
        return tab;
    }

    @FXML public void buttonFullScreenMouseClicked() {
        if (!this.isFullScreen) {
            this.mainScene = this.vBoxVideoPlayer.getScene();
            this.mainStage = (Stage) this.mainScene.getWindow();
            this.mediaViewVideoPlayerFitHeight = this.mediaViewVideoPlayer.getFitHeight();
            this.mediaViewVideoPlayerFitWidth = this.mediaViewVideoPlayer.getFitWidth();

            Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
            this.mediaViewVideoPlayer.setFitWidth(screenRectangle.getWidth());
            this.mediaViewVideoPlayer.setFitHeight(screenRectangle.getHeight() - this.hBoxVideoControlBar.getHeight() - 20);

            AnchorPane anchorPane = new AnchorPane();
            VBox vBox = new VBox();

            vBox.setPrefHeight(screenRectangle.getHeight());
            vBox.setPrefWidth(screenRectangle.getWidth());
            vBox.setStyle("-fx-alignment: CENTER;-fx-spacing: 3px");

            // Add these 3 nodes to a new VBox will cause them removed from original vBoxVideoPlayer
            vBox.getChildren().addAll(this.mediaViewVideoPlayer, this.sliderTimeProgress, this.hBoxVideoControlBar);

            anchorPane.getChildren().add(vBox);
            Scene newScene = new Scene(anchorPane);
            this.mainStage.setScene(newScene);
            this.mainStage.setFullScreen(true);

            newScene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) this.returnToNormalFromFullScreen();
            });

            this.isFullScreen = true;
        }
        else {
            this.returnToNormalFromFullScreen();
        }
    }

    private void returnToNormalFromFullScreen() {
        this.mediaViewVideoPlayer.setFitHeight(this.mediaViewVideoPlayerFitHeight);
        this.mediaViewVideoPlayer.setFitWidth(this.mediaViewVideoPlayerFitWidth);
        this.vBoxVideoPlayer.getChildren().addAll(this.mediaViewVideoPlayer, this.sliderTimeProgress, this.hBoxVideoControlBar);
        this.mainStage.setScene(this.mainScene);
        this.isFullScreen = false;
        this.mediaViewVideoPlayer.getMediaPlayer().play();
    }

    @FXML public void buttonAddToLearningListMouseClicked() throws Exception {
        Record record = this.recordDAO.getRecordByAccountID(this.curMember.getAccountID());
        record.getLearningSeriesIdList().add(this.curVideo.getSeriesID());
        if (this.recordDAO.updateRecord(record)) {
            new GUIScheduler().createNotificationWindow(this.buttonAddToLearningList.getScene().getWindow(), NotificationType.SUCCESS, "Added successfully!");
        }
    }

    private void watchedOver() throws Exception {
        Record record = this.recordDAO.getRecordByAccountID(this.curMember.getAccountID());
        ArrayList<String> watchedOverVideoIdList = record.getWatchedOverVideoIdList();

        if (!watchedOverVideoIdList.contains(this.curVideo.getVideoID())) {
            watchedOverVideoIdList.add(this.curVideo.getVideoID());
            record.setWatchedOverVideoIdList(watchedOverVideoIdList);
            this.recordDAO.updateRecord(record);
        }
    }
}
