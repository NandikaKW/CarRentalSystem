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
import lk.ijse.gdse.carrentalsystem.dto.RentPayemntDto;
import lk.ijse.gdse.carrentalsystem.model.RentPaymentModel;
import lk.ijse.gdse.carrentalsystem.tm.RentPaymentTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class RentPaymentDetailsController  implements Initializable {
    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<Integer> ComboYear;

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
    private TableColumn<RentPaymentTM, String> colDescription;

    @FXML
    private TableColumn<RentPaymentTM, Integer> colDuration;

    @FXML
    private TableColumn<RentPaymentTM, Double> colPayamount;

    @FXML
    private TableColumn<RentPaymentTM, Date> colPaymentDate;

    @FXML
    private TableColumn<RentPaymentTM, String> colPaymentID;

    @FXML
    private TableColumn<RentPaymentTM, String> colPaymentMethod;

    @FXML
    private TableColumn<RentPaymentTM, String> colRentId;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDuration;

    @FXML
    private Label lblPayamount;

    @FXML
    private Label lblPaymentDate;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblPaymentMethod;

    @FXML
    private Label lblRentId;

    @FXML
    private TableView<RentPaymentTM> tblRentPayment;

    @FXML
    private TextField txtDescription;

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
    void ComboDayOnAction(ActionEvent event) {
        showSelectedDate();

    }

    @FXML
    void ComboMonthOnAction(ActionEvent event) {
        updateDays();

    }

    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();

    }
    private void updateDays() {
        try {
            Integer year = ComboYear.getValue();
            Integer month = CombMonth.getValue();

            // Check if both ComboBox values are not null
            if (year != null && month != null) {
                int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
                ComboDay.setItems(FXCollections.observableArrayList(
                        IntStream.rangeClosed(1, daysInMonth).boxed().toList()
                ));
                ComboDay.getSelectionModel().selectFirst();
                showSelectedDate();
            } else {
                System.out.println("Year or Month ComboBox is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showSelectedDate() {
        Integer year = ComboYear.getValue();
        Integer month = CombMonth.getValue();
        Integer day = ComboDay.getValue();

        if (year != null && month != null && day != null) {
            txtPaymentDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }
    }




    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, SQLException {
        String rentId = txtRentId.getText();
        String paymentId=txtPaymentId.getText();
        if(rentId.isEmpty() || paymentId.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter Rent Id and Payment Id").show();
            return;

        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this Rentpayment?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
                boolean isDeleted= RentPaymentModel.deleteRentPayment(rentId,paymentId);
                if(isDeleted){
                    new Alert(Alert.AlertType.INFORMATION,"RentPayment deleted successfully").show();
                    clearFields();
                    loadNextPaymentId();
                    loadNextRentId();
                    refreshTableData();

                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed to delete Rentpayment").show();
                }

            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting RentPayment: "+e.getMessage()).show();

            }
        }

    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<RentPayemntDto> rentPayemntDtos=RentPaymentModel.getAllPayments();
        ObservableList<RentPaymentTM> rentPaymentTMs= FXCollections.observableArrayList();
        for (RentPayemntDto rentPayemntDto:rentPayemntDtos) {
            RentPaymentTM rentPaymentTM=new RentPaymentTM(
                    rentPayemntDto.getRent_id(),
                    rentPayemntDto.getPay_id(),
                    rentPayemntDto.getPayment_date(),
                    rentPayemntDto.getDuration(),
                    rentPayemntDto.getDescription(),
                    rentPayemntDto.getPay_amount(),
                    rentPayemntDto.getPayment_method()


            );
            rentPaymentTMs.add(rentPaymentTM);

        }
        tblRentPayment.setItems(rentPaymentTMs);
    }
    private  void clearFields(){
        txtRentId.clear();
        txtPaymentId.clear();
        txtDescription.clear();
        txtDuration.clear();
        txtPayamount.clear();
        txtPaymentDate.clear();
        txtPaymentMethod.clear();
    }


    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String rentId = txtRentId.getText();
        String paymentId = txtPaymentId.getText();

        // Convert payment date from String to Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(txtPaymentDate.getText(), formatter);
        Date paymentDate = java.sql.Date.valueOf(localDate);  // Convert to SQL Date type

        // Parse duration and payAmount
        String duration = String.valueOf(Integer.parseInt(txtDuration.getText()));
        String description = txtDescription.getText();

        // Convert payAmount from String to BigDecimal
        BigDecimal payamount = new BigDecimal(txtPayamount.getText());

        String paymentMethod = txtPaymentMethod.getText();

        RentPayemntDto rentPayemntDto = new RentPayemntDto(rentId, paymentId, paymentDate, duration, description, payamount, paymentMethod);

        try {
            boolean isSaved = RentPaymentModel.saveRentPayment(rentPayemntDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "RentPayment saved successfully").show();
                refreshPage();
                loadNextRentId();
                loadNextPaymentId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save RentPayment").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();  // Log the error
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
         String rentId = txtRentId.getText();
         String paymentId=txtPaymentId.getText();

         if (rentId.isEmpty() || paymentId.isEmpty()){
             new Alert(Alert.AlertType.ERROR,"Please Enter Rent Id and Payment Id").show();
             return;
         }
         try{
             RentPayemntDto rentPayemntDto=RentPaymentModel.searchRentPayment(rentId,paymentId);
             if (rentPayemntDto!=null){
                 txtRentId.setText(rentPayemntDto.getRent_id());
                 txtPaymentId.setText(rentPayemntDto.getPay_id());
                 txtPaymentDate.setText(rentPayemntDto.getPayment_date().toString());
                 txtDuration.setText(String.valueOf(rentPayemntDto.getDuration()));
                 txtDescription.setText(rentPayemntDto.getDescription());
                 txtPayamount.setText(String.valueOf(rentPayemntDto.getPay_amount()));
                 txtPaymentMethod.setText(rentPayemntDto.getPayment_method());
             }else{
                 new Alert(Alert.AlertType.ERROR,"RentPayment Not Found").show();
                 loadNextPaymentId();
                 loadNextRentId();
                 clearFields();
             }

         }catch (SQLException | ClassNotFoundException e){
             e.printStackTrace();
             new Alert(Alert.AlertType.ERROR,"Error occurred while searching RentPayment: "+e.getMessage()).show();

         }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String rentId = txtRentId.getText();
        String paymentId = txtPaymentId.getText();

        // Convert payment date from String to Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(txtPaymentDate.getText(), formatter);
        Date paymentDate = java.sql.Date.valueOf(localDate);  // Convert to SQL Date type

        // Parse duration and payAmount
        String duration = String.valueOf(Integer.parseInt(txtDuration.getText()));
        String description = txtDescription.getText();

        // Convert payAmount from String to BigDecimal
        BigDecimal payAmount = new BigDecimal(txtPayamount.getText());

        String paymentMethod = txtPaymentMethod.getText();

        RentPayemntDto rentPayemntDto = new RentPayemntDto(rentId, paymentId, paymentDate, duration, description, payAmount, paymentMethod);

        boolean isUpdated = RentPaymentModel.UpdateRentPayment(rentPayemntDto);
        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "RentPayment updated successfully").show();
            refreshPage();
            loadNextPaymentId();
            loadNextRentId();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update RentPayment").show();
        }
    }


    @FXML
    void tblRentPaymentOnClickedOnAction(MouseEvent event) {
        RentPaymentTM rentPaymentTM=tblRentPayment.getSelectionModel().getSelectedItem();
        if (rentPaymentTM!=null){
            txtRentId.setText(rentPaymentTM.getRent_id());
            txtPaymentId.setText(rentPaymentTM.getPay_id());
            txtPaymentDate.setText(rentPaymentTM.getPayment_date().toString());
            txtDuration.setText(String.valueOf(rentPaymentTM.getDuration()));
            txtDescription.setText(rentPaymentTM.getDescription());
            txtPayamount.setText(String.valueOf(rentPaymentTM.getPay_amount()));
            txtPaymentMethod.setText(rentPaymentTM.getPayment_method());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
        colPaymentID.setCellValueFactory(new PropertyValueFactory<>("pay_id"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("payment_date"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPayamount.setCellValueFactory(new PropertyValueFactory<>("pay_amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("payment_method"));

        try{
            loadNextPaymentId();
            loadNextRentId();
            refreshTableData();
            initializeDateCombos();

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to load RentPayment data").show();

        }
    }
    private void initializeDateCombos() {
        // Populate ComboYear with the last 50 years up to the current year
        ComboYear.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYear.getSelectionModel().selectLast();

        // Populate CombMonth with values 1 to 12
        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();

        // Update days based on initial year and month selection
        updateDays();
    }


    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPaymentId();
        loadNextRentId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();

    }
    private  void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<RentPayemntDto> rentPayemntDtos=RentPaymentModel.getAllPayments();
        ObservableList<RentPaymentTM> rentPaymentTMS=FXCollections.observableArrayList();
        for (RentPayemntDto rentPayemntDto:rentPayemntDtos) {
            RentPaymentTM rentPaymentTM=new RentPaymentTM(
                    rentPayemntDto.getRent_id(),
                    rentPayemntDto.getPay_id(),
                    rentPayemntDto.getPayment_date(),
                    rentPayemntDto.getDuration(),
                    rentPayemntDto.getDescription(),
                    rentPayemntDto.getPay_amount(),
                    rentPayemntDto.getPayment_method()

            );
            rentPaymentTMS.add(rentPaymentTM);

        }
        tblRentPayment.setItems(rentPaymentTMS);
    }
    public void loadNextPaymentId() throws SQLException, ClassNotFoundException {
        try {
            String nextPaymentId = RentPaymentModel.loadNextPaymentId();

            txtPaymentId.setText(nextPaymentId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadNextRentId() throws SQLException, ClassNotFoundException {
        try {
            String nextRentId = RentPaymentModel.loadNextRentId();
            txtRentId.setText(nextRentId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
