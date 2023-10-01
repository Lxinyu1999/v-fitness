package com.group77.fitness.fitnesscore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import  javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;


public class GUILoader extends Application{
    private double xOffset, yOffset;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        GUILoader.checkDataDirectory();

        Parent rootNode = FXMLLoader.load(getClass().getResource("/fxml/common/Launch.fxml"));

        Scene scene = new Scene(rootNode);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image("/imgs/icon.ico"));
        stage.show();

        scene.setOnMousePressed(mouseEvent -> {
            this.xOffset = mouseEvent.getSceneX();
            this.yOffset = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - this.xOffset);
            stage.setY(mouseEvent.getScreenY() - this.yOffset);
        });
    }

    private static void checkDataDirectory() {
        if (!new File(System.getProperty("user.dir") + "/London-Fitness/").exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Serious Warning");
            alert.setHeaderText("Data Directory \"London-Fitness\" NOT FOUND, this program cannot work properly.");
            alert.setContentText("Please contact jp2018212955@qmul.ac.uk.");
            alert.showAndWait();
            Platform.exit();
            System.exit(-1);
        }
    }
}
