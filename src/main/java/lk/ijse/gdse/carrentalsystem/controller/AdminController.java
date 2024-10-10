package lk.ijse.gdse.carrentalsystem.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse.carrentalsystem.dto.AdminDto;
import lk.ijse.gdse.carrentalsystem.model.AdminModel;
import lk.ijse.gdse.carrentalsystem.tm.AdminTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

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
        try{
            boolean isDeleted=AdminModel.deleteAdmin(adminId);
            if(isDeleted){
                new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                clear();
                loadNextAdminId();


            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer!").show();
            }



        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting customer: " + e.getMessage()).show();

        }

    }

    @FXML
    void btnGenerateOnAction(ActionEvent event) {

    }
    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String adminId=txtAdminID.getText();
        try{
               AdminDto admin=AdminModel.searchAdmin(adminId);
        if(admin!=null){
            txtAdminID.setText(admin.getAdmin_id());
            txtAdminName.setText(admin.getUserName());
            txtEmail.setText(admin.getEmail());
            txtPassword.setText(admin.getPassword());
            new Alert(Alert.AlertType.INFORMATION,"Admin  found SucessFully!").show();

        }else{
            new Alert(Alert.AlertType.WARNING,"Admin not found!").show();

        }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"ERROR occured while searching for admin:"+e.getMessage()).show();

        }

    }


    @FXML
    void btnResetOnAction(ActionEvent event) {

        txtAdminID.setText("");
        txtAdminName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String adminId = txtAdminID.getText();
        String userName = txtAdminName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        AdminDto adminDto=new AdminDto(adminId,userName,email,password);
        try{
            boolean isSaved=new AdminModel().saveAdmin(adminDto);
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION, "Admin saved successfully!").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to save admin!").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();

        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String adminId = txtAdminID.getText();
        String userName = txtAdminName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        AdminDto adminDto=new AdminDto(adminId,userName,email,password);
        try{
            boolean isUpdated = new AdminModel().updateAdmin(adminDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Admin updated successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update admin!").show();
            }


        }catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating admin: " + e.getMessage()).show();
            e.printStackTrace();


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

        AdminTM adminTM1=new AdminTM("A001","John Doe","LQJbJ@example.com","123456");
        AdminTM adminTM2=new AdminTM("A002","Jane Smith","YI8vH@example.com","654321");
        AdminTM adminTM3=new AdminTM("A003","Bob Johnson","1YzC8@example.com","987654");

        ArrayList<AdminTM> AdminArray=new ArrayList<>();
        AdminArray.add(adminTM1);
       AdminArray.add(adminTM2);
        AdminArray.add(adminTM3);

        ObservableList<AdminTM> adminTMS= FXCollections.observableArrayList();
        for(AdminTM Admin:AdminArray){
            adminTMS.add(Admin);

        }
        tblAdmin.setItems(adminTMS);
        try {

            loadNextAdminId();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load next admin ID").show();
        }
    }


    public void loadNextAdminId() throws SQLException, ClassNotFoundException {
        String nextAdminId = AdminModel.loadNextAdminId();
        txtAdminID.setText(nextAdminId);
    }
    public void clear() {

        txtAdminID.setText("");
        txtAdminName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");

    }


}
