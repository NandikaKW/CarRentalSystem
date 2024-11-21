package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
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
        // Display loading screen
        FXMLLoader loadingScreenLoader = new FXMLLoader(getClass().getResource("/view/Loading.fxml"));
        AnchorPane loadingScreen = loadingScreenLoader.load();
        Stage stage = (Stage) btnSIgnIn.getScene().getWindow();
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
            // Optionally, you can display an error message or return to the welcome screen
        });

        // Start the loading task in a new thread
        new Thread(loadingTask).start();
    }


}
