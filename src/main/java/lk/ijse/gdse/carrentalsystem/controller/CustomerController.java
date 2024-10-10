package lk.ijse.gdse.carrentalsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.tm.CustomerTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();

        if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to delete!").show();
            return;
        }

        try {
            boolean isDeleted = customerModel.deleteCustomer(customerId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                clear();
                loadNextCustomerId();

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting customer: " + e.getMessage()).show();
        }
    }

    @FXML
    private Button btnSearch;


    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtCustomerID.setText("");
        txtCustomerName.setText("");
        txtAdress.setText("");
        txtCustomerNumber.setText("");
        txtNIC.setText("");
        txtAdminID.setText("");



    }

    public void clear() {

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
        String contactNumber = txtCustomerNumber.getText();
        String nic = txtNIC.getText();
        String adminId = txtAdminID.getText();
        CustomerDto dto = new CustomerDto(customerId, customerName, address, contactNumber, nic, adminId);
        boolean isSaved = customerModel.saveCustomer(dto);
        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Save  successfully!").show();
            loadNextCustomerId();
            clear();


        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to Save Customer ....!").show();
        }


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String customerId = txtCustomerID.getText();
        String customername = txtCustomerName.getText();
        String address = txtAdress.getText();
        String contact = txtCustomerNumber.getText();
        String nicNumber = txtNIC.getText();
        String adminid = txtAdminID.getText();
        CustomerDto dto = new CustomerDto(customerId, customername, address, contact, nicNumber, adminid);

        try {
            boolean isUpdated = customerModel.updateCustomer(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating customer: " + e.getMessage()).show();


        }


    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();

        try {
            CustomerDto customer = customerModel.searchCustomer(customerId);

            if (customer != null) {

                txtCustomerID.setText(customer.getCust_id());
                txtCustomerName.setText(customer.getCust_name());
                txtAdress.setText(customer.getAddress());
                txtCustomerNumber.setText(customer.getContact());
                txtNIC.setText(customer.getNic());
                txtAdminID.setText(customer.getAdmin_id());

                new Alert(Alert.AlertType.INFORMATION, "Customer found successfully!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching for customer: " + e.getMessage()).show();
        }

    }

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnRest;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;
    @FXML
    private TableView<CustomerTM> tblCustomer;

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
    private Label lblNIC;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cust_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerNumber.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));

        colAdminID.setCellValueFactory(new PropertyValueFactory<>("admin_id"));

      CustomerTM customerTM1 = new CustomerTM("C001", "John Doe", "123 Main St", "0771234567", "987654321V", "A001");
        CustomerTM customerTM2 = new CustomerTM("C002", "Jane Smith", "456 Elm St", "0789876543", "876543210X", "A002");
       CustomerTM customerTM3 = new CustomerTM("C003", "Bob Johnson", "789 Oak St", "0754567890", "765432109Y", "A003");
        ArrayList<CustomerTM> customerArray = new ArrayList<>();
       customerArray.add(customerTM1);
      customerArray.add(customerTM2);
        customerArray.add(customerTM3);
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (CustomerTM customer : customerArray) {
            customerTMS.add(customer);

        }
        tblCustomer.setItems(customerTMS);
        try {
            loadNextCustomerId();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load next customer id").show();
        }
    }

    CustomerModel customerModel = new CustomerModel();

    public void loadNextCustomerId() throws SQLException, ClassNotFoundException {
        String nextCustomerId = customerModel.loadNextCustomerId();
        txtCustomerID.setText(nextCustomerId);
    }


}
