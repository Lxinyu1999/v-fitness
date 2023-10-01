package com.group77.fitness.fitnesscore.controller.member;

import com.group77.fitness.fitnesscore.vo.Member;
import com.group77.fitness.fitnesscore.vo.Video;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class VideoCardController {
    @FXML private AnchorPane videoCard;
    @FXML private ImageView imageViewCover;
    @FXML private Label labelDisplayName;
    @FXML private Label labelCategory;
    @FXML private Label labelLevel;

    public VideoCardController() {}

    @FXML public void initialize() {}

    public Parent createVideoCard(Video video, Member member, TabPane tabPane) {
        this.imageViewCover.setImage(new Image("file:///" + video.getCoverAbsolutePath()));
        this.labelDisplayName.setText(video.getDisplayName());
        this.labelCategory.setText(video.getCategory());
        this.labelLevel.setText(video.getLevel());

        videoCard.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/member/VideoPlayer.fxml"));
            try {
                fxmlLoader.load();    // Must invoke load() before getting the Controller
            } catch (IOException e) {
                e.printStackTrace();
            }

            VideoPlayerController videoPlayerController = fxmlLoader.getController();
            videoPlayerController.setCurMember(member);
            videoPlayerController.setCurVideo(video);
            try {
                Tab tab = videoPlayerController.creatVideoPlayerTab();
                tabPane.getTabs().add(tab);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tabPane.getSelectionModel().selectLast();
            tabPane.requestFocus();    // If don't get the focus actively, the tab header will reveal a little bit for some unknown reason
        });

        return this.videoCard;
    }
}
