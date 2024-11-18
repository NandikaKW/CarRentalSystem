package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.dto.CustomerPaymentDto;
import lk.ijse.gdse.carrentalsystem.dto.PaymentDto;
import lk.ijse.gdse.carrentalsystem.dto.tm.SubmitTM;
import lk.ijse.gdse.carrentalsystem.model.AdminModel;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.CustomerTM;
import lk.ijse.gdse.carrentalsystem.model.PaymentModel;
import lk.ijse.gdse.carrentalsystem.model.RentModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerController implements Initializable {

    public TableColumn colCustomerEmail;
    CustomerModel customerModel = new CustomerModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cust_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAdminID.setCellValueFactory(new PropertyValueFactory<>("admin_id"));
        setCellValueCart();

        try {

            refreshPage();
            loadCurrentAdminId();
            loadNextCustomerId();
            refreshTableData();
        } catch (SQLException e) {

            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while loading customer data: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Required class not found: " + e.getMessage()).show();
        } catch (Exception e) {

            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }

    }

    private void refreshPage() throws SQLException, ClassNotFoundException {
        setDateAndOrderId();
        loadAllRentIds();
        loadAllPaymentIds();
        loadNextCustomerId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDto> customerDTOS = customerModel.getAllCustomers();
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (CustomerDto customerDTO : customerDTOS) {
            CustomerTM customerTM = new CustomerTM(
                    customerDTO.getCust_id(),
                    customerDTO.getCust_name(),
                    customerDTO.getAddress(),
                    customerDTO.getEmail(),
                    customerDTO.getNic(),
                    customerDTO.getAdmin_id()
            );
            customerTMS.add(customerTM);
        }
        tblCustomer.setItems(customerTMS);
    }
    @FXML
    private TableView<SubmitTM> tblSubmit;

    @FXML
    private TableColumn<SubmitTM,Button> colRemove;

    @FXML
    private TableColumn<SubmitTM, String> colCustId;

    @FXML
    private TableColumn<SubmitTM, String> colPaymentID;

    @FXML
    private TableColumn<SubmitTM, String> colRentID;

    @FXML
    private TableColumn<SubmitTM, BigDecimal> colPaymentAmount;


    @FXML
    private Label lblPaymentAmount;


    @FXML
    private TextField txtPaymentAmount;

    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnPreparePayment;
    @FXML
    private JFXButton btnSubmitPayment;


    @FXML
    private Label lblPaymentID;

    @FXML
    private ComboBox<String> cmbPayemntId;

    @FXML
    private ComboBox<String> cmbRentID;

    @FXML
    private JFXButton btnRest;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<CustomerTM, String> colAddress;

    @FXML
    private TableColumn<CustomerTM, String> colAdminID;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerID;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerName;

    @FXML
    private TableColumn<CustomerTM, String> colCustomerNumber;

    @FXML
    private TableColumn<CustomerTM, String> colNIC;

    @FXML
    private Label lblAdminID;

    @FXML
    private Label lblAdress;

    @FXML
    private Label lblContactNumber;

    @FXML
    private Label lblCustomerID;

    @FXML
    private Label lblCustomerName;
    @FXML
    private Label lblPaymentDate;

    @FXML
    private TextField txtPaymentDate;

    @FXML
    private Label lblNIC;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TextField txtAdminID;

    @FXML
    private TextField txtAdress;

    @FXML
    private TextField txtCustomerID;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerNumber;

    @FXML
    private TextField txtNIC;
    private final ObservableList<SubmitTM> submitTMS = FXCollections.observableArrayList();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();

        // Check if Customer ID is provided
        if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to delete!").show();
            return;
        }

        // Confirm deletion action
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Attempt to delete the customer
                boolean isDeleted = customerModel.deleteCustomer(customerId);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                    clearFields();         // Clear input fields
                    refreshTableData();     // Refresh the table
                    loadNextCustomerId();   // Load the next available customer ID
                    loadCurrentAdminId();   // Load the current admin ID

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred while deleting customer: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Required class not found: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
            }
        }

    }



    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadNextCustomerId();
        loadCurrentAdminId();



    }
    private void setDateAndOrderId() {

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d =df.format(date);
        txtPaymentDate.setText(d);
        // txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void clearFields() {
        txtCustomerID.setText("");
        txtCustomerName.setText("");
        txtAdress.setText("");
        txtCustomerNumber.setText("");
        txtNIC.setText("");
        txtAdminID.setText("");
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String customerId = txtCustomerID.getText();
        String customerName = txtCustomerName.getText();
        String address = txtAdress.getText();
        String email = txtCustomerNumber.getText();
        String nic = txtNIC.getText();
        String adminId = txtAdminID.getText();

        // Regex patterns
        String customerIdPattern = "^C\\d{3}$"; // Matches C001, C002, etc.
        String customerNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String addressPattern = "^[\\w\\s,.#-]+$"; // Allows letters, numbers, spaces, and common punctuation
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // Valid email format
        String nicPattern = "^[0-9]{9}[Vv]$|^[0-9]{12}$"; // Matches NIC format with 9 digits + V or 12 digits
        String adminIdPattern = "^A\\d{3}$"; // Matches A001, A002, etc.

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidCustomerId = customerId.matches(customerIdPattern);
        boolean isValidCustomerName = customerName.matches(customerNamePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidAdminId = adminId.matches(adminIdPattern);

        // Highlight invalid fields
        if (!isValidCustomerId) {
            txtCustomerID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Customer ID format!").show();
        }

        if (!isValidCustomerName) {
            txtCustomerName.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Customer Name format!").show();
        }

        if (!isValidAddress) {
            txtAdress.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Address format!").show();
        }

        if (!isValidEmail) {
            txtCustomerNumber.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Email format!").show();
        }

        if (!isValidNic) {
            txtNIC.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid NIC format!").show();
        }

        if (!isValidAdminId) {
            txtAdminID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Admin ID format!").show();
        }

        // If all fields are valid, proceed to save
        if (isValidCustomerId && isValidCustomerName && isValidAddress && isValidEmail && isValidNic && isValidAdminId) {
            CustomerDto dto = new CustomerDto(customerId, customerName, address, email, nic, adminId);

            try {
                boolean isSaved = customerModel.saveCustomer(dto);

                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully!").show();
                    refreshPage();
                    loadNextCustomerId();
                    loadCurrentAdminId();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save customer!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Required class not found: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage()).show();
            }
        }
    }

    private void resetFieldStyles() {
        txtCustomerID.setStyle("");
        txtCustomerName.setStyle("");
        txtAdress.setStyle("");
        txtCustomerNumber.setStyle("");
        txtNIC.setStyle("");
        txtAdminID.setStyle("");
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();

        // Check if customer ID is empty
        if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to search!").show();
            return;
        }

        try {
            // Try searching for the customer
            CustomerDto customer = customerModel.searchCustomer(customerId);

            // If customer is found, populate fields
            if (customer != null) {
                txtCustomerID.setText(customer.getCust_id());
                txtCustomerName.setText(customer.getCust_name());
                txtAdress.setText(customer.getAddress());
                txtCustomerNumber.setText(customer.getEmail());
                txtNIC.setText(customer.getNic());
                txtAdminID.setText(customer.getAdmin_id());
                new Alert(Alert.AlertType.INFORMATION, "Customer found!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found!").show();
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while searching for customer: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }

    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String customerId = txtCustomerID.getText();
        String customerName = txtCustomerName.getText();
        String address = txtAdress.getText();
        String email = txtCustomerNumber.getText(); // Assuming txtCustomerNumber is used for email input
        String nic = txtNIC.getText();
        String adminId = txtAdminID.getText();

        // Regex patterns
        String customerIdPattern = "^C\\d{3}$"; // Matches C001, C002, etc.
        String customerNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String addressPattern = "^[\\w\\s,.#-]+$"; // Allows letters, numbers, spaces, and common punctuation
        String emailPattern = "^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // Valid email format
        String nicPattern = "^[0-9]{9}[Vv]$|^[0-9]{12}$"; // Matches NIC format with 9 digits + V or 12 digits
        String adminIdPattern = "^A\\d{3}$"; // Matches A001, A002, etc.

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidCustomerId = customerId.matches(customerIdPattern);
        boolean isValidCustomerName = customerName.matches(customerNamePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidAdminId = adminId.matches(adminIdPattern);

        // Highlight invalid fields
        if (!isValidCustomerId) {
            txtCustomerID.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Customer ID.");
        }

        if (!isValidCustomerName) {
            txtCustomerName.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Customer Name.");
        }

        if (!isValidAddress) {
            txtAdress.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Address.");
        }

        if (!isValidEmail) {
            txtCustomerNumber.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Email.");
        }

        if (!isValidNic) {
            txtNIC.setStyle("-fx-border-color: red;");
            System.out.println("Invalid NIC.");
        }

        if (!isValidAdminId) {
            txtAdminID.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Admin ID.");
        }

        // If all fields are valid, proceed to update
        if (isValidCustomerId && isValidCustomerName && isValidAddress && isValidEmail && isValidNic && isValidAdminId) {
            try {
                CustomerDto dto = new CustomerDto(customerId, customerName, address, email, nic, adminId);
                boolean isUpdated = customerModel.updateCustomer(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
                    refreshPage();
                    loadNextCustomerId();
                    loadCurrentAdminId();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred while updating customer: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please correct the highlighted fields before updating!").show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();
        if (customerTM != null) {
            txtCustomerID.setText(customerTM.getCust_id());
            txtCustomerName.setText(customerTM.getCust_name());
            txtAdress.setText(customerTM.getAddress());
            txtCustomerNumber.setText(customerTM.getEmail());
            txtNIC.setText(customerTM.getNic());
            txtAdminID.setText(customerTM.getAdmin_id());
            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }


    }

    public void loadNextCustomerId() throws SQLException, ClassNotFoundException {
        String nextCustomerId = customerModel.loadNextCustomerId();
        txtCustomerID.setText(nextCustomerId);
    }


    public void loadCurrentAdminId() throws SQLException, ClassNotFoundException {
        String currentAdminId = AdminModel.loadCurrentAdminId();
        txtAdminID.setText(currentAdminId);
    }



    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDto> customerDtos = customerModel.getAllCustomer();
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (CustomerDto dto : customerDtos) {
            customerTMS.add(new CustomerTM(
                    dto.getCust_id(),
                    dto.getCust_name(),
                    dto.getAddress(),
                    dto.getEmail(),
                    dto.getNic(),
                    dto.getAdmin_id()
            ));
        }
        tblCustomer.setItems(customerTMS);
    }




    @FXML
    public void openSendMailModel(ActionEvent actionEvent) {
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer.").show();
            return;
        }

        try {
            // Attempt to load the email view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Email.fxml"));
            Parent load = loader.load();

            // Initialize EmailController and set email data
            EmailController emailController = loader.getController();
            emailController.setCustomerEmail(selectedItem.getEmail());

            // Configure and display the stage
            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Send Email");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/icons8-open-email-24.png")));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set the parent window to create modal behavior
            Window parentWindow = ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.initOwner(parentWindow);
            stage.showAndWait();

        } catch (IOException e) {

            new Alert(Alert.AlertType.ERROR, "Failed to load the email sending interface. Please try again later.").show();
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Handle null resource or component issues
            new Alert(Alert.AlertType.ERROR, "Some required resources are missing. Please contact support.").show();
            e.printStackTrace();
        } catch (Exception e) {
            // Handle any other unforeseen errors
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    public  void loadAllPaymentIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> PaymentIds = PaymentModel.getAllPaymentIDs();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(PaymentIds);
        cmbPayemntId.setItems(observableList);
    }
    public  void loadAllRentIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> RentIds = RentModel.getAllRentIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(RentIds);
        cmbRentID.setItems(observableList);
    }
   public void setCellValueCart() {
        // Initialize table columns
        colPaymentID.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colRentID.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colPaymentAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("removeBtn"));


        tblSubmit.setItems(submitTMS);
    }

    @FXML
    void cmbPayemntIdOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

    }
    @FXML
    void cmbRentIDOnAction(ActionEvent event) {

    }
    @FXML
    void btnSubmitPaymentOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, ParseException {
        try {
            // Check if the table has payment details
            if (tblSubmit.getItems().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please add payment details.").show();
                return;
            }

            // Check if a payment ID is selected
            if (cmbPayemntId.getSelectionModel().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select a payment ID.").show();
                return;
            }

            // Calculate the total payment amount from the table
            BigDecimal totalPaymentAmount = BigDecimal.ZERO;
            for (SubmitTM submitTM : tblSubmit.getItems()) {
                totalPaymentAmount = totalPaymentAmount.add(submitTM.getAmount());
            }

            // Parse the required payment amount
            BigDecimal requiredPaymentAmount;
            try {
                requiredPaymentAmount = new BigDecimal(txtPaymentAmount.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid payment amount entered.").show();
                return;
            }

            // Validate the total payment amount against the required amount
            if (totalPaymentAmount.compareTo(requiredPaymentAmount) > 0) {
                new Alert(Alert.AlertType.ERROR, "Total payment amount exceeds the required payment amount.").show();
                return;
            } else if (totalPaymentAmount.compareTo(requiredPaymentAmount) < 0) {
                new Alert(Alert.AlertType.ERROR, "Total payment amount is less than the required payment amount.").show();
                return;
            }

            // Load the next customer ID
            String customerId = CustomerModel.loadNextCustomerId();
            txtCustomerID.setText(customerId);

            // Prepare payment details
            ArrayList<CustomerPaymentDto> customerPaymentDtos = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Parse payment date
            Date date;
            try {
                date = dateFormat.parse(txtPaymentDate.getText());
            } catch (ParseException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use 'yyyy-MM-dd'.").show();
                return;
            }

            // Populate payment DTOs from the table
            for (SubmitTM submitTM : tblSubmit.getItems()) {
                CustomerPaymentDto customerPaymentDto = new CustomerPaymentDto(
                        submitTM.getCustId(),
                        submitTM.getPaymentId(),
                        date,
                        submitTM.getAmount()
                );
                customerPaymentDtos.add(customerPaymentDto);
            }

            // Prepare customer DTO
            CustomerDto customerDto = new CustomerDto(
                    txtCustomerID.getText(),
                    txtCustomerName.getText(),
                    txtAdress.getText(),
                    txtCustomerNumber.getText(),
                    txtNIC.getText(),
                    txtAdminID.getText()
            );
            customerDto.setCustomerPaymentDtos(customerPaymentDtos);

            // Save customer and handle the result
            boolean isSaved = CustomerModel.saveCustomer(customerDto);

            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer saved successfully.").show();

                CustomerTM customerTM = new CustomerTM(
                        customerDto.getCust_id(),
                        customerDto.getCust_name(),
                        customerDto.getAddress(),
                        customerDto.getEmail(),
                        customerDto.getNic(),
                        customerDto.getAdmin_id()
                );
                tblCustomer.getItems().add(customerTM);

                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save customer.").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    // Method to reset form fields after successful payment submission
    private void refreshPaymentForm() {
        txtPaymentAmount.clear();
        txtCustomerID.clear();
        txtPaymentDate.clear();

    }
    @FXML
    void btnPreparePaymentOnAction(ActionEvent event) {
        String paymentId = cmbPayemntId.getValue();
        String rentId = cmbRentID.getValue();
        BigDecimal payAmount;

        // Input validation
        if (paymentId == null || paymentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select the payment ID.").show();
            return;
        }

        if (rentId == null || rentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select the rent ID.").show();
            return;
        }

        try {
            payAmount = new BigDecimal(txtPaymentAmount.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid payment amount.").show();
            return;
        }

        // Check the available amount for the selected payment ID
        BigDecimal availableAmount;
        try {
            availableAmount = PaymentModel.getAvailablePaymentAmount(paymentId);
            if (payAmount.compareTo(availableAmount) > 0) {
                new Alert(Alert.AlertType.ERROR, "The payment amount exceeds the available amount for this payment ID.").show();
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error while fetching available payment amount: " + e.getMessage()).show();
            e.printStackTrace();
            return;
        }

        String custId = txtCustomerID.getText();

        // Check if payment already exists and update the amount
        for (SubmitTM submitTM : submitTMS) {
            if (submitTM.getPaymentId().equals(paymentId)) {
                BigDecimal newAmount = submitTM.getAmount().add(payAmount);

                // Validate if the new amount exceeds the available amount
                if (newAmount.compareTo(availableAmount) > 0) {
                    new Alert(Alert.AlertType.ERROR, "The total payment amount exceeds the available amount.").show();
                    return;
                }

                submitTM.setAmount(newAmount);
                tblSubmit.refresh();
                return;
            }
        }

        // Create a new SubmitTM entry with a remove button
        Button removeBtn = new Button("Remove");
        SubmitTM newSubmitTM = new SubmitTM(paymentId, rentId, custId, payAmount, removeBtn);
        removeBtn.setOnAction(e -> {
            submitTMS.remove(newSubmitTM);
            tblSubmit.refresh();
        });

        // Add the new entry to the list and refresh the table
        submitTMS.add(newSubmitTM);
        tblSubmit.setItems(submitTMS);
        tblSubmit.refresh();
    }





}




