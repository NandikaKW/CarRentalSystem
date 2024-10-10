package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignInController {
    @FXML
    public JFXTextField txtSample;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;


    @FXML
    private AnchorPane anclogin;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals("Nandika") && password.equals("nandika4005")) {
            anclogin.getChildren().clear();
            anclogin.getChildren().add(FXMLLoader.load(getClass().getResource("/view/Dashborad.fxml"))
            );

        } else {
            new Alert(Alert.AlertType.ERROR, "Something Wrong").show();
        }

    }


}
