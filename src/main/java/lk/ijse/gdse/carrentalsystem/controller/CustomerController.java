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
import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.model.AdminModel;
import lk.ijse.gdse.carrentalsystem.model.CustomerModel;
import lk.ijse.gdse.carrentalsystem.tm.CustomerTM;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CustomerController implements Initializable {
    public JFXButton btnBack;
    CustomerModel customerModel = new CustomerModel();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("cust_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerNumber.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));

        colAdminID.setCellValueFactory(new PropertyValueFactory<>("admin_id"));
        try {
            refreshPage();
            loadNextAdminId();
            loadNextCustomerId();
            refreshTableData();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load customer data").show();
        }

    }
    private void refreshPage() throws SQLException, ClassNotFoundException {
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
                    customerDTO.getContact(),
                    customerDTO.getNic(),
                    customerDTO.getAdmin_id()
            );
            customerTMS.add(customerTM);
        }
        tblCustomer.setItems(customerTMS);
    }


    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnReport;

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

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();

        if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to delete!").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                boolean isDeleted = customerModel.deleteCustomer(customerId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                    clearFields();
                    refreshTableData();
                    loadNextCustomerId();
                    loadNextAdminId();

                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting customer: " + e.getMessage()).show();
            }
        }


    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass()
                            .getResourceAsStream("/report/CustomerDetailReport.jrxml"
                            ));

            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();


            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    connection
            );

            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to generate report...!").show();
          e.printStackTrace();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "DB error...!").show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();


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
        String contactNumber = txtCustomerNumber.getText();
        String nic = txtNIC.getText();
        String adminId = txtAdminID.getText();

        CustomerDto dto = new CustomerDto(customerId, customerName, address, contactNumber, nic, adminId);
        boolean isSaved = customerModel.saveCustomer(dto);
        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Save  successfully!").show();
            refreshPage();
            loadNextCustomerId();
            loadNextAdminId();
        } else {
            new Alert(Alert.AlertType.ERROR, "Fail to Save Customer ....!").show();
        }



    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String customerId = txtCustomerID.getText();
        if (customerId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to search!").show();
            return;
        }

        try {
            CustomerDto customer = customerModel.searchCustomer(customerId);
            if (customer != null) {
                txtCustomerID.setText(customer.getCust_id());
                txtCustomerName.setText(customer.getCust_name());
                txtAdress.setText(customer.getAddress());
                txtCustomerNumber.setText(customer.getContact());
                txtNIC.setText(customer.getNic());
                txtAdminID.setText(customer.getAdmin_id());
                new Alert(Alert.AlertType.INFORMATION, "Customer found!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer not found!").show();
                clearFields();

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching for customer: " + e.getMessage()).show();
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
        boolean isUpdated = customerModel.updateCustomer(dto);
        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
            refreshPage();
            loadNextCustomerId();
            loadNextAdminId();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
        }


    }

    @FXML
    void onClickTable(MouseEvent event) {
        CustomerTM customerTM = tblCustomer.getSelectionModel().getSelectedItem();
        if (customerTM != null){
            txtCustomerID.setText(customerTM.getCust_id());
            txtCustomerName.setText(customerTM.getCust_name());
            txtAdress.setText(customerTM.getAddress());
            txtCustomerNumber.setText(customerTM.getContact());
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
    // Load the next admin ID
    // Load the current Admin ID
    public void loadNextAdminId() throws SQLException, ClassNotFoundException {
        String loadNextAdminId =AdminModel.loadNextAdminId();
        txtAdminID.setText(loadNextAdminId);
    }


    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDto> customerDtos = customerModel.getAllCustomer();
        ObservableList<CustomerTM> customerTMS = FXCollections.observableArrayList();
        for (CustomerDto dto : customerDtos) {
            customerTMS.add(new CustomerTM(
                    dto.getCust_id(),
                    dto.getCust_name(),
                    dto.getAddress(),
                    dto.getContact(),
                    dto.getNic(),
                    dto.getAdmin_id()
            ));
        }
        tblCustomer.setItems(customerTMS);
    }


    public void btnBackOnAction(ActionEvent actionEvent) {
    }
}
