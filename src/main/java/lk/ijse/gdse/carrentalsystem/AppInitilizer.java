package lk.ijse.gdse.carrentalsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AppInitilizer extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent load = FXMLLoader.load(getClass().getResource("/view/Welcome.fxml"));
        Scene scene=new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SignIn Form");
        primaryStage.show();


    }
}
