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
import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.dto.tm.ReserveTM;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleRentDetailTM;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.model.RentModel;
import lk.ijse.gdse.carrentalsystem.model.VehicleModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class VehicleDetailsController  implements Initializable {
    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnReserveVehicle;

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
    private  final VehicleModel vehicleModel=new VehicleModel();

    private final ObservableList<ReserveTM> reserveTMS = FXCollections.observableArrayList();

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
                boolean isDeleted = VehicleModel.deleteVehicle(vehicleId);

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
        ArrayList<VehicleDto> vehicleDtos=VehicleModel.getAllVehicles();
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
        // Validate input fields for empty values
        String vehicleId = txtVehicleId.getText();
        String model = txtModel.getText();
        String colour = txtColour.getText();
        String category = txtCategory.getText();
        String packageId = txtPackageId.getText();

        // Check if any field is empty
        if (vehicleId.isEmpty() || model.isEmpty() || colour.isEmpty() || category.isEmpty() || packageId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields").show();
            return;  // Exit early if any field is empty
        }

        // Validate quantity as a valid number
        int quantity;
        try {
            quantity = Integer.parseInt(txtQuantity.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity. Please enter a valid number").show();
            return;  // Exit early if the quantity is not a valid integer
        }

        // Create the VehicleDto object with validated data
        VehicleDto vehicleDto = new VehicleDto(vehicleId, model, colour, category, quantity, packageId);

        try {
            // Attempt to save the vehicle
            boolean isSaved = VehicleModel.saveVehicle(vehicleDto);
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
            VehicleDto vehicle = VehicleModel.searchVehicle(vehicleId);

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
        String quantityText = txtQuantity.getText();  // Get quantity as string for validation
        String packageId = txtPackageId.getText();

        // Check if any critical fields are empty and alert the user
        if (vehicleId.isEmpty() || model.isEmpty() || colour.isEmpty() || category.isEmpty() || quantityText.isEmpty() || packageId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields").show();
            return;  // Exit early if any required field is empty
        }

        int quantity = 0;
        try {
            // Attempt to parse the quantity to an integer
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            // Handle invalid number format for quantity input
            new Alert(Alert.AlertType.ERROR, "Invalid quantity. Please enter a valid number").show();
            return;  // Exit early if quantity is not a valid number
        }

        // Create the VehicleDto object with the validated input data
        VehicleDto vehicleDto = new VehicleDto(vehicleId, model, colour, category, quantity, packageId);

        try {
            // Attempt to update the vehicle in the database
            boolean isUpdated = VehicleModel.updateVehicle(vehicleDto);

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
            e.printStackTrace();  // Log the exception for debugging
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating vehicle: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace();  // Log the exception for debugging
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
        ArrayList<VehicleDto> vehicleDtos=VehicleModel.getAllVehicles();
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
        String nextVehicleId=VehicleModel.loadNextVehicleId();
        txtVehicleId.setText(nextVehicleId);
    }
    public void loadCurrentPackageId() throws SQLException, ClassNotFoundException {
        String currentPackageId = VehicleModel.loadCurrentPackageId();
        txtPackageId.setText(currentPackageId);
    }
    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
//        // Validate if the cart is empty
//        if (tblVehicle.getItems().isEmpty()) {
//            new Alert(Alert.AlertType.ERROR, "Please add vehicles to the cart!").show();
//            return;
//        }
//
//
//
//        // Retrieve Rent details from the UI components
//        String rentId = lblRentId.getText();
//        Date startDate = Date.valueOf(txtStartDate.getText()); // Assuming it's a TextField or similar for date input
//        Date endDate = Date.valueOf(txtEndDate.getText());
//        String agreementId = txtAgreementId.getText();
//        String customerId = cmbCustomerId.getValue();
//
//        // List to hold VehicleRentDetailsDTO
//        ArrayList<VehicleRentDetailTDto> vehicleRentDetails = new ArrayList<>();
//
//        // Iterate over the vehicles in the cart and create VehicleRentDetailsDTO for each vehicle
//        for (ReserveTM reserveTM : cartTMS) {
//
//            // Create a VehicleRentDetailsDTO for each vehicle in the cart
//            VehicleRentDetailsDTO rentDetailsDTO = new VehicleRentDetailsDTO(
//                    rentId,
//                    reserveTM.getVehicle_id(),
//                    reserveTM.getQuantity(),
//                    reserveTM.getModel(),
//                    reserveTM.getColour(),
//                    reserveTM.getPackage_id(),
//                    reserveTM.getCategory()
//            );
//
//            // Add to the vehicle details list
//            vehicleRentDetails.add(rentDetailsDTO);
//        }
//
//        // Create RentDTO with all gathered data
//        RentDTO rentDTO = new RentDTO(
//                rentId,
//                customerId,
//                startDate,
//                endDate,
//                agreementId,
//                vehicleRentDetails
//        );
//
//        // Call saveRent to save the rent details to the database
//        boolean isSaved = rentModel.saveRent(rentDTO);
//
//        // Show appropriate alert based on the result of the save operation
//        if (isSaved) {
//            new Alert(Alert.AlertType.INFORMATION, "Rent order saved successfully!").show();
//
//            // Reset the page or perform any necessary actions after placing the rent
//            refreshPage();
//        } else {
//            new Alert(Alert.AlertType.ERROR, "Failed to save the rent order!").show();
//        }


    }

    @FXML
    void btnReserveVehicleOnAction(ActionEvent event) {
//        VehicleTM selectedVehicle = tblVehicle.getSelectionModel().getSelectedItem();
//        if (selectedVehicle == null) {
//            new Alert(Alert.AlertType.ERROR, "Please select a vehicle to reserve.").show();
//            return;
//        }
//
//        String vehicleId = selectedVehicle.getVehicle_id();
//
//        try {
//            // Validate if the vehicle is available for reservation (could be a field in your database)
//            VehicleDto vehicleDto = VehicleModel.searchVehicle(vehicleId);
//            if (vehicleDto == null) {
//                new Alert(Alert.AlertType.ERROR, "Vehicle not found.").show();
//                return;
//            }
//            if (vehicleDto.getQuantity() <= 0) {
//                new Alert(Alert.AlertType.WARNING, "Vehicle is out of stock.").show();
//                return;
//            }
//
//            // Reserve the vehicle
//            boolean isReserved = RentModel.reserveVehicle(vehicleId);
//            if (isReserved) {
//                new Alert(Alert.AlertType.INFORMATION, "Vehicle reserved successfully!").show();
//
//                // Update vehicle quantity in the database and refresh table data
//                int newQuantity = vehicleDto.getQuantity() - 1;
//                vehicleDto.setQuantity(newQuantity);
//                VehicleModel.updateVehicle(vehicleDto);
//                refreshTableData();
//            } else {
//                new Alert(Alert.AlertType.ERROR, "Failed to reserve the vehicle.").show();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage()).show();
//        }

    }


}
