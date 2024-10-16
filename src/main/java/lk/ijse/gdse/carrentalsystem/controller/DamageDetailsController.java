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
import lk.ijse.gdse.carrentalsystem.dto.DamageDto;
import lk.ijse.gdse.carrentalsystem.dto.EmployeeDto;
import lk.ijse.gdse.carrentalsystem.model.DamageModel;
import lk.ijse.gdse.carrentalsystem.model.EmployeeModel;
import lk.ijse.gdse.carrentalsystem.tm.DamageTM;
import lk.ijse.gdse.carrentalsystem.tm.EmployeeTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DamageDetailsController implements Initializable {

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
    private TableColumn<DamageTM, String> colDamageId;

    @FXML
    private TableColumn<DamageTM, String> colDescription;

    @FXML
    private TableColumn<DamageTM, String> colRentId;

    @FXML
    private TableColumn<DamageTM, BigDecimal> colRepairCost;

    @FXML
    private Label lblDamageId;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblRentId;

    @FXML
    private Label lblRepairCost;

    @FXML
    private TableView<DamageTM> tblDamage;

    @FXML
    private TextField txtDamageId;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtRentId;

    @FXML
    private TextField txtRepairCost;

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String damageId = txtDamageId.getText();
        if (damageId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter damage ID").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this damage?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                boolean isDeleted = DamageModel.deleteDamage(damageId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Damage deleted successfully!").show();
                    clearFields();
                    loadNextDamageId();
                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete damage!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "ERROR occurred while deleting damage: " + e.getMessage()).show();
            }
        }
    }
    private  void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<DamageDto> damageDtos=DamageModel.getAllDamages();
        ObservableList<DamageTM> damageTMS=FXCollections.observableArrayList();

        for(DamageDto  dto:damageDtos){
            DamageTM damageTM=new DamageTM(
                    dto.getDamage_id(),
                    dto.getRepair_cost(),
                    dto.getDetail(),
                    dto.getRent_id()
            );
            damageTMS.add(damageTM);

        }
        tblDamage.setItems(damageTMS);
    }
    private  void clearFields(){
        txtDamageId.setText("");
        txtDescription.setText("");
        txtRepairCost.setText("");
        txtRentId.setText("");
    }
    @FXML
    void OnClickedTable(MouseEvent event) {
        DamageTM damageTM=tblDamage.getSelectionModel().getSelectedItem();
        if(damageTM!=null){
            txtDamageId.setText(damageTM.getDamage_id());
            txtRepairCost.setText(String.valueOf(damageTM.getRepair_cost()));
            txtDescription.setText(damageTM.getDetail());
            txtRentId.setText(damageTM.getRent_id());



        }

    }


    @FXML
    void btnReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String damageId = txtDamageId.getText();
        BigDecimal repairCost = new BigDecimal(txtRepairCost.getText());
        String detail=txtDescription.getText();
        String rentId=txtRentId.getText();
        try{
                   DamageDto damageDto=new DamageDto(damageId,repairCost,detail,rentId);
                   boolean isSaved=DamageModel.saveDamage(damageDto);
        if(isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Damage saved successfully!").show();
            loadNextDamageId();
            refreshTableData();
        }else{
            new Alert(Alert.AlertType.ERROR,"Failed to save damage!").show();
        }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"ERROR occurred while saving damage: "+e.getMessage()).show();
        }

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String damageId = txtDamageId.getText();
        if (damageId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter Damage ID").show();
            return;
        }

        try {
            System.out.println("Searching for Damage ID: " + damageId); // Debugging line

            DamageDto damage = DamageModel.searchDamage(damageId);

            if (damage != null) {
                System.out.println("Damage Found: " + damage.toString()); // Debugging line

                txtDamageId.setText(damage.getDamage_id());
                txtRepairCost.setText(damage.getRepair_cost().toString());
                txtDescription.setText(damage.getDetail());
                txtRentId.setText(damage.getRent_id());
                new Alert(Alert.AlertType.INFORMATION, "Damage Found!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Damage not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching damage: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String damageId = txtDamageId.getText();
        if (damageId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter damage ID").show();
            return;
        }

        BigDecimal repairCost;
        try {
            repairCost = new BigDecimal(txtRepairCost.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Cost").show();
            return;
        }

        String detail = txtDescription.getText();
        String rentId = txtRentId.getText();

        DamageDto damageDto = new DamageDto(damageId, repairCost, detail, rentId);

        // Execute update
        boolean isUpdated = DamageModel.updateDamage(damageDto);

        if (isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Damage Detail updated successfully!").show();
            clearFields();
            refreshTableData();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update Damage Detail!").show();
        }
    }


    @FXML
    void tblDamgeOnAction(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colDamageId.setCellValueFactory(new PropertyValueFactory<>("damage_id"));
        colRepairCost.setCellValueFactory(new PropertyValueFactory<>("repair_cost"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("detail"));
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rent_id"));
        try{
            loadNextDamageId();
            refreshTableData();


        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load Damage Details!").show();

        }
    }
    private  void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextDamageId();
        loadTableDta();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();

    }
    private  void loadTableDta() throws SQLException, ClassNotFoundException {
        ArrayList<DamageDto> damageDtos=DamageModel.getAllDamages();
        ObservableList<DamageTM> damageTMS=FXCollections.observableArrayList();
        for(DamageDto damageDto:damageDtos){
            DamageTM damageTM=new DamageTM(
                    damageDto.getDamage_id(),
                    damageDto.getRepair_cost(),
                    damageDto.getDetail(),
                    damageDto.getRent_id()
            );
            damageTMS.add(damageTM);

        }
        tblDamage.setItems(damageTMS);
    }
    public  void loadNextDamageId() throws SQLException, ClassNotFoundException {
        String nextDamageID=DamageModel.loadNextDamageID();
        txtDamageId.setText(nextDamageID);
    }


}
