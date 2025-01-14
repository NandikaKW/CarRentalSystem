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
import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleTM;
import lk.ijse.gdse.carrentalsystem.model.VehicleRentDetailModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleRentDetailTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class VehicleRentDetailsController  implements Initializable {
    @FXML
    private Label lblVehicleQuantity;
    @FXML
    private TextField txtVehicleQuantity;
    @FXML
    private TableColumn<VehicleRentDetailTM, Integer> colVehicleQuantity;

    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> CombMonthOne;

    @FXML
    private ComboBox<Integer> CombMonthTwo;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<Integer> ComboDayOne;

    @FXML
    private ComboBox<Integer> ComboDayTwo;

    @FXML
    private ComboBox<Integer> ComboYear;

    @FXML
    private ComboBox<Integer> ComboYearOne;

    @FXML
    private ComboBox<Integer> ComboYearTwo;

    @FXML
    private JFXButton btnDelete;



    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnSave;





    @FXML
    private TableColumn<VehicleRentDetailTM, String> colCondition;

    @FXML
    private TableColumn<VehicleRentDetailTM, BigDecimal> colCost;

    @FXML
    private TableColumn<VehicleRentDetailTM, Date> colEndDate;

    @FXML
    private TableColumn<VehicleRentDetailTM, Date> colRentDate;

    @FXML
    private TableColumn<VehicleRentDetailTM, String> colRentID;

    @FXML
    private TableColumn<VehicleRentDetailTM, Date> colStartDate;

    @FXML
    private TableColumn<VehicleRentDetailTM, String> colVehicleID;

    @FXML
    private Label lblCondition;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblEndDate;



    @FXML
    private Label lblRentID;

    @FXML
    private Label lblStartDate;

    @FXML
    private Label lblVehicleID;

    @FXML
    private TableView<VehicleRentDetailTM> tblVehileRent;

    @FXML
    private TextField txtCondition;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtRentId;

    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtVehicleID;
    @FXML
    public void initialize() {
        ComboYear.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYear.getSelectionModel().selectLast();

        ComboYearOne.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYearOne.getSelectionModel().selectLast();




        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();

        CombMonthOne.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonthOne.getSelectionModel().selectFirst();



        updateDays();
        updateDaysOne();

    }


    @FXML
    void ComboDayOnAction(ActionEvent event) {
        showSelectedDate();


    }

    @FXML
    void ComboDayOneOnAction(ActionEvent event) {
        showSelectedDateOne();


    }


    @FXML
    void ComboMonthOnAction(ActionEvent event) {
        updateDays();


    }

    @FXML
    void ComboMonthOneOnAction(ActionEvent event) {
        updateDaysOne();

    }



    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();

    }

    @FXML
    void ComboYearOneOnAction(ActionEvent event) {
        updateDaysOne();

    }


    private void updateDays() {
        Integer year = ComboYear.getValue();
        Integer month = CombMonth.getValue();

        if (year != null && month != null) {
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            ComboDay.setItems(FXCollections.observableArrayList(
                    IntStream.rangeClosed(1, daysInMonth).boxed().toList()
            ));
            ComboDay.getSelectionModel().selectFirst();
            showSelectedDate();
        }
    }
    private void showSelectedDate() {
        Integer year = ComboYear.getValue();
        Integer month = CombMonth.getValue();
        Integer day = ComboDay.getValue();

        if (year != null && month != null && day != null) {
            txtStartDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }
    }

    private void updateDaysOne() {
        Integer year = ComboYearOne.getValue();
        Integer month = CombMonthOne.getValue();

        if (year != null && month != null) {
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            ComboDayOne.setItems(FXCollections.observableArrayList(
                    IntStream.rangeClosed(1, daysInMonth).boxed().toList()
            ));
            ComboDayOne.getSelectionModel().selectFirst();
            showSelectedDateOne();
        }
    }
    private void showSelectedDateOne() {
        Integer year = ComboYearOne.getValue();
        Integer month = CombMonthOne.getValue();
        Integer day = ComboDayOne.getValue();

        if (year != null && month != null && day != null) {
            txtEndDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String vehicleId = txtVehicleID.getText();
        String rentId = txtRentId.getText();

        // Check if required fields are provided
        if (rentId.isEmpty() || vehicleId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Vehicle Id and Rent Id").show();
            return;
        }

        // Show confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle rent?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Attempt to delete the vehicle rent entry
                boolean isDeleted = VehicleRentDetailModel.deleteVehicleRent(rentId, vehicleId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Vehicle Rent Deleted Successfully").show();
                    clearFields();
                    loadCurrentVehicleId();
                    loadCurrentRentId();
                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed To Delete Vehicle Rent").show();
                }

            } catch (SQLException e) {
                // Handle database-related exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error while deleting vehicle rent: " + e.getMessage()).show();

            } catch (ClassNotFoundException e) {
                // Handle missing class or other setup issues
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Configuration error: Class not found. Please contact support.").show();

            } catch (Exception e) {
                // Handle any other unforeseen exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
            }
        }

    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        // Retrieve all vehicle rent details from the model
        ArrayList<VechileRentDetailDto> vechileRentDetailDtos = VehicleRentDetailModel.getAllVehicleRent();

        // Create an observable list for table items
        ObservableList<VehicleRentDetailTM> vehicleRentDetailTMS = FXCollections.observableArrayList();

        // Iterate over the DTO list and map to the corresponding table model
        for (VechileRentDetailDto vechileRentDetailDto : vechileRentDetailDtos) {
            // Create a new VehicleRentDetailTM with the updated fields
            VehicleRentDetailTM vehicleRentDetailTM = new VehicleRentDetailTM(
                    vechileRentDetailDto.getVehicle_id(),
                    vechileRentDetailDto.getRent_id(),
                    vechileRentDetailDto.getStart_date(),
                    vechileRentDetailDto.getEnd_date(),
                    vechileRentDetailDto.getVehicle_condition(),  // Updated field
                    vechileRentDetailDto.getVehicle_quantity()
            );

            // Add the TM object to the observable list
            vehicleRentDetailTMS.add(vehicleRentDetailTM);
        }

        // Set the observable list as the items for the table view
        tblVehileRent.setItems(vehicleRentDetailTMS);
    }

    private  void clearFields(){
        txtVehicleID.clear();
        txtRentId.clear();
        txtStartDate.clear();
        txtEndDate.clear();
        txtCondition.clear();
        txtVehicleQuantity.clear();
    }



    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadCurrentRentId();
        loadCurrentVehicleId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String vehicleId = txtVehicleID.getText();
        String rentId = txtRentId.getText();

        // Validate mandatory fields
        if (vehicleId.isEmpty() || rentId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter both Vehicle ID and Rent ID.").show();
            return;
        }

        java.sql.Date startDate, endDate, rentDate;
        try {
            // Parse dates and handle invalid format
            startDate = java.sql.Date.valueOf(txtStartDate.getText());
            endDate = java.sql.Date.valueOf(txtEndDate.getText());

        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-DD format.").show();
            return;
        }

        String vehicleCondition = txtCondition.getText();
        Integer quantity;
        try {
            // Parse quantity and handle non-numeric input
            quantity = Integer.parseInt(txtVehicleQuantity.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity. Please enter a numeric value.").show();
            return;
        }

        // Create DTO with validated inputs
        VechileRentDetailDto vehicleRentDetailDto = new VechileRentDetailDto(
                vehicleId, rentId, startDate, endDate,  quantity, vehicleCondition
        );

        try {
            boolean isSaved = VehicleRentDetailModel.saveVehicleRent(vehicleRentDetailDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Rent Saved Successfully").show();
                refreshPage();
                refreshTableData();
                loadCurrentRentId();
                loadCurrentVehicleId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save vehicle rent.").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error occurred while saving vehicle rent: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "System error: Required class not found.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }
    }




    @FXML
    void tblVehicleRentClickedOnAction(MouseEvent event) {
        VehicleRentDetailTM vehicleRentDetailTM=tblVehileRent.getSelectionModel().getSelectedItem();
        if (vehicleRentDetailTM!=null){
            txtVehicleID.setText(vehicleRentDetailTM.getVehicle_id());
            txtRentId.setText(vehicleRentDetailTM.getRent_id());
            txtStartDate.setText(vehicleRentDetailTM.getStart_date().toString());
            txtEndDate.setText(vehicleRentDetailTM.getEnd_date().toString());
            txtCondition.setText(vehicleRentDetailTM.getVehicle_condition());
            txtVehicleQuantity.setText(String.valueOf(vehicleRentDetailTM.getVehicle_quantity()));
            btnSave.setDisable(true);
            btnDelete.setDisable(false);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
            colRentID.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
            colEndDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
            colCondition.setCellValueFactory(new PropertyValueFactory<>("vehicle_condition"));
            colVehicleQuantity.setCellValueFactory(new PropertyValueFactory<>("vehicle_quantity"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error setting up table columns: " + e.getMessage()).show();
        }

        try {
            initialize(); // Ensure that this does not conflict with FXMLLoader's initialize method
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error initializing the controller: " + e.getMessage()).show();
        }

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error refreshing the page: " + e.getMessage()).show();
        }

        try {
            loadCurrentVehicleId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading the current vehicle ID: " + e.getMessage()).show();
        }

        try {
            loadCurrentRentId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading the current rent ID: " + e.getMessage()).show();
        }

        try {
            refreshTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error refreshing table data: " + e.getMessage()).show();
        }
    }
    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadTableData();
        loadCurrentVehicleId();
        loadCurrentRentId();
        btnSave.setDisable(false);
        btnDelete.setDisable(true);
       clearFields();

    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<VechileRentDetailDto> vechileRentDetailDtos=VehicleRentDetailModel.getAllVehicleRent();
        ObservableList<VehicleRentDetailTM> vehicleRentDetailTMS=FXCollections.observableArrayList();
        for (VechileRentDetailDto vechileRentDetailDto:vechileRentDetailDtos){
            VehicleRentDetailTM vehicleRentDetailTM=new VehicleRentDetailTM(
                    vechileRentDetailDto.getVehicle_id(),
                    vechileRentDetailDto.getRent_id(),
                    vechileRentDetailDto.getStart_date(),
                    vechileRentDetailDto.getEnd_date(),
                    vechileRentDetailDto.getVehicle_condition(),
                    vechileRentDetailDto.getVehicle_quantity()

            );
            vehicleRentDetailTMS.add(vehicleRentDetailTM);
        }
        tblVehileRent.setItems(vehicleRentDetailTMS);


    }
    public void loadCurrentVehicleId() throws SQLException, ClassNotFoundException {
        String currentVehicleId = VehicleRentDetailModel.loadCurrentVehicleId();
        txtVehicleID.setText(currentVehicleId);
    }

    public void loadCurrentRentId() throws SQLException, ClassNotFoundException {
        String currentRentId = VehicleRentDetailModel.loadCurrentRentId();
        txtRentId.setText(currentRentId);
    }

}
