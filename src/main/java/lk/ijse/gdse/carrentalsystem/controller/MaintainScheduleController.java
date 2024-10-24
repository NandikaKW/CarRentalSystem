package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse.carrentalsystem.dto.MaintainDto;
import lk.ijse.gdse.carrentalsystem.tm.MaintainTM;
import lk.ijse.gdse.carrentalsystem.model.MaintinModel;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaintainScheduleController  implements Initializable {

    public TextField txtDescription;
    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnReport;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<MaintainTM, BigDecimal> colCost;

    @FXML
    private TableColumn<MaintainTM, String> colDescription;

    @FXML
    private TableColumn<MaintainTM, String> colDuration;

    @FXML
    private TableColumn<MaintainTM, Date> colMaintainDate;

    @FXML
    private TableColumn<MaintainTM, String> colMaintainID;

    @FXML
    private TableColumn<MaintainTM, String> colVechleId;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblDiscription;

    @FXML
    private Label lblDuration;

    @FXML
    private Label lblMaintainDate;

    @FXML
    private Label lblMaintainID;

    @FXML
    private Label lblVehicleID;

    @FXML
    private TableView<MaintainTM> tblMaintain;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtMaintainDate;

    @FXML
    private TextField txtMaintainID;

    @FXML
    private TextField txtVehicleID;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String maintainId = txtMaintainID.getText();
        if(maintainId.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter Maintain Id").show();
            return;
        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this Maintain?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
                boolean isDeleted=MaintinModel.deleteMaintain(maintainId);
                if(isDeleted){
                    new Alert(Alert.AlertType.INFORMATION, "Maintain deleted successfully!").show();
                    clearFields();
                    loadNextVehicleId();
                    loadNextMaintainId();
                    refreshTableData();

                }else{
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Maintain!").show();
                }

            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting Maintain: "+e.getMessage()).show();

            }

        }

    }
    private  void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<MaintainDto> maintainDtos=MaintinModel.getAllMaintain();
        ObservableList<MaintainTM> maintainTMS = FXCollections.observableArrayList();
        for (MaintainDto dto:maintainDtos){
            MaintainTM maintainTM=new MaintainTM(
                    dto.getMaintain_id(),
                    dto.getCost(),
                    dto.getMaintain_date(),
                    dto.getDescription(),
                    dto.getDuration(),
                    dto.getVehicle_id()
            );
            maintainTMS.add(maintainTM);

        }
        tblMaintain.setItems(maintainTMS);
    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        clearFields();
        loadNextVehicleId();
        loadNextMaintainId();

    }
    private void clearFields(){
        txtMaintainID.setText("");
        txtCost.setText("");
        txtDuration.setText("");
        txtMaintainDate.setText("");
        txtDescription.setText("");
        txtVehicleID.setText("");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            String maintainId = txtMaintainID.getText();
            String cost = txtCost.getText();
            String maintainDateStr = txtMaintainDate.getText();
            String description = txtDescription.getText();
            String duration = txtDuration.getText();
            String vehicleId = txtVehicleID.getText();

            // Validate inputs
            if (maintainId.isEmpty() || cost.isEmpty() || maintainDateStr.isEmpty() || duration.isEmpty() || vehicleId.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
                return; // Exit if validation fails
            }

            // Convert cost to BigDecimal
            BigDecimal decimalCost;
            try {
                decimalCost = new BigDecimal(cost);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid cost format!").show();
                return; // Exit if cost format is invalid
            }

            // Parse maintainDate from String to Date
            java.util.Date maintainDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust to your date format
                maintainDate = dateFormat.parse(maintainDateStr);
            } catch (ParseException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format! Use YYYY-MM-DD.").show();
                return; // Exit if date format is invalid
            }

            // Create MaintainDto object
            MaintainDto maintainDto = new MaintainDto(maintainId, decimalCost, maintainDate, description, duration, vehicleId);

            // Save to the database
            boolean isSaved = MaintinModel.saveMaintain(maintainDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Maintain saved successfully!").show();
                loadNextMaintainId();
                loadNextVehicleId();
                refreshTableData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save Maintain!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database connection error: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String maintainId = txtMaintainID.getText(); // Get the user input, not the component ID
        if(maintainId.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter Maintain Id").show();
            return;
        }

        try {
            MaintainDto maintain = MaintinModel.searchMaintain(maintainId); // Correct model method
            if (maintain != null) {
                txtMaintainID.setText(maintain.getMaintain_id());
                txtCost.setText(maintain.getCost().toString());
                txtMaintainDate.setText(maintain.getMaintain_date().toString());
                txtDescription.setText(maintain.getDescription());
                txtDuration.setText(maintain.getDuration());
                txtVehicleID.setText(maintain.getVehicle_id());
            } else {
                new Alert(Alert.AlertType.ERROR, "Maintain not found").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching Maintain: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String maintainId = txtMaintainID.getText();
            String costStr = txtCost.getText();
            String maintainDateStr = txtMaintainDate.getText();
            String description = txtDescription.getText();
            String duration = txtDuration.getText();
            String vehicleId = txtVehicleID.getText();

            // Validate inputs
            if (maintainId.isEmpty() || costStr.isEmpty() || maintainDateStr.isEmpty() || duration.isEmpty() || vehicleId.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
                return; // Exit if validation fails
            }

            // Convert cost to BigDecimal
            BigDecimal cost;
            try {
                cost = new BigDecimal(costStr);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid cost format!").show();
                return; // Exit if cost format is invalid
            }

            // Parse maintainDate from String to Date
            java.util.Date maintainDate;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust according to your date format
                maintainDate = dateFormat.parse(maintainDateStr);
            } catch (ParseException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format! Use YYYY-MM-DD.").show();
                return; // Exit if date format is invalid
            }

            // Create MaintainDto object
            MaintainDto maintainDto = new MaintainDto(maintainId, cost, maintainDate, description, duration, vehicleId);

            // Update the database
            boolean isUpdated = MaintinModel.updateMaintain(maintainDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Maintain updated successfully!").show();
                clearFields();
                loadNextMaintainId();
                loadNextVehicleId();
                refreshTableData();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Maintain!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database connection error: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            e.printStackTrace(); // Log for debugging
        }
    }

    @FXML
    void tblMaintainOnClick(MouseEvent event) {
        MaintainTM selectedItem = tblMaintain.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            txtMaintainID.setText(selectedItem.getMaintain_id());
            txtCost.setText(selectedItem.getCost().toString());
            txtMaintainDate.setText(selectedItem.getMaintain_date().toString());
            txtDescription.setText(selectedItem.getDescription());
            txtDuration.setText(selectedItem.getDuration());
            txtVehicleID.setText(selectedItem.getVehicle_id());

        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colMaintainID.setCellValueFactory(new PropertyValueFactory<>("maintain_id"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colMaintainDate.setCellValueFactory(new PropertyValueFactory<>("maintain_date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colVechleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));

       try{
           loadNextMaintainId();
           loadNextVehicleId();
           refreshTableData();

       } catch (SQLException | ClassNotFoundException e){
           e.printStackTrace();
           new Alert(Alert.AlertType.ERROR,"Error occurred while loading Maintain: "+e.getMessage()).show();

       }
    }
    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextMaintainId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();

    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        // Clear the existing items before loading new data
        tblMaintain.getItems().clear();

        // Load data from the database
        ArrayList<MaintainDto> maintainDtos = MaintinModel.getAllMaintain();
        ObservableList<MaintainTM> maintainTMS = FXCollections.observableArrayList();

        for (MaintainDto dto : maintainDtos) {
            // Convert maintain_date to java.util.Date
            Date maintainDate = new Date(dto.getMaintain_date().getTime()); // Convert if needed
            MaintainTM maintainTM = new MaintainTM(
                    dto.getMaintain_id(),
                    dto.getCost(),
                    maintainDate,
                    dto.getDescription(),
                    dto.getDuration(),
                    dto.getVehicle_id()
            );
            maintainTMS.add(maintainTM);
        }

        // Set the items in the table
        tblMaintain.setItems(maintainTMS);
    }

    public  void loadNextMaintainId() throws SQLException, ClassNotFoundException {
        String nextId=MaintinModel.loadNextMaintainId();
        txtMaintainID.setText(nextId);
    }
    public void loadNextVehicleId() throws SQLException, ClassNotFoundException {
        String nextId=MaintinModel.loadNextVehicleId();
        txtVehicleID.setText(nextId);
    }


}
