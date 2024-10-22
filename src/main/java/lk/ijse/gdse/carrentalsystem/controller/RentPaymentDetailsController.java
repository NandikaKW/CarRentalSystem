package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RentPaymentDetailsController {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnReport;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colPayamount;

    @FXML
    private TableColumn<?, ?> colPaymentDate;

    @FXML
    private TableColumn<?, ?> colPaymentID;

    @FXML
    private TableColumn<?, ?> colPaymentMethod;

    @FXML
    private TableColumn<?, ?> colRentId;

    @FXML
    private Label lblDiscription;

    @FXML
    private Label lblDuration;

    @FXML
    private Label lblPayamount;

    @FXML
    private Label lblPayemntDate;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblPaymentMethod;

    @FXML
    private Label lblRentID;

    @FXML
    private TableView<?> tblRentPayment;

    @FXML
    private TextField txtDiscription;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtPayamount;

    @FXML
    private TextField txtPaymentDate;

    @FXML
    private TextField txtPaymentId;

    @FXML
    private TextField txtPaymentMethod;

    @FXML
    private TextField txtRentId;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

    @FXML
    void tblRentPaymentOnClickedOnAction(MouseEvent event) {

    }

}
