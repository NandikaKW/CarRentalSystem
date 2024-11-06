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
        String employeeID=txtEmployeeID.getText();
        if(employeeID.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Please enter employee ID").show();
            return;
        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this employee?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try {
                boolean isDeleted=EmployeeModel.deleteEmployee(employeeID);
                if(isDeleted){
                    new Alert(Alert.AlertType.INFORMATION,"Employee deleted successfully!").show();
                    clearFields();
                    loadNextAdminId();
                    loadNextEmployeeId();
                    refreshTableData();

                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed to delete employee!").show();
                }

            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting employee: "+e.getMessage()).show();

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
        loadNextAdminId();
        loadNextEmployeeId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
           String empId=txtEmployeeID.getText();
           String empName=txtEmployeeName.getText();
           String address=txtAdress.getText();
           String job=txtJobRole.getText();
        BigDecimal salary;

        try {
            salary = new BigDecimal(txtSalary.getText()); // Validate and log salary
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid salary format!").show();
            return;
        }

        String adminID = txtAdminID.getText();

        EmployeeDto employeeDto = new EmployeeDto(empId, empName, address, job, salary, adminID);

        try {
            boolean isSaved = EmployeeModel.saveEmployee(employeeDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
                loadNextAdminId();
                loadNextEmployeeId();
                refreshPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save Employee!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String employeeId = txtEmployeeID.getText();
      if(employeeId.isEmpty()){
          new Alert(Alert.AlertType.WARNING,"Please enter employee ID").show();
          return;

      }
      try{
          EmployeeDto employee= EmployeeModel.SearchEmployee(employeeId);
          if(employee!=null){
              txtEmployeeID.setText(employee.getEmp_id());
              txtEmployeeName.setText(employee.getEmp_name());
              txtAdress.setText(employee.getAddress());
              txtJobRole.setText(employee.getAddress());
              txtJobRole.setText(employee.getJob());
              txtAdress.setText(employee.getAddress());
          new Alert(Alert.AlertType.INFORMATION,"Employee found!").show();
          }else{
              new Alert(Alert.AlertType.ERROR,"Employee not found!").show();
              clearFields();
              loadNextAdminId();
              loadNextEmployeeId();

          }

      }catch (SQLException | ClassNotFoundException e){
          e.printStackTrace();
          new Alert(Alert.AlertType.ERROR,"Error occurred while searching employee: "+e.getMessage()).show();

      }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String EmployeeId = txtEmployeeID.getText();
        String EmployeeName=txtEmployeeName.getText();
        String Address=txtAdress.getText();
        String Job=txtJobRole.getText();
        BigDecimal Salary = new BigDecimal(txtSalary.getText());
        String AdminId=txtAdminID.getText();

        EmployeeDto employeeDto=new EmployeeDto(EmployeeId,EmployeeName,Address,Job,Salary,AdminId);
              boolean isUpdated=EmployeeModel.updateEmployee(employeeDto);

       if(isUpdated){
           new Alert(Alert.AlertType.INFORMATION,"Employee updated successfully!").show();
           loadNextAdminId();
           loadNextEmployeeId();
           refreshPage();

       }else{
           new Alert(Alert.AlertType.ERROR,"Failed to update employee!").show();

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

        try{
            refreshPage();
          loadNextEmployeeId();
          loadNextAdminId();
          refreshTableData();

       }catch (SQLException | ClassNotFoundException e){
           e.printStackTrace();
           new Alert(Alert.AlertType.ERROR,"Failed to load Employee").show();

       }


    }
    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextEmployeeId();
        loadNextAdminId();
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
    public void loadNextAdminId() throws SQLException, ClassNotFoundException {
        String nextAdminId = AdminModel.loadNextAdminId();
        txtAdminID.setText(nextAdminId);
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
