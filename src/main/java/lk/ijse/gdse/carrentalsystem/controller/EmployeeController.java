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
import lk.ijse.gdse.carrentalsystem.dto.EmployeeDto;
import lk.ijse.gdse.carrentalsystem.model.AdminModel;
import lk.ijse.gdse.carrentalsystem.model.EmployeeModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.EmployeeTM;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {


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

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblAdminID;

    @FXML
    private Label lblEmployeeID;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblJobRole;

    @FXML
    private Label lblSalary;

    @FXML
    private TextField txtAdminID;

    @FXML
    private TextField txtAdress;

    @FXML
    private TextField txtEmployeeID;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtJobRole;

    @FXML
    private TextField txtSalary;

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String employeeID = txtEmployeeID.getText();

        // Check if the employee ID is empty
        if (employeeID.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter employee ID").show();
            return;
        }

        // Confirm with the user before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Try to delete the employee from the model
                boolean isDeleted = EmployeeModel.deleteEmployee(employeeID);

                // If deletion was successful, show an info alert
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
                    clearFields();
                    loadCurrentAdminId();
                    loadNextEmployeeId();
                    refreshTableData();
                } else {
                    // If deletion failed, show an error alert
                    new Alert(Alert.AlertType.ERROR, "Failed to delete employee!").show();
                }

            } catch (SQLException e) {
                // Catch any SQL exceptions and show an error alert
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred while deleting employee: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Catch any ClassNotFoundException and show an error alert
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Class not found error occurred while deleting employee: " + e.getMessage()).show();
            } catch (Exception e) {
                // Catch any other unexpected exceptions and show a generic error alert
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            }
        }

    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDto> employeeDtos=EmployeeModel.getAllEmployees();
        ObservableList<EmployeeTM> employeeTMS=FXCollections.observableArrayList();
        for(EmployeeDto dto:employeeDtos){
            EmployeeTM employeeTM=new EmployeeTM(
                    dto.getEmp_id(),
                    dto.getEmp_name(),
                    dto.getAddress(),
                    dto.getJob(),
                    dto.getSalary(),
                    dto.getAdmin_id()
            );
            employeeTMS.add(employeeTM);


        }
        tblEmployee.setItems(employeeTMS);
    }


    private  void clearFields(){
        txtEmployeeID.setText("");
        txtEmployeeName.setText("");
        txtAdress.setText("");
        txtJobRole.setText("");
        txtSalary.setText("");
        txtAdminID.setText("");
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadCurrentAdminId();
       // loadNextAdminId();
        loadNextEmployeeId();
       // loadCurrentEmployeeId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String empId = txtEmployeeID.getText();
        String empName = txtEmployeeName.getText();
        String address = txtAdress.getText();
        String job = txtJobRole.getText();
        String adminID = txtAdminID.getText();
        BigDecimal salary;

        // Regex patterns
        String empIdPattern = "^E\\d{3}$"; // Matches E001, E002, etc.
        String empNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String addressPattern = "^[\\w\\s,.#-]+$"; // Allows letters, numbers, spaces, and common punctuation
        String jobPattern = "^[A-Za-z ]+$"; // Allows letters and spaces only for job role
        String adminIdPattern = "^A\\d{3}$"; // Matches A001, A002, etc.

        // Validation checks
        boolean isValidEmpId = empId.matches(empIdPattern);
        boolean isValidEmpName = empName.matches(empNamePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidJob = job.matches(jobPattern);
        boolean isValidAdminId = adminID.matches(adminIdPattern);

        // Reset field styles
        resetFieldStyles();

        // Highlight invalid fields and show messages
        if (!isValidEmpId) {
            txtEmployeeID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Employee ID format!").show();
        }
        if (!isValidEmpName) {
            txtEmployeeName.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Employee Name format!").show();
        }
        if (!isValidAddress) {
            txtAdress.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Address format!").show();
        }
        if (!isValidJob) {
            txtJobRole.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Job Role format!").show();
        }
        if (!isValidAdminId) {
            txtAdminID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.WARNING, "Invalid Admin ID format!").show();
        }

        // Validate salary
        try {
            salary = new BigDecimal(txtSalary.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid salary format!").show();
            return;
        }

        // If all fields are valid, proceed to save the employee
        if (isValidEmpId && isValidEmpName && isValidAddress && isValidJob && isValidAdminId) {
            EmployeeDto employeeDto = new EmployeeDto(empId, empName, address, job, salary, adminID);

            try {
                // Try to save the employee
                boolean isSaved = EmployeeModel.saveEmployee(employeeDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
                    loadCurrentAdminId();
                    loadNextEmployeeId();
                    refreshPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Employee!").show();
                }
            } catch (SQLException e) {
                // Handle database-related exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Handle class not found exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
            } catch (Exception e) {
                // Catch any other unexpected exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
            }
        }
    }

    private void resetFieldStyles() {
        txtEmployeeID.setStyle("");
        txtEmployeeName.setStyle("");
        txtAdress.setStyle("");
        txtJobRole.setStyle("");
        txtSalary.setStyle("");
        txtAdminID.setStyle("");
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String employeeId = txtEmployeeID.getText();

        // Check if the employee ID field is empty
        if (employeeId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter employee ID").show();
            return; // Stop further execution if the ID is empty
        }

        try {
            // Search for the employee using the provided ID
            EmployeeDto employee = EmployeeModel.SearchEmployee(employeeId);

            // Check if the employee is found
            if (employee != null) {
                // Populate the fields with employee data
                txtEmployeeID.setText(employee.getEmp_id());
                txtEmployeeName.setText(employee.getEmp_name());
                txtAdress.setText(employee.getAddress());
                txtJobRole.setText(employee.getJob()); // Fixed the incorrect setText for job role
                new Alert(Alert.AlertType.INFORMATION, "Employee found!").show(); // Show success alert
            } else {
                // If employee is not found, show an error alert
                new Alert(Alert.AlertType.ERROR, "Employee not found!").show();
                clearFields(); // Clear the fields
                loadCurrentAdminId(); // Load current admin ID
                loadNextEmployeeId(); // Load next employee ID
            }

        } catch (SQLException e) {
            // Handle SQLException and display a specific alert
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException and display a specific alert
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String employeeId = txtEmployeeID.getText();
        String employeeName = txtEmployeeName.getText();
        String address = txtAdress.getText();
        String job = txtJobRole.getText();
        String adminId = txtAdminID.getText();
        BigDecimal salary;

        // Regex patterns
        String empIdPattern = "^E\\d{3}$"; // Matches E001, E002, etc.
        String empNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String addressPattern = "^[\\w\\s,.#-]+$"; // Allows letters, numbers, spaces, and common punctuation
        String jobPattern = "^[A-Za-z ]+$"; // Allows letters and spaces only for job role
        String adminIdPattern = "^A\\d{3}$"; // Matches A001, A002, etc.

        // Validation checks
        boolean isValidEmpId = employeeId.matches(empIdPattern);
        boolean isValidEmpName = employeeName.matches(empNamePattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidJob = job.matches(jobPattern);
        boolean isValidAdminId = adminId.matches(adminIdPattern);

        // Reset field styles
        resetFieldStyles();

        // Highlight invalid fields and show appropriate alerts
        if (!isValidEmpId) {
            txtEmployeeID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID. Expected format: E001, E002, etc.").show();
        }
        if (!isValidEmpName) {
            txtEmployeeName.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Name. Only letters and spaces are allowed.").show();
        }
        if (!isValidAddress) {
            txtAdress.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Address. Only letters, numbers, and common punctuation are allowed.").show();
        }
        if (!isValidJob) {
            txtJobRole.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Job Role. Only letters and spaces are allowed.").show();
        }
        if (!isValidAdminId) {
            txtAdminID.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Admin ID. Expected format: A001, A002, etc.").show();
        }

        // Validate salary
        try {
            salary = new BigDecimal(txtSalary.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid salary format! Please enter a valid number.").show();
            return; // Stop further execution if salary format is invalid
        }

        // If all fields are valid, proceed to update
        if (isValidEmpId && isValidEmpName && isValidAddress && isValidJob && isValidAdminId) {
            EmployeeDto employeeDto = new EmployeeDto(employeeId, employeeName, address, job, salary, adminId);

            try {
                boolean isUpdated = EmployeeModel.updateEmployee(employeeDto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
                    loadCurrentAdminId(); // Load current admin ID
                    loadNextEmployeeId(); // Load next employee ID
                    refreshPage(); // Refresh the page to reflect changes
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update employee!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                // Handle any SQL or class not found exceptions that may occur during the update
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
            }
        }

    }
    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TableColumn<EmployeeTM, String> colAddress;

    @FXML
    private TableColumn<EmployeeTM, String> colAdminID;

    @FXML
    private TableColumn<EmployeeTM, String> colEmployeeName;


    @FXML
    private TableColumn<EmployeeTM, String> colEmplyeeID;

    @FXML
    private TableColumn<EmployeeTM, String> colJobRole;

    @FXML
    private TableColumn<EmployeeTM, String> colSalary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colEmplyeeID.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("emp_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("job"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        colAdminID.setCellValueFactory(new PropertyValueFactory<>("admin_id"));

        try {
            // Refreshing the page and loading necessary IDs and table data
            refreshPage();
            loadNextEmployeeId();
            loadCurrentAdminId();
            refreshTableData();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred while loading employee data: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred: " + e.getMessage()).show();
        }
    }
    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextEmployeeId();

        //loadNextAdminId();
        loadCurrentAdminId();
        loadTableDta();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }
    private  void loadTableDta() throws SQLException, ClassNotFoundException {
        ArrayList<EmployeeDto> employeeDtos=EmployeeModel.getAllEmployees();
        ObservableList<EmployeeTM> employeeTMS=FXCollections.observableArrayList();
        for(EmployeeDto  employeeDto:employeeDtos){
            EmployeeTM employeeTM=new EmployeeTM(
                   employeeDto.getEmp_id(),
                    employeeDto.getEmp_name(),
                    employeeDto.getAddress(),
                    employeeDto.getJob(),
                    employeeDto.getSalary(),
                    employeeDto.getAdmin_id()



            );
            employeeTMS.add(employeeTM);


        }
        tblEmployee.setItems(employeeTMS);
    }



    public void loadNextEmployeeId() throws SQLException,ClassNotFoundException{
        String nextEmployeeId=EmployeeModel.loadNextEmployeeId();
        txtEmployeeID.setText(nextEmployeeId);
    }

    public void loadCurrentAdminId() throws SQLException, ClassNotFoundException {
        String currentAdminId = AdminModel.loadCurrentAdminId();
        txtAdminID.setText(currentAdminId);
    }



    public void onClickedEmployeeTable(MouseEvent mouseEvent) {
        EmployeeTM employeeTM= tblEmployee.getSelectionModel().getSelectedItem();
        if(employeeTM!=null){
            txtEmployeeID.setText(employeeTM.getEmp_id());
            txtEmployeeName.setText(employeeTM.getEmp_name());
            txtAdress.setText(employeeTM.getAddress());
            txtJobRole.setText(employeeTM.getJob());

            try {
                BigDecimal salaryDecimal = new BigDecimal(String.valueOf(employeeTM.getSalary()));
                txtSalary.setText(salaryDecimal.setScale(2, RoundingMode.HALF_UP).toString());
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                btnSave.setDisable(true);// format to 2 decimal places
            } catch (NumberFormatException e) {
                txtSalary.setText("0.00"); // In case of invalid number format, set a default value
            }

            txtAdminID.setText(employeeTM.getAdmin_id());
        }
    }


}
