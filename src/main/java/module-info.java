module lk.ijse.gdse.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires com.jfoenix;
    requires java.desktop;

    opens  lk.ijse.gdse.carrentalsystem.tm to javafx.base;//importnat
    opens lk.ijse.gdse.carrentalsystem to javafx.fxml;
    exports lk.ijse.gdse.carrentalsystem;
//    exports lk.ijse.gdse.carrentalsystem.controller;
    opens lk.ijse.gdse.carrentalsystem.controller to javafx.fxml;
    opens lk.ijse.gdse.carrentalsystem.dto to javafx.base;

}