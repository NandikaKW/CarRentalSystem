package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
            System.out.println("User is logged in");

            // Correct FXML file path
            try {
                AnchorPane load = FXMLLoader.load(getClass().getResource("/view/Dashborad.fxml"));
                Stage stage = (Stage) anclogin.getScene().getWindow();
                stage.setScene(new Scene(load));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load Dashboard: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Incorrect username or password").show();
        }
    }



}
