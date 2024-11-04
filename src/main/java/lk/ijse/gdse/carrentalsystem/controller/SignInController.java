package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
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

            // Display loading screen
            FXMLLoader loadingScreenLoader = new FXMLLoader(getClass().getResource("/view/Loading.fxml"));
            AnchorPane loadingScreen = loadingScreenLoader.load();
            Stage stage = (Stage) anclogin.getScene().getWindow();
            stage.setScene(new Scene(loadingScreen));
            stage.show();

            // Create a background task to load the dashboard
            Task<Scene> loadingTask = new Task<>() {
                @Override
                protected Scene call() throws Exception {
                    // Load the dashboard layout from FXML
                    FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/view/Dashborad.fxml"));
                    AnchorPane dashboardRoot = dashboardLoader.load();
                    return new Scene(dashboardRoot);
                }
            };

            // Set the scene to the dashboard when loading is successful
            loadingTask.setOnSucceeded(event1 -> {
                stage.setScene(loadingTask.getValue());
                stage.setTitle("Dashboard");
                stage.show();
            });

            // Handle loading failure (optional)
            loadingTask.setOnFailed(event1 -> {
                System.err.println("Failed to load the dashboard.");
                new Alert(Alert.AlertType.ERROR, "Failed to load Dashboard.").show();
                // Optionally, you can return to the login screen or show an error screen
                stage.setScene(new Scene(anclogin));
            });

            // Start the loading task in a new thread
            new Thread(loadingTask).start();

        } else {
            new Alert(Alert.AlertType.ERROR, "Incorrect username or password").show();
        }
    }
}
