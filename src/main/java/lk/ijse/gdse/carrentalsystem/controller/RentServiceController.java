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
import lk.ijse.gdse.carrentalsystem.dto.PaymentDto;
import lk.ijse.gdse.carrentalsystem.dto.RentDto;
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.model.AgrimentModel;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.model.RentModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.RentTM;


import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class RentServiceController  implements Initializable {
    @FXML
    private TextField txtAgrimentID;

    @FXML
    private TableColumn<?, ?> colAgrimentID;
    @FXML
    private Label lblAgreementID;

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
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<RentDto, String> colCustomerID;

    @FXML
    private TableColumn<RentDto, Date> colEndDate;

    @FXML
    private TableColumn<RentDto, String> colRentId;

    @FXML
    private TableColumn<RentDto, Date> colStartDate;

    @FXML
    private Label lblCustomerID;

    @FXML
    private Label lblEndDate;

    @FXML
    private Label lblRentId;

    @FXML
    private Label lblStartDate;

    @FXML
    private TableView<RentTM> tblRent;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtEndDate;

    @FXML
    private TextField txtRentId;

    @FXML
    private TextField txtStartDate;

    @FXML
    private JFXButton btnReset;

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadNextRentId();
        loadCurrentCustomerId();
        //loadNextCustomerId();
        loadCurrentAgreementId();
       // loadNextAgreementId();


    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
          String rentId=txtRentId.getText();
          if(rentId.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please enter a Rent ID to delete").show();
            return;

          }
          Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this rent?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
                boolean isDeleted=RentModel.DeleteRent(rentId);
                if(isDeleted){
                  new Alert(Alert.AlertType.INFORMATION,"Rent deleted successfully").show();
                  clearFields();
                  loadNextRentId();
                    loadCurrentCustomerId();
                 // loadNextCustomerId();
                  refreshTableData();
                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed to delete rent").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting rent: "+e.getMessage()).show();

            }

        }

    }
    private void clearFields(){
        txtRentId.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtCustomerId.setText("");
    }



    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String rentId = txtRentId.getText();
        String startDateStr = txtStartDate.getText();  // renamed variable
        String endDateStr = txtEndDate.getText();      // renamed variable
        String custId = txtCustomerId.getText();
        String agreementId = txtAgrimentID.getText(); // added variable for agreement ID

        // Define a date format that matches your input format (e.g., "yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the date strings into Date objects
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            // Create RentDto with the new agreementId
            RentDto dto = new RentDto(rentId, startDate, endDate, custId, agreementId);
            boolean isSaved = RentModel.saveRent(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Rent saved successfully").show();
                //loadNextCustomerId();
                loadCurrentCustomerId();
                loadNextRentId();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save rent").show();
            }
        } catch (ParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format! Use 'yyyy-MM-dd'.").show();
        }

    }






    private void refreshPage() throws SQLException, ClassNotFoundException {
        //loadNextAgreementId();
        loadCurrentAgreementId();
        loadNextRentId();
        loadTableData();
        //loadNextCustomerId();
        loadCurrentCustomerId();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }


    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String rentId = txtRentId.getText();
        if (rentId.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Please enter a Rent ID to search").show();
            return;
        }
        try {
            RentDto rent = RentModel.SearchRent(rentId); // Ensure method name is in camelCase
            if (rent != null) {
                txtRentId.setText(rent.getRentId()); // Updated to use camelCase
                txtStartDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(rent.getStartDate())); // Format date for display
                txtEndDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(rent.getEndDate())); // Format date for display
                txtCustomerId.setText(rent.getCustId()); // Updated to use camelCase
                txtAgrimentID.setText(rent.getAgreementId()); // Added to display agreement ID
                new Alert(Alert.AlertType.INFORMATION, "Rent found").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Rent not found").show();
                //loadNextCustomerId();
                loadCurrentCustomerId();
                loadNextRentId();
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching for Rent: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String rentId = txtRentId.getText();
        String startDateText = txtStartDate.getText();
        String endDateText = txtEndDate.getText();
        String custId = txtCustomerId.getText();
        String agreementId = txtAgrimentID.getText(); // Added to capture the agreement ID

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Parse the strings into Date objects
            Date startDate = dateFormat.parse(startDateText);
            Date endDate = dateFormat.parse(endDateText);

            RentDto dto = new RentDto(rentId, startDate, endDate, custId, agreementId); // Include agreementId
            boolean isUpdated = RentModel.updateRent(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Rent updated successfully").show();
                refreshPage();
                //loadNextCustomerId();
                loadCurrentCustomerId();
                loadNextRentId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update rent").show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd.").show();
        }
    }

    @FXML
    void tblRenClicked(MouseEvent event) {
        RentTM rentTM = tblRent.getSelectionModel().getSelectedItem();
        if (rentTM != null) {
            txtRentId.setText(rentTM.getRentId()); // Updated to use camelCase
            txtStartDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(rentTM.getStartDate())); // Format date for display
            txtEndDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(rentTM.getEndDate())); // Format date for display
            txtCustomerId.setText(rentTM.getCustId()); // Updated to use camelCase
            txtAgrimentID.setText(rentTM.getAgreementId()); // Added to display agreement ID
            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }
    public void loadCurrentCustomerId() throws SQLException, ClassNotFoundException {
        String currentCustomerId = CustomerModel.loadCurrentCustomerId();
        txtCustomerId.setText(currentCustomerId);
    }


    public void loadNextRentId() throws SQLException, ClassNotFoundException {
        String nextRentId = RentModel.loadNextRentId();
        txtRentId.setText(nextRentId);
    }
//    public  void loadNextCustomerId() throws SQLException, ClassNotFoundException {
//       String nextCustomerId= CustomerModel.loadNextCustomerId();
//       txtCustomerId.setText(nextCustomerId);
//    }
public void loadCurrentAgreementId() throws SQLException, ClassNotFoundException {
    String currentAgreementId = AgrimentModel.loadCurrentAgreementId();
    txtAgrimentID.setText(currentAgreementId);
}

//    public void loadNextAgreementId() throws SQLException, ClassNotFoundException {
//        String nextAgreementId= AgrimentModel.loadNextAgreementId();
//        txtAgrimentID.setText(nextAgreementId);
//    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId")); // Use camelCase
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate")); // Use camelCase
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate")); // Use camelCase
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("custId")); // Use camelCase
        colAgrimentID.setCellValueFactory(new PropertyValueFactory<>("agreementId")); // Added for agreementId

        try {
            refreshPage();
            loadNextRentId();
            //loadNextCustomerId();
            loadCurrentCustomerId();
            refreshTableData();

            updateYears();
            updateMonths();

            // Set default values for year and month to trigger day updates
            ComboYear.getSelectionModel().selectFirst();
            CombMonth.getSelectionModel().selectFirst();
            updateDays();

            // Same for the second set of combo boxes
            ComboYearOne.getSelectionModel().selectFirst();
            CombMonthOne.getSelectionModel().selectFirst();
            updateDaysOne();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Rent data").show();
        }
    }

    private void updateYears() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int currentYear = java.time.Year.now().getValue();
        IntStream.rangeClosed(currentYear - 10, currentYear + 10).forEach(years::add);
        ComboYear.setItems(years);
        ComboYearOne.setItems(years);
    }

    private void updateMonths() {
        ObservableList<Integer> months = FXCollections.observableArrayList();
        IntStream.rangeClosed(1, 12).forEach(months::add);
        CombMonth.setItems(months);
        CombMonthOne.setItems(months);
    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<RentDto> rentDtos = RentModel.getAllRentData();
        ObservableList<RentTM> rentTMS = FXCollections.observableArrayList();

        for (RentDto rentDto : rentDtos) {
            RentTM rentTM = new RentTM(
                    rentDto.getRentId(),
                    rentDto.getStartDate(),
                    rentDto.getEndDate(),
                    rentDto.getCustId(),
                    rentDto.getAgreementId() // Ensure this is retrieved properly
            );
            rentTMS.add(rentTM);
        }

        tblRent.setItems(rentTMS);
    }


    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<RentDto> rentDtos = RentModel.getAllRentData();
        ObservableList<RentTM> rentTMS = FXCollections.observableArrayList();

        for (RentDto dto : rentDtos) {
            rentTMS.add(new RentTM(
                    dto.getRentId(),       // Updated to use camelCase
                    dto.getStartDate(),    // Updated to use camelCase
                    dto.getEndDate(),      // Updated to use camelCase
                    dto.getCustId(),       // Updated to use camelCase
                    dto.getAgreementId()    // Added to use the new agreementId
            ));
        }

        tblRent.setItems(rentTMS);
    }

    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();
    }

    @FXML
    void ComboMonthOnAction(ActionEvent event) {
        updateDays();
    }

    @FXML
    void ComboDayOnAction(ActionEvent event) {
        showSelectedDate();
    }

    @FXML
    void ComboYearOneOnAction(ActionEvent event) {
        updateDaysOne();
    }

    @FXML
    void ComboMonthOneOnAction(ActionEvent event) {
        updateDaysOne();
    }

    @FXML
    void ComboDayOneOnAction(ActionEvent event) {
        showSelectedDateOne();
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
            txtEndDate.setText(String.format("%04d-%02d-%02d", year, month, day));
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
            txtStartDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }
    }


}
