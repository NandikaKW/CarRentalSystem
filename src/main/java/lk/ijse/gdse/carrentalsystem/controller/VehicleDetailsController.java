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
import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.model.VehicleModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.VehicleTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class VehicleDetailsController  implements Initializable {

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
    private TableColumn<VehicleTM, String> colCategory;

    @FXML
    private TableColumn<VehicleTM, String> colColour;

    @FXML
    private TableColumn<VehicleTM, String> colModel;

    @FXML
    private TableColumn<VehicleTM, String> colPackageId;

    @FXML
    private TableColumn<VehicleTM, Integer> colQuantity;

    @FXML
    private TableColumn<VehicleTM, String> colVehicleId;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblColour;

    @FXML
    private Label lblModel;

    @FXML
    private Label lblPackageId;

    @FXML
    private Label lblQuantity;

    @FXML
    private Label lblVehicleId;

    @FXML
    private TableView<VehicleTM> tblVehicle;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtColour;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtPackageId;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtVehicleId;

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String  vehicleId= txtVehicleId.getText();
        if(vehicleId.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter Vehicle Id").show();
            return;

        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this vehicle?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
               boolean isDeleted= VehicleModel.deleteVehicle(vehicleId);
               if(isDeleted){
                   new Alert(Alert.AlertType.INFORMATION, "Vehicle Deleted Successfully").show();
                   clearFields();
                   loadNextVehicleId();
                   loadNextPackageId();
                   refreshTableData();

               }else{
                   new Alert(Alert.AlertType.ERROR, "Failed To Delete Vehicle").show();
               }

            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting vehicle: "+e.getMessage()).show();

            }

        }

    }
    private  void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<VehicleDto> vehicleDtos=VehicleModel.getAllVehicles();
        ObservableList<VehicleTM> vehicleTMS= FXCollections.observableArrayList();
        for(VehicleDto dto:vehicleDtos){
            VehicleTM vehicleTM=new VehicleTM(
                    dto.getVehicle_id(),
                    dto.getModel(),
                    dto.getColour(),
                    dto.getCategory(),
                    dto.getQuantity(),
                    dto.getPackage_id()
            );
            vehicleTMS.add(vehicleTM);

        }
        tblVehicle.setItems(vehicleTMS);
    }
    private  void clearFields(){
        txtVehicleId.setText("");
        txtModel.setText("");
        txtColour.setText("");
        txtCategory.setText("");
        txtQuantity.setText("");
        txtPackageId.setText("");

    }

    @FXML
    void btnGenerateOnAction(ActionEvent event) {

    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId=txtVehicleId.getText();
        String model=txtModel.getText();
        String colour=txtColour.getText();
        String category=txtCategory.getText();
        int quantity=Integer.parseInt(txtQuantity.getText());
        String packageId=txtPackageId.getText();
         VehicleDto vehicleDto=new VehicleDto(vehicleId,model,colour,category,quantity,packageId);
         boolean isSaved=VehicleModel.saveVehicle(vehicleDto);
        if(isSaved){
            new Alert(Alert.AlertType.INFORMATION, "Vehicle Saved Successfully").show();
            refreshPage();
            loadNextVehicleId();
            loadNextPackageId();


        }else{
            new Alert(Alert.AlertType.ERROR, "Failed To Save Vehicle").show();
        }


    }

    @FXML
    void btnSeachOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId=txtVehicleId.getText();
        if(vehicleId.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter Vehicle Id").show();
            return;
        }
        try{
            VehicleDto vehicle=VehicleModel.searchVehicle(vehicleId);
            if(vehicle!=null){
                txtVehicleId.setText(vehicle.getVehicle_id());
                txtModel.setText(vehicle.getModel());
                txtColour.setText(vehicle.getColour());
                txtCategory.setText(vehicle.getCategory());
                txtQuantity.setText(String.valueOf(vehicle.getQuantity()));
                txtPackageId.setText(vehicle.getPackage_id());
                new Alert(Alert.AlertType.INFORMATION, "Vehicle Found").show();

            }else{
                new Alert(Alert.AlertType.ERROR, "Vehicle Not Found").show();
                loadNextPackageId();
                loadNextVehicleId();
                clearFields();
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while searching vehicle: "+e.getMessage()).show();

        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String vehicleId=txtVehicleId.getText();
        String model=txtModel.getText();
        String colour=txtColour.getText();
        String category=txtCategory.getText();
        int quantity=Integer.parseInt(txtQuantity.getText());
        String packageId=txtPackageId.getText();

        VehicleDto vehicleDto=new VehicleDto(vehicleId,model,colour,category,quantity,packageId);
        boolean isUpdated=VehicleModel.updateVehicle(vehicleDto);
        if(isUpdated){
            new Alert(Alert.AlertType.INFORMATION, "Vehicle Updated Successfully").show();
            refreshPage();
            loadNextPackageId();
            loadNextVehicleId();


        }else{
            new Alert(Alert.AlertType.ERROR, "Failed To Update Vehicle").show();
        }




    }

    @FXML
    void onVehicleTblClicked(MouseEvent event) {
        VehicleTM vehicleTM=tblVehicle.getSelectionModel().getSelectedItem();
        if(vehicleTM!=null){
            txtVehicleId.setText(vehicleTM.getVehicle_id());
            txtModel.setText(vehicleTM.getModel());
            txtColour.setText(vehicleTM.getColour());
            txtCategory.setText(vehicleTM.getCategory());
            txtQuantity.setText(String.valueOf(vehicleTM.getQuantity()));
            txtPackageId.setText(vehicleTM.getPackage_id());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicle_id"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colColour.setCellValueFactory(new PropertyValueFactory<>("colour"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPackageId.setCellValueFactory(new PropertyValueFactory<>("package_id"));

        try{
            refreshPage();
            loadNextPackageId();
            loadNextVehicleId();
            refreshTableData();

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while loading data: "+e.getMessage()).show();

        }
    }
    private  void  refreshPage() throws SQLException, ClassNotFoundException {
        loadNextVehicleId();
        loadTableData();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }
    private  void loadTableData() throws  SQLException,ClassNotFoundException{
        ArrayList<VehicleDto> vehicleDtos=VehicleModel.getAllVehicles();
        ObservableList<VehicleTM> vehicleTMS=FXCollections.observableArrayList();
        for (VehicleDto dto:vehicleDtos){
            VehicleTM vehicleTM=new VehicleTM(
                    dto.getVehicle_id(),
                    dto.getModel(),
                    dto.getColour(),
                    dto.getCategory(),
                    dto.getQuantity(),
                    dto.getPackage_id()

            );
            vehicleTMS.add(vehicleTM);
        }
        tblVehicle.setItems(vehicleTMS);

    }
    public  void loadNextVehicleId() throws SQLException, ClassNotFoundException {
        String nextVehicleId=VehicleModel.loadNextVehicleId();
        txtVehicleId.setText(nextVehicleId);
    }
    public void loadNextPackageId() throws SQLException, ClassNotFoundException {
        String nexetPackage=VehicleModel.loadNextPackageId();
        txtPackageId.setText(nexetPackage);
    }

}
