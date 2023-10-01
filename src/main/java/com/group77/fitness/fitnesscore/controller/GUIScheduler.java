package com.group77.fitness.fitnesscore.controller;

import com.group77.fitness.fitnesscore.controller.manager.ManagerHomeController;
import com.group77.fitness.fitnesscore.controller.member.MemberHomeController;
import com.group77.fitness.fitnesscore.controller.trainer.TrainerHomeController;
import com.group77.fitness.fitnesscore.util.NotificationType;
import com.group77.fitness.fitnesscore.vo.Account;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.lang.reflect.Method;


public class GUIScheduler {
    /**
     * @param tipText The text that will be displayed in the pop up window.
     * @param buttonYesTargetMethod A method that will be invoked while clicking the Yes button.
     * Example: {@code new GUIScheduler().createPopUp2("The displaying text",
     * GUIScheduler.class.getDeclaredMethod("openSignUp"));}
     * */
    @Deprecated
    public void createPopUp2(String tipText, Method buttonYesTargetMethod) throws IOException {
        Stage stage = new Stage();
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/fxml/common/PopUp2.fxml"));
        Text textTipText = (Text) anchorPane.lookup("#textTipText");
        textTipText.setText(tipText);
        stage.setUserData(buttonYesTargetMethod);     // Bind the target method on the stage
        stage.setScene(new Scene(anchorPane));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tips");
        stage.show();
    }

    public void openSignInWindow() throws IOException {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/fxml/common/Launch.fxml"));

        Scene scene = new Scene(rootNode);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void openSignUpWindow() throws IOException {
        Stage stage = new Stage();
        Parent rootNode = FXMLLoader.load(getClass().getResource("/fxml/member/MemberProfile.fxml"));
        stage.setScene(new Scene(rootNode));
        stage.setTitle("Sign up a new account");
        stage.show();
    }

    public void openMemberHomeWindow(Account account) throws Exception {
        Stage stage = new Stage();
        stage.setUserData(account);   // Record the signed in account to stage.userData

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/member/MemberHome.fxml"));
        Parent rootNode = fxmlLoader.load();

        stage.setScene(new Scene(rootNode));
        stage.setTitle("London Fitness - Member Home");
        stage.show();

        // Get the controller instance by FXMLLoader instance and invoke the controller's loadDataUpdateUI() to update UI.
        // This way is able to avoid invoking loadDataUpdateUI() in MemberHomeController's initialize() so that avoid accessing
        // the scene before initialing completes (this can cause NullPointerException because the scene is always null).
        MemberHomeController memberHomeController = fxmlLoader.getController();
        memberHomeController.loadDataUpdateUI();  // Invoke this method after the scene and stage have been initialized.

        this.createNotificationWindow(rootNode.getScene().getWindow(), NotificationType.SUCCESS, "Sign in successfully!");
    }

    public void openManagerHomeWindow(Account account) throws IOException {
        Stage stage = new Stage();
        stage.setUserData(account);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/manager/ManagerHome.fxml"));
        Parent rootNode = fxmlLoader.load();

        stage.setScene(new Scene(rootNode));
        stage.setTitle("London Fitness - Manager Home");
        stage.show();

        ManagerHomeController managerHomeController = fxmlLoader.getController();
        managerHomeController.loadDataUpdateUI();

        this.createNotificationWindow(rootNode.getScene().getWindow(), NotificationType.SUCCESS, "Sign in successfully!");
    }

    public void openTrainerHomeWindow(Account account) throws IOException {
        Stage stage = new Stage();
        stage.setUserData(account);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/trainer/TrainerHome.fxml"));
        Parent rootNode = fxmlLoader.load();

        stage.setScene(new Scene(rootNode));
        stage.setTitle("London Fitness - Trainer Home");
        stage.show();

        TrainerHomeController trainerHomeController = fxmlLoader.getController();
        trainerHomeController.loadDataUpdateUI();

        this.createNotificationWindow(rootNode.getScene().getWindow(), NotificationType.SUCCESS, "Sign in successfully!");
    }

    public void createNotificationWindow(Window parentWindow, NotificationType type, String content) throws IOException {
        Stage stage = new Stage();

        HBox rootHBox = FXMLLoader.load(getClass().getResource("/fxml/common/Notification.fxml"));
        Label labelIcon = (Label) rootHBox.lookup("#labelIcon");
        Label labelContent = (Label) rootHBox.lookup("#labelContent");
        labelContent.setText(content);
        switch (type) {
            case INFO: {
                labelIcon.setGraphic(new FontIcon("fa-info-circle:30:BLACK"));
                labelContent.setTextFill(Paint.valueOf("BLACK"));
                break;
            }
            case SUCCESS: {
                labelIcon.setGraphic(new FontIcon("fa-check-circle-o:30:GREEN"));
                labelContent.setTextFill(Paint.valueOf("GREEN"));
                break;
            }
            case WARNING: {
                labelIcon.setGraphic(new FontIcon("fa-warning:30:ORANGE"));
                labelContent.setTextFill(Paint.valueOf("ORANGE"));
                break;
            }
            case ERROR: {
                labelIcon.setGraphic(new FontIcon("fa-times-circle-o:30:RED"));
                labelContent.setTextFill(Paint.valueOf("RED"));
                break;
            }
        }

        // Timing operation to close the notification stage after a period of time
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(actionEvent -> stage.close());
        pauseTransition.play();

        stage.setScene(new Scene(rootHBox));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);

        stage.setX(parentWindow.getX() + parentWindow.getWidth() / 2 - rootHBox.getPrefWidth() / 2);
        stage.setY(parentWindow.getY() + 20);
        stage.show();

        parentWindow.requestFocus();       // get the focus from the notification window
    }
}
