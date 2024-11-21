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
import lk.ijse.gdse.carrentalsystem.dto.PackageDto;
import lk.ijse.gdse.carrentalsystem.model.PackageModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.PackageTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class PackageOffersController  implements Initializable {
    @FXML
    private JFXButton btnReport;


    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<Integer> ComboYear;
    @FXML
    private TableColumn<PackageTM, String> colDescription;

    @FXML
    private TableColumn<PackageTM, Boolean> colInsuarence;

    @FXML
    private TableColumn<PackageTM, String> colMileage;

    @FXML
    private TableColumn<PackageTM, String> colPackageID;

    @FXML
    private TableColumn<PackageTM, String> colPackageName;

    @FXML
    private TableColumn<PackageTM, Date> colRentDate;

    @FXML
    private TableColumn<PackageTM, String> colRentDuration;

    @FXML
    private TableColumn<PackageTM, Double> colTotalCost;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblInsuarence;

    @FXML
    private Label lblMilage;

    @FXML
    private Label lblPackage;

    @FXML
    private Label lblPackageName;

    @FXML
    private Label lblRentDate;

    @FXML
    private Label lblRentDuration;

    @FXML
    private Label lblTotalCost;

    @FXML
    private TableView<PackageTM> tblPackage;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtMileage;

    @FXML
    private TextField txtPackageId;

    @FXML
    private TextField txtPackageName;

    @FXML
    private TextField txtRentDate;

    @FXML
    private TextField txtRentDuration;

    @FXML
    private TextField txtcost;

    @FXML
    private TextField txtinsuarence;

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String packageId = txtPackageId.getText();

        // Input validation for Package ID
        if (packageId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Package ID to delete!").show();
            return; // Exit if the Package ID is not entered
        }

        // Confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Package?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        // Proceed only if YES is selected
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Attempt to delete the package
                boolean isDeleted = PackageModel.deletePackage(packageId);

                if (isDeleted) {
                    // Show success message if deletion is successful
                    new Alert(Alert.AlertType.INFORMATION, "Package deleted successfully!").show();
                    clearFields();
                    refreshTableData();
                    loadNextPackageId();
                } else {
                    // Show error message if deletion failed
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Package!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                // Catch database errors and show the appropriate message to the user
                e.printStackTrace(); // Log the exception for debugging purposes
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Package: " + e.getMessage()).show();
            } catch (Exception e) {
                // Catch any other unexpected errors
                e.printStackTrace(); // Log the exception for debugging purposes
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            }
        }

    }



    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadNextPackageId();

    }
    private void clearFields() {
        txtPackageId.setText("");
        txtPackageName.setText("");
        txtcost.setText("");
        txtinsuarence.setText("");
        txtRentDuration.setText("");
        txtRentDate.setText("");
        txtMileage.setText("");
        txtDescription.setText("");
    }




    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        // Get input data from fields
        String packageId = txtPackageId.getText();
        String packageName = txtPackageName.getText();
        BigDecimal cost = BigDecimal.ZERO;
        String rentDuration = txtRentDuration.getText();
        String mileage = txtMileage.getText();
        String description = txtDescription.getText();

        // Regex patterns
        String packageIdPattern = "^P\\d{3}$"; // Matches P001, P002, etc.
        String packageNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String rentDurationPattern = "^[\\d]+\\s?(days|months|years)?$"; // Matches "10 days", "5 months", etc.
        String mileagePattern = "^[\\d]+\\s?(km|mi)?$"; // Matches "1500 km", "2000 mi", etc.

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidPackageId = packageId.matches(packageIdPattern);
        boolean isValidPackageName = packageName.matches(packageNamePattern);
        boolean isValidRentDuration = rentDuration.matches(rentDurationPattern);
        boolean isValidMileage = mileage.matches(mileagePattern);

        // Validate cost
        try {
            cost = new BigDecimal(txtcost.getText()); // Convert cost to BigDecimal
        } catch (NumberFormatException e) {
            txtcost.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid cost format!").show();
            return; // Exit if cost format is invalid
        }

        // Validate insurance
        boolean insurance = false;
        try {
            insurance = Boolean.parseBoolean(txtinsuarence.getText()); // Parse insurance input
        } catch (Exception e) {
            txtinsuarence.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid insurance value! Please enter true/false.").show();
            return; // Exit if insurance value is invalid
        }

        // Parsing rentDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date rentDate = null;
        try {
            rentDate = dateFormat.parse(txtRentDate.getText()); // Parse date
        } catch (ParseException e) {
            txtRentDate.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid date format! Use yyyy-MM-dd.").show();
            return; // Exit if date format is wrong
        }

        // Alert message for validation errors
        if (!isValidPackageId || !isValidPackageName || !isValidRentDuration || !isValidMileage) {
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

            if (!isValidPackageId) {
                txtPackageId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package ID (Expected format: P001)\n");
            }
            if (!isValidPackageName) {
                txtPackageName.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package Name (Only letters and spaces allowed)\n");
            }
            if (!isValidRentDuration) {
                txtRentDuration.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Rental Duration (e.g., '10 days')\n");
            }
            if (!isValidMileage) {
                txtMileage.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Mileage (e.g., '1500 km')\n");
            }

            new Alert(Alert.AlertType.WARNING, errorMessage.toString()).show();
            return;
        }

        // Create PackageDto object to pass the data to the model
        PackageDto dto = new PackageDto(packageId, packageName, cost, insurance, rentDuration, rentDate, mileage, description);

        // Attempt to save the package in the database
        try {
            boolean isSaved = PackageModel.savePackage(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Package saved successfully!").show();
                loadNextPackageId();
                refreshTableData();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save package!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Log the exception
            new Alert(Alert.AlertType.ERROR, "Database connection error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }
    }

    private void resetFieldStyles() {
        txtPackageId.setStyle("");
        txtPackageName.setStyle("");
        txtcost.setStyle("");
        txtinsuarence.setStyle("");
        txtRentDuration.setStyle("");
        txtRentDate.setStyle("");
        txtMileage.setStyle("");
        txtDescription.setStyle("");
    }


    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPackageId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();

    }

    @FXML
    void btnSearchAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String packageId = txtPackageId.getText();

        // Check if Package ID is provided
        if (packageId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Package ID to search!").show();
            return; // Exit if Package ID is not provided
        }

        try {
            // Search the package using the PackageModel
            PackageDto dto = PackageModel.searchPackage(packageId);

            if (dto != null) {
                // Populate text fields with retrieved data
                txtPackageId.setText(dto.getPackageId());
                txtPackageName.setText(dto.getPackageName());
                txtcost.setText(dto.getTotalCost().toString());
                txtinsuarence.setText(String.valueOf(dto.isInsuranceIncluded()));
                txtRentDuration.setText(dto.getRentalDuration());
                txtRentDate.setText(dto.getRentDate().toString());
                txtMileage.setText(dto.getMileageLimit());
                txtDescription.setText(dto.getDescription());
            } else {
                // If package not found, show a warning
                new Alert(Alert.AlertType.WARNING, "Package not found!").show();
                clearFields(); // Clear fields if package is not found
                loadNextPackageId(); // Load the next available Package ID
            }

        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error while searching for Package: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error while searching for Package: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String packageId = txtPackageId.getText();
        String packageName = txtPackageName.getText();
        BigDecimal cost = null;

        // Regex patterns
        String packageIdPattern = "^P\\d{3}$"; // Matches P001, P002, etc.
        String packageNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String rentDurationPattern = "^[\\w\\s]+$"; // Allows alphanumeric and spaces for durations like "10 days"
        String mileagePattern = "^[\\w\\s]+$"; // Allows alphanumeric and spaces for mileage like "1500 km"

        // Reset field styles
        resetFieldStyles();

        // Validate cost input
        try {
            cost = new BigDecimal(txtcost.getText());
        } catch (NumberFormatException e) {
            txtcost.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid cost format! Please enter a valid number.").show();
            return;
        }

        boolean insurance = Boolean.parseBoolean(txtinsuarence.getText());
        String rentDuration = txtRentDuration.getText();
        String mileage = txtMileage.getText();
        String description = txtDescription.getText();

        // Parsing date using java.util.Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date rentDate = null;
        try {
            rentDate = dateFormat.parse(txtRentDate.getText());
        } catch (ParseException e) {
            txtRentDate.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid date format! Use yyyy-MM-dd.").show();
            return;
        }

        // Validation checks
        boolean isValidPackageId = packageId.matches(packageIdPattern);
        boolean isValidPackageName = packageName.matches(packageNamePattern);
        boolean isValidRentDuration = rentDuration.matches(rentDurationPattern);
        boolean isValidMileage = mileage.matches(mileagePattern);

        if (!isValidPackageId || !isValidPackageName || !isValidRentDuration || !isValidMileage) {
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

            if (!isValidPackageId) {
                txtPackageId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package ID (Expected format: P001)\n");
            }
            if (!isValidPackageName) {
                txtPackageName.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Package Name (Only letters and spaces allowed)\n");
            }
            if (!isValidRentDuration) {
                txtRentDuration.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Rent Duration (Alphanumeric and spaces only)\n");
            }
            if (!isValidMileage) {
                txtMileage.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Mileage (Alphanumeric and spaces only)\n");
            }

            new Alert(Alert.AlertType.WARNING, errorMessage.toString()).show();
            return;
        }

        // Ensure all required fields are filled before proceeding
        if (description.isEmpty()) {
            txtDescription.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Please fill the description field before updating.").show();
            return;
        }

        // Create PackageDto and update
        PackageDto dto = new PackageDto(packageId, packageName, cost, insurance, rentDuration, rentDate, mileage, description);

        try {
            boolean isUpdated = PackageModel.updatePackage(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Package updated successfully!").show();
                refreshPage();
                loadNextPackageId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update package!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while updating the package: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error occurred while updating the package: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }
    }


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colPackageID.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colInsuarence.setCellValueFactory(new PropertyValueFactory<>("insuranceIncluded"));
        colRentDuration.setCellValueFactory(new PropertyValueFactory<>("rentalDuration"));
        colRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileageLimit"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            // Attempt to refresh the page, load data, and initialize components
            refreshPage();
            refreshTableData();
            loadNextPackageId();
            initializeDateCombos();

        } catch (SQLException e) {
            // Handle database-related errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle class not found errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (NullPointerException e) {
            // Handle null pointer exceptions if any
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Null value encountered during initialization: " + e.getMessage()).show();
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred during initialization: " + e.getMessage()).show();
        }
    }
    private void initializeDateCombos() {
        ComboYear.setItems(FXCollections.observableArrayList(
            IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYear.getSelectionModel().selectLast();
        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();
        updateDays();

    }

private void loadTableData() throws SQLException, ClassNotFoundException {
    // Retrieve all packages as DTO objects
    ArrayList<PackageDto> packageDtos = PackageModel.getAllPackages();


    ObservableList<PackageTM> packageTMS = FXCollections.observableArrayList();

    // Loop through each PackageDto object
    for (PackageDto packageDto : packageDtos) {
        // Create a new PackageTM object with the corresponding fields from PackageDto
        PackageTM packageTM = new PackageTM(
                packageDto.getPackageId(),
                packageDto.getPackageName(),
                packageDto.getTotalCost(),
                packageDto.isInsuranceIncluded(), // Assuming you want to display "Yes"/"No" for boolean
                packageDto.getRentalDuration(),
                packageDto.getRentDate(),
                packageDto.getMileageLimit(),
                packageDto.getDescription()
        );

        // Add the PackageTM object to the observable list
        packageTMS.add(packageTM);
    }

    // Set the data into the table view
    tblPackage.setItems(packageTMS);
}

    public void loadNextPackageId() throws SQLException, ClassNotFoundException {
        String nextPackageId=PackageModel.loadNextPackageId();
        txtPackageId.setText(nextPackageId);
    }
    private  void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<PackageDto> packageDtos= PackageModel.getAllPackages();
        ObservableList<PackageTM> packageTMS=FXCollections.observableArrayList();
        for(PackageDto  dto:packageDtos){
            packageTMS.add(new PackageTM(
                    dto.getPackageId(),
                    dto.getPackageName(),
                    dto.getTotalCost(),
                    dto.isInsuranceIncluded(),
                    dto.getRentalDuration(),
                    dto.getRentDate(),
                    dto.getMileageLimit(),
                    dto.getDescription()
            ));

        }
        tblPackage.setItems(packageTMS);

    }


    public void MouseClickOnAction(MouseEvent mouseEvent) {
        PackageTM packageTM=tblPackage.getSelectionModel().getSelectedItem();
        if(packageTM!=null){
            txtPackageId.setText(packageTM.getPackageId());
            txtPackageName.setText(packageTM.getPackageName());
            txtcost.setText(String.valueOf(packageTM.getTotalCost()));
            txtinsuarence.setText(String.valueOf(packageTM.isInsuranceIncluded()));
            txtRentDuration.setText(packageTM.getRentalDuration());
            txtRentDate.setText(String.valueOf(packageTM.getRentDate()));
            txtMileage.setText(packageTM.getMileageLimit());
            txtDescription.setText(packageTM.getDescription());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }
    }


    public void ComboYearOnAction(ActionEvent actionEvent) {
        updateDays();

    }

    public void ComboMonthOnAction(ActionEvent actionEvent) {
        updateDays();

    }

    public void ComboDayOnAction(ActionEvent actionEvent) {
        showSelectedDate();
    }
    private void updateDays(){
        try{
            Integer year=ComboYear.getValue();
            Integer month=CombMonth.getValue();
            if (year!=null && month!=null){
                int daysInMonth= YearMonth.of(year,month).lengthOfMonth();
                ComboDay.setItems(FXCollections.observableArrayList(
                        IntStream.rangeClosed(1,daysInMonth).boxed().toList()
                ));
                ComboDay.getSelectionModel().selectFirst();
                showSelectedDate();

            }else{
                System.out.println("Year or Month ComboBox is null");
            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }
    private void showSelectedDate(){
        Integer year=ComboYear.getValue();
        Integer month=CombMonth.getValue();
        Integer day=ComboDay.getValue();
       if (year!=null && month!=null && day!=null){
           txtRentDate.setText(String.format("%04d-%02d-%02d",year,month,day));
       }

    }
    @FXML
    void btnReportGenerateOnAction(ActionEvent event) {
        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/PackageDetail.jrxml")
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
