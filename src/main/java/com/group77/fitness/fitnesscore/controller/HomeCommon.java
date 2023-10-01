package com.group77.fitness.fitnesscore.controller;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

public class HomeCommon {
    private static void clearUserTabs(int tabCnt, Object...args) {
        TabPane tabPane = (TabPane) args[0];

        MediaView mediaViewVideoPlayer = (MediaView) tabPane.lookup("#mediaViewVideoPlayer");
        if (mediaViewVideoPlayer != null) {
            MediaPlayer mediaPlayer = mediaViewVideoPlayer.getMediaPlayer();
            if (mediaPlayer != null) mediaPlayer.pause();  // Forced pause immediately
        }

        if (tabCnt < tabPane.getTabs().size()) {
            Iterator<Tab> iterator = tabPane.getTabs().iterator();

            for (int i = 0; i < tabCnt; i++) iterator.next();

            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
    }

    public void setButtonTabMouseClickedEvents(TabPane tabPane, ObservableList<Node> nodes) {
        this.setButtonTabMouseClickedEvents(tabPane, nodes, null);
    }

    public void setButtonTabMouseClickedEvents(TabPane tabPane, ObservableList<Node> nodes, String methodName, Object...args) {
        for (int i = 0; i < nodes.size() - 2; i++) {
            final int tabIndex = i;
            if (methodName != null) {
                if ("clearUserTabs".equals(methodName)) {
                    nodes.get(i).setOnMouseClicked(mouseEvent -> {
                        clearUserTabs(nodes.size() - 2, tabPane);    // -2 because Pane and Log Out are not a tab
                        tabPane.getSelectionModel().select(tabIndex);
                    });
                }
            }
            else {
                nodes.get(i).setOnMouseClicked(mouseEvent -> {
                    tabPane.getSelectionModel().select(tabIndex);
                });
            }
        }

        // Log Out for the last button
        nodes.get(nodes.size() - 1).setOnMouseClicked(mouseEvent -> {
            try {
                logOut(tabPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void logOut(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        new GUIScheduler().openSignInWindow();
    }
}
