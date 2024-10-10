package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.carrentalsystem.dto.EmployeeDto;
import lk.ijse.gdse.carrentalsystem.model.EmployeeModel;
import lk.ijse.gdse.carrentalsystem.tm.EmployeeTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

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
            new Alert(Alert.AlertType.WARNING,"Please enter a Employee ID to delete!").show();
            return;
        }
        try{
               boolean isDeleted =EmployeeModel.deleteEmployee(employeeID);
               if(isDeleted){
                   new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();

                   loadNextEmployeeId();
               }else{
                   new Alert(Alert.AlertType.ERROR, "Failed to delete Employee!").show();

               }

        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting Employee: " + e.getMessage()).show();


        }

    }

    @FXML
    void btnGenerateReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        txtEmployeeID.setText("");
        txtEmployeeName.setText("");
        txtAdress.setText("");
        txtJobRole.setText("");
        txtSalary.setText("");
        txtAdminID.setText("");

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
           String empId=txtEmployeeID.getText();
           String empName=txtEmployeeName.getText();
           String address=txtAdress.getText();
           String job=txtJobRole.getText();
           String salary=txtSalary.getText();
           String adminID=txtAdminID.getText();

        EmployeeDto employeeDto=new EmployeeDto(empId,empName,address,job,salary,adminID);
        try{
            boolean isSaved=new EmployeeModel().saveEmployee(employeeDto);
            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Employee saved successfully!").show();

            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to save employee!").show();
            }

        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while saving employee: " + e.getMessage()).show();

        }
    }
    public void clear() {

        txtEmployeeID.setText("");
        txtEmployeeName.setText("");
        txtAdress.setText("");
        txtJobRole.setText("");
        txtSalary.setText("");
        txtAdminID.setText("");

    }
    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String employeeId = txtEmployeeID.getText();
        try{
            EmployeeDto employee= EmployeeModel.SearchEmployee(employeeId);
            if(employee!=null){
                txtEmployeeID.setText(employee.getEmp_id());
                txtEmployeeName.setText(employee.getEmp_name());
                txtAdress.setText(employee.getAddress());
                txtJobRole.setText(employee.getJob());
                txtSalary.setText(employee.getSalary());
                txtAdminID.setText(employee.getAdmin_id());
                new Alert(Alert.AlertType.INFORMATION,"Employee found successfully!").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"Employee not found!").show();
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while searching for employee: " + e.getMessage()).show();

        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String EmployeeId = txtEmployeeID.getText();
        String EmployeeName=txtEmployeeName.getText();
        String Address=txtAdress.getText();
        String Job=txtJobRole.getText();
        String Salary=txtSalary.getText();
        String AdminId=txtAdminID.getText();

        EmployeeDto employeeDto=new EmployeeDto(EmployeeId,EmployeeName,Address,Job,Salary,AdminId);
        try{
            boolean isUpdated=EmployeeModel.updateEmployee(employeeDto);
            if(isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Employee updated successfully!").show();

            }else{
                new Alert(Alert.AlertType.ERROR,"Employee not updated!").show();

            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while updating employee: " +e.getMessage()).show();


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

        EmployeeTM employeeTM1 = new EmployeeTM("E001", "John Doe", "123 Main St", "Manager", "50000", "A001");
        EmployeeTM employeeTM2 = new EmployeeTM("E002", "Jane Smith", "456 Elm St", "Assistant Manager", "45000", "A001");
        EmployeeTM employeeTM3 = new EmployeeTM("E003", "Robert Johnson", "789 Oak St", "Clerk", "40000", "A002");


        ArrayList<EmployeeTM> EmployeeArray=new ArrayList<>();
        EmployeeArray.add(employeeTM1);
        EmployeeArray.add(employeeTM2);
        EmployeeArray.add(employeeTM3);

        ObservableList<EmployeeTM> employeeTMS= FXCollections.observableArrayList();
        for(EmployeeTM employee:EmployeeArray){
            employeeTMS.add(employee);
        }
        tblEmployee.setItems(employeeTMS);
        try{
            loadNextEmployeeId();
            loadNextAdminId();
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
           new Alert(Alert.AlertType.ERROR,"Failed to load next employee ID").show();
        }

    }
    public void loadNextEmployeeId() throws SQLException,ClassNotFoundException{
        String nextEmployeeId=EmployeeModel.loadNextEmployeeId();
        txtEmployeeID.setText(nextEmployeeId);
    }
    public void loadNextAdminId() throws SQLException, ClassNotFoundException {
        String nextAdminId = EmployeeModel.loadNextAdminId();
        txtAdminID.setText(nextAdminId);
    }


}
