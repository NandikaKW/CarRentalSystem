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
import lk.ijse.gdse.carrentalsystem.dto.RentDto;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.model.RentModel;
import lk.ijse.gdse.carrentalsystem.tm.RentTM;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentServiceController  implements Initializable {

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnGenerateReport;

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
    void btnResetOnAction(ActionEvent event) {
        txtRentId.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtCustomerId.setText("");
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
                  loadNextCustomerId();
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
    void btnReporOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String rentId = txtRentId.getText();
        String startDateStr = txtStartDate.getText();  // renamed variable
        String endDateStr = txtEndDate.getText();      // renamed variable
        String custId = txtCustomerId.getText();

        // Define a date format that matches your input format (e.g., "yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the date strings into Date objects
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            RentDto dto = new RentDto(rentId, startDate, endDate, custId);
            boolean isSaved = RentModel.saveRent(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Rent saved successfully").show();
                loadNextCustomerId();
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
        loadNextRentId();
        loadNextCustomerId();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }


    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String RentId = txtRentId.getText();
        if(RentId.isEmpty()){
            new Alert(Alert.AlertType.INFORMATION,"Please enter a Rent ID to search").show();
            return;
        }
        try{
            RentDto rent=RentModel.SearchRent(RentId);
            if(rent!=null){
                txtRentId.setText(rent.getRent_id());
                txtStartDate.setText(rent.getStartDate().toString());
                txtEndDate.setText(rent.getEndDate().toString());
                txtCustomerId.setText(rent.getCust_id());
                new Alert(Alert.AlertType.INFORMATION,"Rent found").show();

            }else{
                new Alert(Alert.AlertType.INFORMATION,"Rent not found").show();
                loadNextCustomerId();
                loadNextRentId();
                clearFields();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while searching for Rent: "+e.getMessage()).show();

        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String RentId = txtRentId.getText();
        String startDateText = txtStartDate.getText();
        String endDateText=txtEndDate.getText();
        String custId=txtCustomerId.getText();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Parse the strings into Date objects
            Date startDate = dateFormat.parse(startDateText);
            Date endDate = dateFormat.parse(endDateText);

            RentDto dto = new RentDto(RentId, startDate, endDate, custId);
            boolean isUpdated = RentModel.updateRent(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Rent updated successfully").show();
                refreshPage();
                loadNextCustomerId();
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
        RentTM rentTM=tblRent.getSelectionModel().getSelectedItem();
        if(rentTM!=null){
           txtRentId.setText(rentTM.getRent_id());
           txtStartDate.setText(rentTM.getStartDate().toString());
           txtEndDate.setText(rentTM.getEndDate().toString());
           txtCustomerId.setText(rentTM.getCust_id());
        }

    }
    public void loadNextRentId() throws SQLException, ClassNotFoundException {
        String nextRentId = RentModel.loadNextRentId();
        txtRentId.setText(nextRentId);
    }
    public  void loadNextCustomerId() throws SQLException, ClassNotFoundException {
       String nextCustomerId= CustomerModel.loadNextCustomerId();
       txtCustomerId.setText(nextCustomerId);
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        try{
            loadNextRentId();
            loadNextCustomerId();
            refreshTableData();

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to load Rent data").show();

        }
    }


    private void loadTaleData() throws SQLException, ClassNotFoundException {
        ArrayList<RentDto> rentDtos= RentModel.getAllRentData();
        ObservableList<RentTM> rentTMS= FXCollections.observableArrayList();
        for(RentDto rentDto:rentDtos){
            RentTM rentTM=new RentTM(
                    rentDto.getRent_id(),
                    rentDto.getStartDate(),
                    rentDto.getEndDate(),
                    rentDto.getCust_id()
            );
            rentTMS.add(rentTM);

        }
        tblRent.setItems(rentTMS);


    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<RentDto> rentDtos=RentModel.getAllRentData();
        ObservableList<RentTM> rentTMS=FXCollections.observableArrayList();
        for(RentDto dto:rentDtos){
            rentTMS.add(new RentTM(
                    dto.getRent_id(),
                    dto.getStartDate(),
                    dto.getEndDate(),
                    dto.getCust_id()
            ));

        }
        tblRent.setItems(rentTMS);
    }





}
