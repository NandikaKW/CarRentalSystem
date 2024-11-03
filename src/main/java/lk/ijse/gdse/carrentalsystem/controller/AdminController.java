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
import lk.ijse.gdse.carrentalsystem.dto.AdminDto;
import lk.ijse.gdse.carrentalsystem.model.AdminModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.AdminTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {


    public JFXButton btnReset;
    public JFXButton btnDelete;
    public JFXButton btnSave;
    public JFXButton btnUpdate;

    @FXML
    private Label lblAdminID;

    @FXML
    private Label lblAdminName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPassword;

    @FXML
    private TextField txtAdminID;

    @FXML
    private TextField txtAdminName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;
    @FXML
    private JFXButton btnSearch;


    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String adminId=txtAdminID.getText();
        if(adminId.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Please enter a Admin ID to delete!").show();
            return;
        }


             Alert alert=new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this admin?", ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> optionalButtonType=alert.showAndWait();
            if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
             try {
                 boolean isDeleted=AdminModel.deleteAdmin(adminId);
                 if(isDeleted){
                     new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                     clearField(); // Clear fields after deletion
                     refreshTableData(); // Refresh table to remove deleted record
                     loadNextAdminId(); // Reset ID field to next available


                 }else{
                     new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
                 }

             }catch (SQLException | ClassNotFoundException e){
                 e.printStackTrace();
                 new Alert(Alert.AlertType.ERROR,"Error occurred while deleting Admin: "+e.getMessage()).show();

             }
            }

    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<AdminDto> adminDtos=AdminModel.getAllAdmins();
        ObservableList<AdminTM> adminTMS=FXCollections.observableArrayList();
        for(AdminDto dto:adminDtos){
            AdminTM adminTM=new AdminTM(
                    dto.getAdmin_id(),
                    dto.getUserName(),
                    dto.getEmail(),
                    dto.getPassword()
            );
            adminTMS.add(adminTM);

        }
        tblAdmin.setItems(adminTMS);

    }


    private void clearField() {
        txtAdminID.setText("");
        txtAdminName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");

    }



    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String adminId=txtAdminID.getText();
       if(adminId.isEmpty()){
           new Alert(Alert.AlertType.WARNING,"Please enter a Admin ID to search!").show();
         return;
       }
       try{
           AdminDto admin=AdminModel.searchAdmin(adminId);
           if(admin!=null){
               txtAdminID.setText(admin.getAdmin_id());
               txtAdminName.setText(admin.getUserName());
               txtEmail.setText(admin.getEmail());
               txtPassword.setText(admin.getPassword());
               new Alert(Alert.AlertType.INFORMATION,"Admin found!").show();



           }else{
               new Alert(Alert.AlertType.WARNING,"Admin not found!").show();
               loadNextAdminId();
               clearField();

           }

       }catch (SQLException | ClassNotFoundException e){
           e.printStackTrace();
           new Alert(Alert.AlertType.ERROR,"Error occurred while searching for Admin: "+e.getMessage()).show();

       }
    }


    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String adminId = txtAdminID.getText();
        String userName = txtAdminName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Regex patterns
        String adminIdPattern = "^A\\d{3}$"; // Matches A001, A002, etc.
        String userNamePattern = "^[A-Za-z ]+$"; // Allows letters and spaces only
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // Valid email
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"; // At least 8 characters, at least 1 letter and 1 number

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidAdminId = adminId.matches(adminIdPattern);
        boolean isValidUserName = userName.matches(userNamePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPassword = password.matches(passwordPattern);

        // Highlight invalid fields
        if (!isValidAdminId) {
            txtAdminID.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Admin ID.");
        }

        if (!isValidUserName) {
            txtAdminName.setStyle("-fx-border-color: red;");
            System.out.println("Invalid User Name.");
        }

        if (!isValidEmail) {
            txtEmail.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Email.");
        }

        if (!isValidPassword) {
            txtPassword.setStyle("-fx-border-color: red;");
            System.out.println("Invalid Password.");
        }

        // If all fields are valid, proceed to save
        if (isValidAdminId && isValidUserName && isValidEmail && isValidPassword) {
            AdminDto adminDto = new AdminDto(adminId, userName, email, password);
            boolean isSaved = AdminModel.saveAdmin(adminDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Admin saved successfully!").show();
                clearField();
                refreshPage();
                loadNextAdminId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save admin!").show();
            }
        }
    }

    // Method to reset styles for all fields
    private void resetFieldStyles() {
        txtAdminID.setStyle(""); // Reset to default
        txtAdminName.setStyle(""); // Reset to default
        txtEmail.setStyle(""); // Reset to default
        txtPassword.setStyle(""); // Reset to default
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String adminId = txtAdminID.getText();
        String userName = txtAdminName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        AdminDto adminDto = new AdminDto(adminId, userName, email, password);
        boolean isUpdated = AdminModel.updateAdmin(adminDto);

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Admin updated successfully!").show();
            refreshPage();
            loadNextAdminId();

            // Ensure you call refreshTableData() here to refresh the table
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update admin!").show();
        }
    }

    @FXML
    private TableColumn<AdminTM, String> colAdminID;

    @FXML
    private TableColumn<AdminTM, String> colAdminName;

    @FXML
    private TableColumn<AdminTM, String> colEmail;

    @FXML
    private TableColumn<AdminTM, String> colPassword;
    @FXML
    private TableView<AdminTM> tblAdmin;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colAdminID.setCellValueFactory(new PropertyValueFactory<>("Admin_id"));
        colAdminName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        try{
            refreshPage();
            loadNextAdminId();
            refreshTableData();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load admin data").show();
        }
    }
    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextAdminId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearField();

    }
    private  void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<AdminDto> adminDtos=AdminModel.getAllAdmins();
        ObservableList<AdminTM> adminTMS=FXCollections.observableArrayList();
        for(AdminDto  adminDto:adminDtos){
            AdminTM adminTM=new AdminTM(
                    adminDto.getAdmin_id(),
                    adminDto.getUserName(),
                    adminDto.getEmail(),
                    adminDto.getPassword()
            );
            adminTMS.add(adminTM);


        }
        tblAdmin.setItems(adminTMS);
    }


    public void loadNextAdminId() throws SQLException, ClassNotFoundException {
        String nextAdminId = AdminModel.loadNextAdminId();
        txtAdminID.setText(nextAdminId);
    }


    public void onClickTable(MouseEvent mouseEvent) {
        AdminTM adminTM=tblAdmin.getSelectionModel().getSelectedItem();
        if(adminTM!=null){
            txtAdminID.setText(adminTM.getAdmin_id());
            txtAdminName.setText(adminTM.getUserName());
            txtEmail.setText(adminTM.getEmail());
            txtPassword.setText(adminTM.getPassword());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }
    }


}
