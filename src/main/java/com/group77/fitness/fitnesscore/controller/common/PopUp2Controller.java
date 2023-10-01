package com.group77.fitness.fitnesscore.controller.common;

import com.group77.fitness.fitnesscore.controller.GUIScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PopUp2Controller {
    @FXML
    private Button buttonYes;

    @FXML
    private Button buttonNo;

    public PopUp2Controller() {}

    @FXML
    public void initialize() {}

    @FXML
    public void buttonYesMouseClicked() throws InvocationTargetException, IllegalAccessException {
        Stage stage = (Stage) this.buttonYes.getScene().getWindow();
        Method method = (Method) stage.getUserData();   // Get the binding method
        method.invoke(new GUIScheduler());              // and invoke it. (The parameter is the instance where the method
                                                        // comes from. If the method is static, then the param can be the
                                                        // class or null)
    }

    @FXML
    public void buttonNoMouseClicked() {
        Stage stage = (Stage) this.buttonNo.getScene().getWindow();
        stage.close();
    }
}
