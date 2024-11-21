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
import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.dto.tm.ReserveTM;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleRentDetailTM;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.model.RentModel;
import lk.ijse.gdse.carrentalsystem.model.VehicleModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class VehicleDetailsController  implements Initializable {
    @FXML
    private JFXButton btnReturnReport;


    @FXML
    private JFXButton btnDelete;



    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<VehicleTM, String> colCategory;

    @FXML
    private TableColumn<VehicleTM, String> colColour;

    @FXML
    private TableColumn<VehicleTM, String> colModel;

    @FXML
    private TableColumn<VehicleTM, String> colPackageId;

    @FXML
    private TableColumn<VehicleTM, Integer> colQuantity;

    @FXML
    private TableColumn<VehicleTM, String> colVehicleId;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblColour;

    @FXML
    private Label lblModel;

    @FXML
    private Label lblPackageId;

    @FXML
    private Label lblQuantity;

    @FXML
    private Label lblVehicleId;

    @FXML
    private TableView<VehicleTM> tblVehicle;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtColour;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtPackageId;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtVehicleId;
    private final RentModel rentModel=new RentModel();
    private final CustomerModel customerModel=new CustomerModel();
    private  VehicleModel vehicleModel;

    private final ObservableList<ReserveTM> reserveTMS = FXCollections.observableArrayList();

    public VehicleDetailsController() {
        vehicleModel = new VehicleModel();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId = txtVehicleId.getText();

        // Validate vehicle ID input
        if (vehicleId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Vehicle Id").show();
            return;  // Exit early if vehicle ID is not provided
        }

        // Ask for confirmation before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        // Proceed if user clicks "Yes"
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Attempt to delete the vehicle from the model
                boolean isDeleted = vehicleModel.deleteVehicle(vehicleId);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Vehicle Deleted Successfully").show();
                    // Clear fields and refresh data after successful deletion
                    clearFields();
                    loadNextVehicleId();
                    loadCurrentPackageId();
                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed To Delete Vehicle").show();
                }

            } catch (SQLException e) {
                // Handle SQL-related exceptions
                e.printStackTrace();  // Log the exception for debugging
                new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Handle ClassNotFound exceptions (e.g., if a required class is missing)
                e.printStackTrace();  // Log the exception for debugging
                new Alert(Alert.AlertType.ERROR, "Class Not Found Error: " + e.getMessage()).show();
            } catch (Exception e) {
                // Catch any unexpected exceptions
                e.printStackTrace();  // Log the exception for debugging
                new Alert(Alert.AlertType.ERROR, "Unexpected Error: " + e.getMessage()).show();
            }
        }
    }
    private  void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<VehicleDto> vehicleDtos=vehicleModel.getAllVehicles();
        ObservableList<VehicleTM> vehicleTMS= FXCollections.observableArrayList();
        for(VehicleDto dto:vehicleDtos){
            VehicleTM vehicleTM=new VehicleTM(
                    dto.getVehicle_id(),
                    dto.getModel(),
                    dto.getColour(),
                    dto.getCategory(),
                    dto.getQuantity(),
                    dto.getPackage_id()
            );
            vehicleTMS.add(vehicleTM);

        }
        tblVehicle.setItems(vehicleTMS);
    }
    private  void clearFields(){
        txtVehicleId.setText("");
        txtModel.setText("");
        txtColour.setText("");
        txtCategory.setText("");
        txtQuantity.setText("");
        txtPackageId.setText("");

    }



    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadCurrentPackageId();
        loadNextVehicleId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId = txtVehicleId.getText();
        String model = txtModel.getText();
        String colour = txtColour.getText();
        String category = txtCategory.getText();
        String packageId = txtPackageId.getText();

        // Regex patterns
        String vehicleIdPattern = "^V\\d{3}$"; // Matches V001, V002, etc.
        String modelPattern = "^[A-Za-z0-9 ]+$"; // Letters, numbers, and spaces only
        String colourPattern = "^[A-Za-z]+$"; // Letters only
        String categoryPattern = "^[A-Za-z]+$"; // Letters only
        String packageIdPattern = "^P\\d{3}$"; // Matches P001, P002, etc.

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidVehicleId = vehicleId.matches(vehicleIdPattern);
        boolean isValidModel = model.matches(modelPattern);
        boolean isValidColour = colour.matches(colourPattern);
        boolean isValidCategory = category.matches(categoryPattern);
        boolean isValidPackageId = packageId.matches(packageIdPattern);

        // Alert message for validation errors
        if (!isValidVehicleId || !isValidModel || !isValidColour || !isValidCategory || !isValidPackageId) {
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

            if (!isValidVehicleId) {
                txtVehicleId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Vehicle ID (Expected format: V001)\n");
            }
            if (!isValidModel) {
                txtModel.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Model (Letters, numbers, and spaces only)\n");
            }
            if (!isValidColour) {
                txtColour.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Colour (Only letters allowed)\n");
            }
            if (!isValidCategory) {
                txtCategory.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Category (Only letters allowed)\n");
            }
            if (!isValidPackageId) {
                txtPackageId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package ID (Expected format: P001)\n");
            }

            new Alert(Alert.AlertType.WARNING, errorMessage.toString()).show();
            return;
        }

        // Validate quantity as a valid number
        int quantity;
        try {
            quantity = Integer.parseInt(txtQuantity.getText());
            if (quantity <= 0) {
                throw new NumberFormatException("Quantity must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            txtQuantity.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid quantity. Please enter a valid positive number").show();
            return;
        }

        // Create the VehicleDto object with validated data
        VehicleDto vehicleDto = new VehicleDto(vehicleId, model, colour, category, quantity, packageId);

        try {
            // Attempt to save the vehicle
            boolean isSaved = vehicleModel.saveVehicle(vehicleDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Saved Successfully").show();
                // Refresh the page and update fields after successful save
                refreshPage();
                loadNextVehicleId();
                loadCurrentPackageId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed To Save Vehicle").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Catch database related errors
            e.printStackTrace();  // Log the error for debugging
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace();  // Log the error for debugging
            new Alert(Alert.AlertType.ERROR, "Unexpected Error: " + e.getMessage()).show();
        }
    }

    private void resetFieldStyles() {
        txtVehicleId.setStyle("");
        txtModel.setStyle("");
        txtColour.setStyle("");
        txtCategory.setStyle("");
        txtQuantity.setStyle("");
        txtPackageId.setStyle("");
    }

    @FXML
    void btnSeachOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId = txtVehicleId.getText();

        // Check if vehicle ID is empty and alert the user
        if (vehicleId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Vehicle Id").show();
            return;  // Exit early if vehicle ID is empty
        }

        try {
            // Attempt to search for the vehicle in the database
            VehicleDto vehicle = vehicleModel.searchVehicle(vehicleId);

            if (vehicle != null) {
                // If vehicle is found, display its details in the text fields
                txtVehicleId.setText(vehicle.getVehicle_id());
                txtModel.setText(vehicle.getModel());
                txtColour.setText(vehicle.getColour());
                txtCategory.setText(vehicle.getCategory());
                txtQuantity.setText(String.valueOf(vehicle.getQuantity()));
                txtPackageId.setText(vehicle.getPackage_id());

                // Show an information alert confirming that the vehicle was found
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Found").show();

            } else {
                // If no vehicle is found, clear the fields and display an error alert
                new Alert(Alert.AlertType.ERROR, "Vehicle Not Found").show();
                loadCurrentPackageId();
                loadNextVehicleId();
                clearFields();
            }

        } catch (SQLException | ClassNotFoundException e) {
            // Catch any database or class not found exceptions and alert the user
            e.printStackTrace();  // Log the exception for debugging purposes
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching vehicle: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any unexpected exceptions that might occur during the search process
            e.printStackTrace();  // Log the exception for debugging purposes
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        // Retrieve input values from the text fields
        String vehicleId = txtVehicleId.getText();
        String model = txtModel.getText();
        String colour = txtColour.getText();
        String category = txtCategory.getText();
        String quantityText = txtQuantity.getText(); // Get quantity as string for validation
        String packageId = txtPackageId.getText();

        // Reset field styles before validation
        resetFieldStyles();

        // Regex patterns for validation
        String vehicleIdPattern = "^V\\d{3}$"; // Matches V001, V002, etc.
        String modelPattern = "^[A-Za-z0-9 ]+$"; // Alphanumeric with spaces
        String colourPattern = "^[A-Za-z ]+$"; // Letters and spaces only
        String categoryPattern = "^[A-Za-z ]+$"; // Letters and spaces only
        String quantityPattern = "^[1-9]\\d*$"; // Positive integers only (non-zero)
        String packageIdPattern = "^P\\d{3}$"; // Matches P001, P002, etc.

        // Validation checks
        boolean isValidVehicleId = vehicleId.matches(vehicleIdPattern);
        boolean isValidModel = model.matches(modelPattern);
        boolean isValidColour = colour.matches(colourPattern);
        boolean isValidCategory = category.matches(categoryPattern);
        boolean isValidQuantity = quantityText.matches(quantityPattern);
        boolean isValidPackageId = packageId.matches(packageIdPattern);

        if (!isValidVehicleId || !isValidModel || !isValidColour || !isValidCategory || !isValidQuantity || !isValidPackageId) {
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

            if (!isValidVehicleId) {
                txtVehicleId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Vehicle ID (Expected format: V001)\n");
            }
            if (!isValidModel) {
                txtModel.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Model (Only alphanumeric characters and spaces allowed)\n");
            }
            if (!isValidColour) {
                txtColour.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Colour (Only letters and spaces allowed)\n");
            }
            if (!isValidCategory) {
                txtCategory.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Category (Only letters and spaces allowed)\n");
            }
            if (!isValidQuantity) {
                txtQuantity.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Quantity (Must be a positive integer)\n");
            }
            if (!isValidPackageId) {
                txtPackageId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package ID (Expected format: P001)\n");
            }

            new Alert(Alert.AlertType.WARNING, errorMessage.toString()).show();
            return; // Exit early if validation fails
        }

        // Parse the quantity to an integer (safe since regex validated it)
        int quantity = Integer.parseInt(quantityText);

        // Create the VehicleDto object with the validated input data
        VehicleDto vehicleDto = new VehicleDto(vehicleId, model, colour, category, quantity, packageId);

        try {
            // Attempt to update the vehicle in the database
            boolean isUpdated = vehicleModel.updateVehicle(vehicleDto);

            // Show the result of the update operation
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Updated Successfully").show();
                refreshPage();
                loadCurrentPackageId();
                loadNextVehicleId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed To Update Vehicle").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            // Catch any database-related exceptions and display the error
            e.printStackTrace(); // Log the exception for debugging
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating vehicle: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace(); // Log the exception for debugging
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }


    @FXML
    void onVehicleTblClicked(MouseEvent event) {
        VehicleTM vehicleTM=tblVehicle.getSelectionModel().getSelectedItem();
        if(vehicleTM!=null){
            txtVehicleId.setText(vehicleTM.getVehicle_id());
            txtModel.setText(vehicleTM.getModel());
            txtColour.setText(vehicleTM.getColour());
            txtCategory.setText(vehicleTM.getCategory());
            txtQuantity.setText(String.valueOf(vehicleTM.getQuantity()));
            txtPackageId.setText(vehicleTM.getPackage_id());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the table columns with their respective properties
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colColour.setCellValueFactory(new PropertyValueFactory<>("colour"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("package_id"));

        try {
            // Attempt to load data for the page and refresh the table
            refreshPage();
            loadCurrentPackageId();
            loadNextVehicleId();
            refreshTableData();

        } catch (SQLException | ClassNotFoundException e) {
            // Handle database and class loading errors
            e.printStackTrace();  // Log the exception for debugging
            new Alert(Alert.AlertType.ERROR, "Error occurred while loading data: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any other unforeseen errors and display an alert
            e.printStackTrace();  // Log the exception for debugging
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }
    private  void  refreshPage() throws SQLException, ClassNotFoundException {
        loadNextVehicleId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }
    private  void loadTableData() throws  SQLException,ClassNotFoundException{
        ArrayList<VehicleDto> vehicleDtos=vehicleModel.getAllVehicles();
        ObservableList<VehicleTM> vehicleTMS=FXCollections.observableArrayList();
        for (VehicleDto dto:vehicleDtos){
            VehicleTM vehicleTM=new VehicleTM(
                    dto.getVehicle_id(),
                    dto.getModel(),
                    dto.getColour(),
                    dto.getCategory(),
                    dto.getQuantity(),
                    dto.getPackage_id()

            );
            vehicleTMS.add(vehicleTM);
        }
        tblVehicle.setItems(vehicleTMS);

    }
    public  void loadNextVehicleId() throws SQLException, ClassNotFoundException {
        String nextVehicleId=vehicleModel.loadNextVehicleId();
        txtVehicleId.setText(nextVehicleId);
    }
    public void loadCurrentPackageId() throws SQLException, ClassNotFoundException {
        String currentPackageId = vehicleModel.loadCurrentPackageId();
        txtPackageId.setText(currentPackageId);
    }
    @FXML
    void btnReturnReportOnAction(ActionEvent event) {
        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/ReturnReport.jrxml")
            );


            Connection connection = DBConnection.getInstance().getConnection();


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_Date", LocalDate.now().toString());  // Example parameter

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            // Display the report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate report!").show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error!").show();
        }

    }



}
