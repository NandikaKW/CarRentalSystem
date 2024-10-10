package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private JFXButton btnSIgnIn;
    @FXML
    private AnchorPane ancPageOne;


    @FXML
    void btnSIgnInOnAction(ActionEvent event) throws IOException {
        ancPageOne.getChildren().clear();
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/SignIn.fxml"));
        ancPageOne.getChildren().add(load);
    }

}
