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

        // Check for empty username or password fields
        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Username and Password cannot be empty").show();
            return;
        }

        // Correct credentials check
        if (username.equals("Nandika") && password.equals("nandika4005")) {
            txtUsername.clear();  // Clear username field
            txtPassword.clear();  // Clear password field
            System.out.println("User is logged in");

            // Display loading screen
            FXMLLoader loadingScreenLoader = new FXMLLoader(getClass().getResource("/view/Loading.fxml"));
            AnchorPane loadingScreen = null;
            try {
                loadingScreen = loadingScreenLoader.load();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to load loading screen. Please try again.").show();
                e.printStackTrace();
                return; // Exit the method if loading screen fails
            }

            Stage stage = (Stage) anclogin.getScene().getWindow();
            stage.setScene(new Scene(loadingScreen));
            stage.show();

            // Create a background task to load the dashboard
            Task<Scene> loadingTask = new Task<>() {
                @Override
                protected Scene call() throws Exception {
                    // Load the dashboard layout from FXML
                    FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/view/Dashborad.fxml"));
                    AnchorPane dashboardRoot = null;
                    try {
                        dashboardRoot = dashboardLoader.load();
                    } catch (IOException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to load dashboard layout. Please try again.").show();
                        e.printStackTrace();
                        return null; // Return null if the loading fails
                    }
                    return new Scene(dashboardRoot);
                }
            };

            // Set the scene to the dashboard when loading is successful
            loadingTask.setOnSucceeded(event1 -> {
                Scene dashboardScene = loadingTask.getValue();
                if (dashboardScene != null) {
                    stage.setScene(dashboardScene);
                    stage.setTitle("Dashboard");
                    stage.show();
                }
            });

            // Handle loading failure
            loadingTask.setOnFailed(event1 -> {
                System.err.println("Failed to load the dashboard.");
                new Alert(Alert.AlertType.ERROR, "Failed to load Dashboard. Please try again.").show();
                stage.setScene(new Scene(anclogin)); // Optionally return to the login screen
            });

            // Start the loading task in a new thread
            new Thread(loadingTask).start();

        } else {
            txtUsername.clear();  // Optional: Clear fields even on failure (if desired)
            txtPassword.clear();  // Optional: Clear fields even on failure (if desired)
            new Alert(Alert.AlertType.ERROR, "Incorrect username or password").show();
        }
    }
}
