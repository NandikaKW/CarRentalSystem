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
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.model.VehicleRentDetailModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleRentDetailTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class VehicleRentDetailsController  implements Initializable {
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
    private Label lblRentDate;

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
    private TextField txtRentDate;

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

        ComboYearTwo.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYearTwo.getSelectionModel().selectLast();

        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();

        CombMonthOne.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonthOne.getSelectionModel().selectFirst();

        CombMonthTwo.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonthTwo.getSelectionModel().selectFirst();

        updateDays();
        updateDaysOne();
        updateDaysTwo();
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
    void ComboDayTwoOnAction(ActionEvent event) {
        showSelectedDateTwo();


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
    void ComboMonthTwoOnAction(ActionEvent event) {
        updateDaysTwo();



    }

    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();

    }

    @FXML
    void ComboYearOneOnAction(ActionEvent event) {
        updateDaysOne();

    }

    @FXML
    void ComboYearTwoOnAction(ActionEvent event) {
        updateDaysTwo();


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

    private void updateDaysTwo() {
        Integer year = ComboYearTwo.getValue();
        Integer month = CombMonthTwo.getValue();

        if (year != null && month != null) {
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            ComboDayTwo.setItems(FXCollections.observableArrayList(
                    IntStream.rangeClosed(1, daysInMonth).boxed().toList()
            ));
            ComboDayTwo.getSelectionModel().selectFirst();
            showSelectedDateTwo();
        }
    }
    private void showSelectedDateTwo() {
        Integer year = ComboYearTwo.getValue();
        Integer month = CombMonthTwo.getValue();
        Integer day = ComboDayTwo.getValue();

        if (year != null && month != null && day != null) {
            txtRentDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }
    }

    public void txtDateone(ActionEvent actionEvent) {
        lblRentDate.setText("Selected Date: " + txtRentDate.getText());
    }



    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String vehicleId=txtVehicleID.getText();
        String rentId=txtRentId.getText();
        if(rentId.isEmpty() || vehicleId.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter Vehicle Id and Rent Id").show();
            return;

        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this vehicle rent?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if (optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
                boolean isDeleted =VehicleRentDetailModel.deleteVehicleRent(rentId,vehicleId);
                if(isDeleted){
                    new Alert(Alert.AlertType.INFORMATION,"Vehicle Rent Deleted Successfully").show();
                    clearFields();
                    loadNextVehicleId();
                    loadNextRentId();
                    refreshTableData();

                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed To Delete Vehicle Rent").show();
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting vehicle rent: "+e.getMessage()).show();

            }

        }

    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<VechileRentDetailDto>  vechileRentDetailDtos=VehicleRentDetailModel.getAllVehicleRent();
        ObservableList<VehicleRentDetailTM> vehicleRentDetailTMS= FXCollections.observableArrayList();
        for (VechileRentDetailDto vechileRentDetailDto:vechileRentDetailDtos){
            VehicleRentDetailTM vehicleRentDetailTM=new VehicleRentDetailTM(
                    vechileRentDetailDto.getVehicle_id(),
                    vechileRentDetailDto.getRent_id(),
                    vechileRentDetailDto.getStart_date(),
                    vechileRentDetailDto.getEnd_date(),
                    vechileRentDetailDto.getRent_date(),
                    vechileRentDetailDto.getCost(),
                    vechileRentDetailDto.getVehicle_condition()
            );
            vehicleRentDetailTMS.add(vehicleRentDetailTM);
        }
        tblVehileRent.setItems(vehicleRentDetailTMS);
    }

    private  void clearFields(){
        txtVehicleID.clear();
        txtRentId.clear();
        txtStartDate.clear();
        txtEndDate.clear();
        txtRentDate.clear();
        txtCost.clear();
        txtCondition.clear();
    }


    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String vehicleId = txtVehicleID.getText();
        String rentId = txtRentId.getText();

        // Using java.sql.Date.valueOf to convert from String to SQL Date
        java.sql.Date startDate = java.sql.Date.valueOf(txtStartDate.getText());
        java.sql.Date endDate = java.sql.Date.valueOf(txtEndDate.getText());
        java.sql.Date rentDate = java.sql.Date.valueOf(txtRentDate.getText());

        BigDecimal cost = BigDecimal.valueOf(Double.parseDouble(txtCost.getText()));
        String vechicleCondition = txtCondition.getText();

        // Updated to use java.sql.Date in the DTO as well
        VechileRentDetailDto vechileRentDetailDto = new VechileRentDetailDto(vehicleId, rentId, startDate, endDate, rentDate, cost, vechicleCondition);

        try {
            boolean isSaved = VehicleRentDetailModel.saveVehicleRent(vechileRentDetailDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Rent Saved Successfully").show();
               refreshPage();
               refreshTableData();
               loadNextRentId();
               loadNextVehicleId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving vehicle rent: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String vehicleId=txtVehicleID.getText();
        String rentId=txtRentId.getText();
        if(vehicleId.isEmpty() || rentId.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter Vehicle Id and Rent Id").show();
            return;

        }
        try{
            VechileRentDetailDto vechileRentDetailDto= VehicleRentDetailModel.searchVehicleRent(vehicleId,rentId);
            if (vechileRentDetailDto!=null){
                txtVehicleID.setText(vechileRentDetailDto.getVehicle_id());
                txtRentId.setText(vechileRentDetailDto.getRent_id());
                txtStartDate.setText(vechileRentDetailDto.getStart_date().toString());
                txtEndDate.setText(vechileRentDetailDto.getEnd_date().toString());
                txtRentDate.setText(vechileRentDetailDto.getRent_date().toString());
                txtCost.setText(vechileRentDetailDto.getCost().toString());
                txtCondition.setText(vechileRentDetailDto.getVehicle_condition());
            }else{
                new Alert(Alert.AlertType.ERROR,"Vehicle Rent Not Found").show();
                loadNextRentId();
                loadNextVehicleId();
                clearFields();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while searching vehicle rent: "+e.getMessage()).show();

        }


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId = txtVehicleID.getText();
        String rentId = txtRentId.getText();

        // Using java.sql.Date.valueOf to convert from String to SQL Date
        java.sql.Date startDate = java.sql.Date.valueOf(txtStartDate.getText());
        java.sql.Date endDate = java.sql.Date.valueOf(txtEndDate.getText());
        java.sql.Date rentDate = java.sql.Date.valueOf(txtRentDate.getText());

        BigDecimal cost = BigDecimal.valueOf(Double.parseDouble(txtCost.getText()));
        String vechicleCondition = txtCondition.getText();

        // Updated to use java.sql.Date in the DTO as well
        VechileRentDetailDto vechileRentDetailDto = new VechileRentDetailDto(vehicleId, rentId, startDate, endDate, rentDate, cost, vechicleCondition);

        boolean isUpdated = VehicleRentDetailModel.isVehicleRentUpdated(vechileRentDetailDto);

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Vehicle Rent Updated Successfully").show();
            clearFields();
            refreshPage();
            loadNextVehicleId();
            loadNextRentId();
        } else {
            new Alert(Alert.AlertType.ERROR, "Vehicle Rent Not Updated").show();
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
            txtRentDate.setText(vehicleRentDetailTM.getRent_date().toString());
            txtCost.setText(vehicleRentDetailTM.getCost().toString());
            txtCondition.setText(vehicleRentDetailTM.getVehicle_condition());
            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colVehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
        colRentID.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        colRentDate.setCellValueFactory(new PropertyValueFactory<>("rent_date"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("vehicle_condition"));

        try{
            initialize();
            refreshPage();
            loadNextVehicleId();
            loadNextRentId();
            refreshTableData();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while loading data: "+e.getMessage()).show();

        }
    }
    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadTableData();
        loadNextVehicleId();
        loadNextRentId();
       btnSave.setDisable(false);
       btnUpdate.setDisable(true);
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
                    vechileRentDetailDto.getRent_date(),
                    vechileRentDetailDto.getCost(),
                    vechileRentDetailDto.getVehicle_condition()

            );
            vehicleRentDetailTMS.add(vehicleRentDetailTM);
        }
        tblVehileRent.setItems(vehicleRentDetailTMS);


    }
    public void loadNextVehicleId() throws SQLException, ClassNotFoundException {
       String nextVehicleId=VehicleRentDetailModel.loadNextVehicleId();
        txtVehicleID.setText(nextVehicleId);
    }
    public void loadNextRentId() throws SQLException, ClassNotFoundException {
        String nextRentId=VehicleRentDetailModel.loadNextRentId();
        txtRentId.setText(nextRentId);
    }
}
