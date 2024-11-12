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
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.dto.tm.CartTM;
import lk.ijse.gdse.carrentalsystem.dto.tm.ReserveTM;
import lk.ijse.gdse.carrentalsystem.model.*;
import lk.ijse.gdse.carrentalsystem.dto.tm.RentTM;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class RentServiceController  implements Initializable {

    @FXML
    private JFXButton btnBookVehicle;

    @FXML
    private JFXButton btnReserveVehicle;

    @FXML
    private TableColumn<RentTM, String> colAgrimentID;

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
    private ComboBox<String> cmbPackageId;

    @FXML
    private ComboBox<String> cmbVehicleId;

    @FXML
    private ComboBox<String> cmbCondition;

    @FXML
    private JFXButton btnDelete;
    @FXML
    private TextField txtRentId;


    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<RentTM, String> colCustomerID;

    @FXML
    private TableColumn<RentTM, Date> colEndDate;

    @FXML
    private TableColumn<RentTM, String> colRentId;

    @FXML
    private TableColumn<RentTM, Date> colStartDate;

    @FXML
    private TableColumn<ReserveTM, String> colPackageId;

    @FXML
    private TableColumn<ReserveTM, Integer> colQty;

    @FXML
    private TableColumn<ReserveTM, Button> colRemove;

    @FXML
    private TableColumn<ReserveTM, String> colVehicleId;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblCondition;

    @FXML
    private Label lblColor;

    @FXML
    private Label lblModel;

    @FXML
    private Label lblPackageID;

    @FXML
    private Label lblQty;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblCustomerID;

    @FXML
    private Label lblEndDate;

    @FXML
    private Label lblRentId;

    @FXML
    private Label lblStartDate;

    @FXML
    private Label lblVehicleID;

    @FXML
    private TableView<CartTM> tblCart;

    @FXML
    private TableView<RentTM> tblRent;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtEndDate;



    @FXML
    private TextField txtStartDate;

    @FXML
    private TextField txtAgrimentID;

    @FXML
    private TextField txtQty;

    @FXML
    private JFXButton btnReset;
    private final  RentModel rentModel=new RentModel();
    private final CustomerModel customerModel=new CustomerModel();
    private  final VehicleModel vehicleModel=new VehicleModel();
    private final PackageModel packageModel=new PackageModel();
    private final ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

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
        loadCmbVehicle();
        loadCmbPackage();
        loadCmbCondiion();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
        tblCart.setItems(cartTMS);
        cartTMS.clear();
        tblCart.refresh();
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

    public void loadCurrentAgreementId() throws SQLException, ClassNotFoundException {
        String currentAgreementId = AgrimentModel.loadCurrentAgreementId();
        txtAgrimentID.setText(currentAgreementId);
    }

    public void loadCmbVehicle() throws SQLException, ClassNotFoundException {
        ArrayList<String> vehicleIds = vehicleModel.getAllVehicleIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(vehicleIds);
        cmbVehicleId.setItems(observableList);
    }

    public void loadCmbPackage() throws SQLException, ClassNotFoundException {
        ArrayList<String> packageIds = packageModel.getAllPackageIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(packageIds);
        cmbPackageId.setItems(observableList);
    }

    public void loadCmbCondiion(){
        if (cmbCondition != null) {
            String[] conditions = {"New", "Old", "Used"};
            cmbCondition.getItems().addAll(conditions);
        } else {
            System.err.println("cmbCondition is null");
        }
    }

     @Override
    public void initialize(URL location, ResourceBundle resources) {
         if (cmbCondition == null) {
             System.err.println("cmbCondition is not initialized properly");
         }

         colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
         colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
         colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
         colCustomerID.setCellValueFactory(new PropertyValueFactory<>("custId"));
         colAgrimentID.setCellValueFactory(new PropertyValueFactory<>("agreementId"));

         setCellValueCart();
         tblCart.setItems(cartTMS);

         try {
             refreshPage();
             loadNextRentId();
             loadCurrentCustomerId();
             refreshTableData();

             updateYears();
             updateMonths();

             ComboYear.getSelectionModel().selectFirst();
             CombMonth.getSelectionModel().selectFirst();
             updateDays();

             ComboYearOne.getSelectionModel().selectFirst();
             CombMonthOne.getSelectionModel().selectFirst();
             updateDaysOne();

         } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
             new Alert(Alert.AlertType.ERROR, "Failed to load Rent data").show();
         }
    }

    private void setCellValueCart() {
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("PackageId"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));

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
    @FXML
    void btnBookVehicleOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (tblCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add vehicles to cart..!").show();
            return;
        }
        if (cmbVehicleId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select vehicle for place order..!").show();
            return;
        }

        // Load the correct next rent ID
        String rentId = RentModel.loadNextRentId();
        txtRentId.setText(rentId); // update the text field with the new rent ID

        ArrayList<VechileRentDetailDto> vechileRentDetailDtos = new ArrayList<>();

        // Date format to parse input strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse start and end dates from input fields
            Date startDate = dateFormat.parse(txtStartDate.getText());
            Date endDate = dateFormat.parse(txtEndDate.getText());

            // Collect data for each item in the cart and add to order details array
            for (CartTM cartTM : cartTMS) {
                VechileRentDetailDto vechileRentDetailDto = new VechileRentDetailDto(
                        cmbVehicleId.getValue(),
                        rentId, // use updated rent ID
                        startDate,
                        endDate,
                        cmbCondition.getValue(),
                        Integer.parseInt(txtQty.getText())
                );
                vechileRentDetailDtos.add(vechileRentDetailDto);
            }

            RentDto rentDto = new RentDto(
                    rentId, // use updated rent ID
                    startDate,
                    endDate,
                    txtCustomerId.getText(),
                    txtAgrimentID.getText(),
                    vechileRentDetailDtos
            );

            boolean isSaved = RentModel.saveRent(rentDto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Order saved..!").show();
                refreshPage(); // This should ideally call loadNextRentId() to update the next available ID
            } else {
                new Alert(Alert.AlertType.ERROR, "Order failed..!").show();
            }

        } catch (ParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format! Please use yyyy-MM-dd.").show();
        }
    }


    @FXML
    void btnReserveVehicleOnAction(ActionEvent event) {
        String selectedVehicleId = cmbVehicleId.getValue();


        if (selectedVehicleId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select item..!").show();
            return;
        }

        int cartQty = Integer.parseInt(txtQty.getText());
        int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());


        if (qtyOnHand < cartQty) {
            new Alert(Alert.AlertType.ERROR, "Not enough items..!").show();
            return;
        }

        // Loop through each item in cart's observable list to check if the item already exists
        for (CartTM cartTM : cartTMS) {
            if (cartTM.getVehicle_id().equals(selectedVehicleId)) {
                // Update quantity and recalculate the total
                int newQty = Integer.parseInt(cartTM.getQuantity()) + cartQty;
                cartTM.setQuantity(String.valueOf(newQty));

                // Refresh the table to display the updated information
                tblCart.refresh();
                return; // Exit the method as the cart item has been updated
            }
        }

        // Create a "Remove" button for the new item
        Button btn = new Button("Remove");
        String packageId = cmbPackageId.getValue();

        // Create a new CartTM object to represent the item if it does not already exist
        CartTM newCartTM = new CartTM(
                selectedVehicleId,
                String.valueOf(cartQty),
                packageId,
                btn
        );

        // Set an action for the "Remove" button to remove the item from the cart when clicked
        btn.setOnAction(actionEvent -> {
            cartTMS.remove(newCartTM);
            tblCart.refresh();
        });

        // Add the new item to the cart's observable list
        cartTMS.add(newCartTM);
        tblCart.setItems(cartTMS);
        tblCart.refresh();
    }


    @FXML
    void cmbPackageOnAction(ActionEvent event) {

    }

    @FXML
    void cmbVehicleIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedVehicleId = cmbVehicleId.getSelectionModel().getSelectedItem();
        VehicleDto vehicleDto = vehicleModel.searchVehicle(selectedVehicleId); // Get the selected vehicleDTO = itemModel.findById(selectedItemId);

        // If item found (itemDTO not null)
        if (vehicleDto != null) {

            // FIll item related labels
            lblModel.setText(vehicleDto.getModel());
            lblColor.setText(vehicleDto.getColour());
            lblCategory.setText(vehicleDto.getCategory());
            lblQtyOnHand.setText(String.valueOf(vehicleDto.getQuantity()));
        }
    }

    @FXML
    void tblCartOnClick(MouseEvent event) {

    }

    @FXML
    void cmbConditionOnAction(ActionEvent event) {

    }
}







