package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private JFXButton btnSIgnIn;
    @FXML
    private AnchorPane ancPageOne;


    @FXML
    void btnSIgnInOnAction(ActionEvent event) throws IOException {
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/Dashborad.fxml"));
        Stage stage = (Stage) btnSIgnIn.getScene().getWindow();
        stage.setScene(new Scene(load));
        stage.show();
    }

}
