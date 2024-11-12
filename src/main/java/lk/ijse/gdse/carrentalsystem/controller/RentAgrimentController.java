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
import lk.ijse.gdse.carrentalsystem.dto.AgrimentDto;
import lk.ijse.gdse.carrentalsystem.dto.RentDto;
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.model.AgrimentModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.AgrimentTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class RentAgrimentController implements Initializable {
    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> CombMonthOne;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<Integer> ComboDayOne;

    @FXML
    private ComboBox<Integer> ComboYear;

    @FXML
    private ComboBox<Integer> ComboYearOne;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnRseset;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<AgrimentTM, String> colAgrimentId;

    @FXML
    private TableColumn<AgrimentTM, Double> colDepositAmount;

    @FXML
    private TableColumn<AgrimentTM, Date> colEndDate;

    @FXML
    private TableColumn<AgrimentTM, String> colPaymentTerms;

    @FXML
    private TableColumn<AgrimentTM, Double> colRentCost;

    @FXML
    private TableColumn<AgrimentTM, Date> colStartDate;

    @FXML
    private Label lblAgrimentId;

    @FXML
    private Label lblCost;

    @FXML
    private Label lblDepositAmount;

    @FXML
    private Label lblEndDate;

    @FXML
    private Label lblPaymentTerms;

    @FXML
    private Label lblStartDate;

    @FXML
    private TableView<AgrimentTM> tblAgriment;

    @FXML
    private TextField txtAgrimentId;

    @FXML
    private TextField txtCost;

    @FXML
    private TextField txtDepositAmount;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtPaymentTerms;

    @FXML
    private TextField txtStartDate;

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String agrimentId = txtAgrimentId.getText();

        // Step 1: Check if Agriment ID is provided
        if (agrimentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please provide Agriment Id").show();
            return; // Exit if Agriment ID is not provided
        }

        // Step 2: Show confirmation alert for deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Rent Agriment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        // Step 3: Proceed with deletion only if user confirms
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Step 4: Perform deletion operation
                boolean isDeleted = AgrimentModel.deleteAgriment(agrimentId);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Rent Agriment Deleted").show();
                    clearFields();
                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Rent Agriment").show();
                }

            } catch (SQLException e) {
                // Step 5: Handle database-related errors
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred while deleting Agriment: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Step 6: Handle class not found errors (e.g., missing JDBC drivers)
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
            } catch (Exception e) {
                // Step 7: Handle any other unexpected errors
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
            }
        }
    }

    public void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<AgrimentDto> agrimentDtos = AgrimentModel.getAllAgriment();
        ObservableList<AgrimentTM> agrimentTMS = FXCollections.observableArrayList();
        for (AgrimentDto agrimentDto : agrimentDtos) {
            AgrimentTM agrimentTM = new AgrimentTM(
                    agrimentDto.getAgreement_id(),
                    agrimentDto.getPayment_terms(),
                    agrimentDto.getStart_date(),
                    agrimentDto.getEnd_date(),
                    agrimentDto.getDeposit_amount(),
                    agrimentDto.getTotal_rent_cost()

            );
            agrimentTMS.add(agrimentTM);
        }
        tblAgriment.setItems(agrimentTMS);

    }

    public void clearFields() {
        txtAgrimentId.setText("");
        txtCost.setText("");
        txtDepositAmount.setText("");
        txtEndDate.setText("");
        txtPaymentTerms.setText("");
        txtStartDate.setText("");

    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadNextAgrimentId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            String agrimentId = txtAgrimentId.getText();
            String paymentTerms = txtPaymentTerms.getText();

            // Check for missing fields
            if (agrimentId.isEmpty() || paymentTerms.isEmpty() || txtStartDate.getText().isEmpty() || txtEndDate.getText().isEmpty() || txtDepositAmount.getText().isEmpty() || txtCost.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "All fields must be filled in.").show();
                return;
            }

            // Convert String to Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = dateFormat.parse(txtStartDate.getText());
                endDate = dateFormat.parse(txtEndDate.getText());
            } catch (ParseException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd.").show();
                return; // Exit if date format is invalid
            }

            // Convert String to BigDecimal
            BigDecimal depositAmount = null;
            BigDecimal cost = null;

            try {
                depositAmount = new BigDecimal(txtDepositAmount.getText());
                cost = new BigDecimal(txtCost.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric format for deposit amount or cost.").show();
                return; // Exit if numeric format is invalid
            }

            AgrimentDto agrimentDto = new AgrimentDto(agrimentId, paymentTerms, startDate, endDate, depositAmount, cost);

            try {
                boolean isSaved = AgrimentModel.saveAgriment(agrimentDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Rent Agreement Saved").show();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Rent Agreement").show();
                }
            } catch (SQLException e) {
                // Handle database-related errors
                new Alert(Alert.AlertType.ERROR, "Database error occurred while saving Rent Agreement: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Handle class not found errors (e.g., missing JDBC driver)
                new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
            }

        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String agrimentId = txtAgrimentId.getText();

        // Check if AgrimentId is empty
        if (agrimentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Provide Agriment Id").show();
            return; // Exit the method early if AgrimentId is empty
        }

        try {
            // Attempt to search for the agreement using the AgrimentModel
            AgrimentDto agrimentDto = AgrimentModel.searchAgriment(agrimentId);

            if (agrimentDto != null) {
                // If found, populate the fields with the data from the AgrimentDto
                txtAgrimentId.setText(agrimentDto.getAgreement_id());
                txtPaymentTerms.setText(agrimentDto.getPayment_terms());
                txtStartDate.setText(agrimentDto.getStart_date().toString());
                txtEndDate.setText(agrimentDto.getEnd_date().toString());
                txtDepositAmount.setText(agrimentDto.getDeposit_amount().toString());
                txtCost.setText(agrimentDto.getTotal_rent_cost().toString());
            } else {
                // If no agreement is found, show an error and clear the fields
                new Alert(Alert.AlertType.ERROR, "Rent Agreement Not Found").show();
                clearFields();
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while searching for the agreement: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException (e.g., JDBC driver issues)
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            String agrimentId = txtAgrimentId.getText();
            String paymentTerms = txtPaymentTerms.getText();

            // Convert String to Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;

            try {
                startDate = dateFormat.parse(txtStartDate.getText());
                endDate = dateFormat.parse(txtEndDate.getText());
            } catch (ParseException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd.").show();
                return; // Exit the method early if there is an invalid date format
            }

            // Convert String to BigDecimal for depositAmount and cost
            BigDecimal depositAmount = null;
            BigDecimal cost = null;
            try {
                depositAmount = new BigDecimal(txtDepositAmount.getText());
                cost = new BigDecimal(txtCost.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric format for deposit amount or cost.").show();
                return; // Exit the method early if there is an invalid numeric format
            }

            // Create AgrimentDto object with valid values
            AgrimentDto agrimentDto = new AgrimentDto(agrimentId, paymentTerms, startDate, endDate, depositAmount, cost);

            // Attempt to update the rent agreement using AgrimentModel
            boolean isUpdated = AgrimentModel.updateAgriment(agrimentDto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Rent Agreement Updated").show();
                refreshPage(); // Refresh page or perform any additional actions if update is successful
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Rent Agreement").show();
            }

        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., database connectivity issues)
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while updating Rent Agreement: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException (e.g., JDBC driver issues)
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }
    @FXML
    void tblMouseClickedOnAction(MouseEvent event) {
        AgrimentTM agrimentTM = tblAgriment.getSelectionModel().getSelectedItem();
        if (agrimentTM != null) {
            txtAgrimentId.setText(agrimentTM.getAgreement_id());
            txtPaymentTerms.setText(agrimentTM.getPayment_terms());
            txtStartDate.setText(agrimentTM.getStart_date().toString());
            txtEndDate.setText(agrimentTM.getEnd_date().toString());
            txtDepositAmount.setText(agrimentTM.getDeposit_amount().toString());
            txtCost.setText(agrimentTM.getTotal_rent_cost().toString());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colAgrimentId.setCellValueFactory(new PropertyValueFactory<>("agreement_id"));
        colPaymentTerms.setCellValueFactory(new PropertyValueFactory<>("payment_terms"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        colDepositAmount.setCellValueFactory(new PropertyValueFactory<>("deposit_amount"));
        colRentCost.setCellValueFactory(new PropertyValueFactory<>("total_rent_cost"));

        try {
            // Load initial data and refresh UI elements
            loadNextAgrimentId();
            refreshPage();
            refreshTableData();
            updateYears();
            updateMonths();

            // Set default selections for year and month comboboxes
            ComboYear.getSelectionModel().selectFirst();
            CombMonth.getSelectionModel().selectFirst();
            updateDays();

            ComboYearOne.getSelectionModel().selectFirst();
            CombMonthOne.getSelectionModel().selectFirst();
            updateDaysOne();

        } catch (SQLException e) {
            // Handle SQLException - typically related to database errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while loading data: " + e.getMessage()).show();

        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException - typically related to JDBC driver or class loading issues
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error occurred while loading data: " + e.getMessage()).show();

        } catch (Exception e) {
            // Catch any other unexpected exceptions
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while initializing: " + e.getMessage()).show();
        }
    }
    private  void updateYears(){
        ObservableList<Integer> years=FXCollections.observableArrayList();
        int currentYear=java.time.Year.now().getValue();
        IntStream.rangeClosed(currentYear-10,currentYear+10).forEach(years::add);
        ComboYear.setItems(years);
        ComboYearOne.setItems(years);

    }
    private  void updateMonths(){
        ObservableList<Integer> years=FXCollections.observableArrayList();

        IntStream.rangeClosed(1,12).forEach(years::add);
        CombMonth.setItems(years);
        CombMonthOne.setItems(years);
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
    private  void updateDays(){
        Integer year=ComboYear.getValue();
        Integer month=CombMonth.getValue();
        if (year!=null && month!=null){
            int daysInMonth= YearMonth.of(year,month).lengthOfMonth();
            ComboDay.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1,daysInMonth).boxed().toList()
            ));
            ComboDay.getSelectionModel().selectFirst();
            showSelectedDate();
        }
    }
    private  void showSelectedDate(){
        Integer year=ComboYear.getValue();
        Integer month=CombMonth.getValue();
        Integer day=ComboDay.getValue();
        if (year!=null && month!=null && day!=null){
            txtStartDate.setText(String.format("%04d-%02d-%02d",year,month,day));
        }

    }
    private  void updateDaysOne(){
        Integer year=ComboYearOne.getValue();
        Integer month=CombMonthOne.getValue();
        if (year!=null && month!=null){
            int daysInMonth= YearMonth.of(year,month).lengthOfMonth();
            ComboDayOne.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1,daysInMonth).boxed().toList(

                )
            ));
            ComboDayOne.getSelectionModel().selectFirst();
            showSelectedDateOne();
        }


    }
    private  void showSelectedDateOne(){
        Integer year=ComboYearOne.getValue();
        Integer month=CombMonthOne.getValue();
        Integer day=ComboDayOne.getValue();
        if (year!=null && month!=null && day!=null){
            txtEndDate.setText(String.format("%04d-%02d-%02d",year,month,day));
        }
    }
    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<AgrimentDto> agrimentDtos = AgrimentModel.getAllAgriment();
        ObservableList<AgrimentTM> agrimentTMS=FXCollections.observableArrayList();
        for (AgrimentDto agrimentDto:agrimentDtos) {
            AgrimentTM agrimentTM=new AgrimentTM(
                    agrimentDto.getAgreement_id(),
                    agrimentDto.getPayment_terms(),
                    agrimentDto.getStart_date(),
                    agrimentDto.getEnd_date(),
                    agrimentDto.getDeposit_amount(),
                    agrimentDto.getTotal_rent_cost()
            );
            agrimentTMS.add(agrimentTM);

        }
        tblAgriment.setItems(agrimentTMS);
    }
    public void loadNextAgrimentId() throws SQLException, ClassNotFoundException {
        try {
            // Get the next agreement ID
            String nextAgrimentId = AgrimentModel.getNextAgrimentId();

            // Debugging to ensure the ID is returned correctly
            System.out.println("Next Agreement ID: " + nextAgrimentId);

            // Set the next agreement ID to the text field
            if (nextAgrimentId != null && !nextAgrimentId.isEmpty()) {
                txtAgrimentId.setText(nextAgrimentId);
            } else {
                // Handle case where the ID is null or empty (could happen if no agreement records exist)
                new Alert(Alert.AlertType.WARNING, "Failed to load next agreement ID").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while loading next agreement ID: " + e.getMessage()).show();
        }
    }


}
