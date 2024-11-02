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
import lk.ijse.gdse.carrentalsystem.dto.PaymentDto;
import lk.ijse.gdse.carrentalsystem.model.PackageModel;
import lk.ijse.gdse.carrentalsystem.model.PaymentModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.PaymentTM;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.IntStream;

public class PaymentTrackingController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("pay_id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoice"));
        colPayMehtod.setCellValueFactory(new PropertyValueFactory<>("method"));
        colTransaction.setCellValueFactory(new PropertyValueFactory<>("transaction_reference"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount_applied"));
        try{
            refreshPage();
            loadTableData();
            loadNextPaymentId();
            refreshTableData();

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();

        }
        initializeDateCombos();
    }
    private void refreshPage() throws SQLException, ClassNotFoundException {
        loadNextPaymentId();
        loadTableData();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
        clearFields();
    }
    private  void loadTableData() throws SQLException, ClassNotFoundException {
        ArrayList<PaymentDto> paymentDtos=PaymentModel.getAllPayments();
        ObservableList<PaymentTM> paymentTMS=FXCollections.observableArrayList();
        for(PaymentDto paymentDto:paymentDtos){
            PaymentTM paymentTM=new PaymentTM(
                    paymentDto.getPay_id(),
                    paymentDto.getAmount(),
                    paymentDto.getDate(),
                    paymentDto.getInvoice(),
                    paymentDto.getMethod(),
                    paymentDto.getTransaction_reference(),
                    paymentDto.getTax(),
                    paymentDto.getDiscount_applied()

            );
            paymentTMS.add(paymentTM);

        }
        tblPayment.setItems(paymentTMS);
    }
    public  void loadNextPaymentId() throws SQLException, ClassNotFoundException {
        String nextPaymentId=PaymentModel.loadNextPaymentId();
        txtPaymentId.setText(nextPaymentId);
    }

    @FXML
    private ComboBox<Integer> CombMonth;

    @FXML
    private ComboBox<Integer> ComboDay;

    @FXML
    private ComboBox<Integer> ComboYear;
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
    private TableColumn<PaymentTM, BigDecimal> colAmount;

    @FXML
    private TableColumn<PaymentTM, Date> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colDiscount;

    @FXML
    private TableColumn<PaymentTM, String> colInvoice;

    @FXML
    private TableColumn<PaymentTM, String> colPayMehtod;

    @FXML
    private TableColumn<PaymentTM, String> colPaymentId;

    @FXML
    private TableColumn<PaymentTM, BigDecimal> colTax;

    @FXML
    private TableColumn<PaymentTM, String> colTransaction;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDiscount;

    @FXML
    private Label lblInvoice;

    @FXML
    private Label lblPayMethods;

    @FXML
    private Label lblPaymentId;

    @FXML
    private Label lblTax;

    @FXML
    private Label lblTransaction;

    @FXML
    private TableView<PaymentTM> tblPayment;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtInvoice;

    @FXML
    private TextField txtPayMethods;

    @FXML
    private TextField txtPaymentId;

    @FXML
    private TextField txtTax;

    @FXML
    private TextField txtTransaction;
    @FXML
    void ComboDayOnAction(ActionEvent event) {
        showSelectedDate();



    }
    private void initializeDateCombos() {
        // Populate ComboYear with the last 50 years up to the current year
        ComboYear.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1970, YearMonth.now().getYear()).boxed().toList()
        ));
        ComboYear.getSelectionModel().selectLast();

        // Populate CombMonth with values 1 to 12
        CombMonth.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 12).boxed().toList()
        ));
        CombMonth.getSelectionModel().selectFirst();

        // Update days based on initial year and month selection
        updateDays();

    }


    @FXML
    void ComboMonthOnAction(ActionEvent event) {

        updateDays();
    }

    @FXML
    void ComboYearOnAction(ActionEvent event) {
        updateDays();

    }
    private void updateDays() {
        Integer year = ComboYear.getValue();
        Integer month = CombMonth.getValue();

        if (year != null && month != null) {
            int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
            ComboDay.setItems(FXCollections.observableArrayList(
                    IntStream.rangeClosed(1, daysInMonth).boxed().toList()
            ));
            ComboDay.getSelectionModel().selectFirst();
            showSelectedDate();
        }

    }

    private void showSelectedDate() {
        Integer year = ComboYear.getValue();
        Integer month = CombMonth.getValue();
        Integer day = ComboDay.getValue();

        if (year != null && month != null && day != null) {
            txtDate.setText(String.format("%04d-%02d-%02d", year, month, day));
        }

    }





    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId = txtPaymentId.getText();
        if(paymentId.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter Payment Id").show();
            return;

        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this payment?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> optionalButtonType=alert.showAndWait();
        if(optionalButtonType.isPresent() && optionalButtonType.get()==ButtonType.YES){
            try{
                boolean isDeleted =PaymentModel.deletePayment(paymentId);
                if(isDeleted){
                    new Alert(Alert.AlertType.INFORMATION,"Payment deleted successfully").show();
                    clearFields();
                    loadNextPaymentId();
                    refreshTableData();

                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed to delete payment").show();
                }

            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error occurred while deleting payment: "+e.getMessage()).show();

            }

        }


    }
    private void refreshTableData() throws SQLException, ClassNotFoundException {
        ArrayList<PaymentDto> paymentDtos=PaymentModel.getAllPayments();
        ObservableList<PaymentTM> paymentTMS= FXCollections.observableArrayList();
        for (PaymentDto dto:paymentDtos){
            PaymentTM paymentTM=new PaymentTM(
                    dto.getPay_id(),
                    dto.getAmount(),
                    dto.getDate(),
                    dto.getInvoice(),
                    dto.getMethod(),
                    dto.getTransaction_reference(),
                    dto.getTax(),
                    dto.getDiscount_applied()
            );
            paymentTMS.add(paymentTM);

        }
        tblPayment.setItems(paymentTMS);
    }
    private  void clearFields(){
        txtPaymentId.setText("");
        txtAmount.setText("");
        txtDate.setText("");
        txtDiscount.setText("");
        txtInvoice.setText("");
        txtPayMethods.setText("");
        txtTax.setText("");
        txtTransaction.setText("");
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
        String paymentId=txtPaymentId.getText();
        BigDecimal amount = new BigDecimal(txtAmount.getText());// line no 1
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Assuming the date format is yyyy-MM-dd
            date = dateFormat.parse(txtDate.getText()); // Parse the text to a Date object
        } catch (ParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd format.").show();
            return;
        }
        String invoice=txtInvoice.getText();
        String method=txtPayMethods.getText();
        String transaction=txtTransaction.getText();
        BigDecimal tax = new BigDecimal(txtTax.getText());    // line no 2
        BigDecimal discount = new BigDecimal(txtDiscount.getText());// line no 3
        PaymentDto paymentDto=new PaymentDto(paymentId,amount,date,invoice,method,transaction,tax,discount);
        boolean isSaved=PaymentModel.savePayment(paymentDto);
        if(isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Payment saved successfully").show();
            refreshPage();
            loadNextPaymentId();

        }else{
            new Alert(Alert.AlertType.ERROR,"Failed to save payment").show();
        }


    }

    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String PaymentId = txtPaymentId.getText();
        if(PaymentId.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter Payment Id").show();
            return;
        }
        try{
            PaymentDto payment= PaymentModel.searchPayment(PaymentId);
            if (payment!=null){
                txtPaymentId.setText(payment.getPay_id());
                txtAmount.setText(String.valueOf(payment.getAmount()));
                txtDate.setText(String.valueOf(payment.getDate()));
                txtInvoice.setText(payment.getInvoice());
                txtPayMethods.setText(payment.getMethod());
                txtTransaction.setText(payment.getTransaction_reference());
                txtTax.setText(String.valueOf(payment.getTax()));
                txtDiscount.setText(payment.getDiscount_applied().toString());

            }else{
                new Alert(Alert.AlertType.ERROR,"Payment Not Found").show();
                loadNextPaymentId();
                clearFields();
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error occurred while searching payment: "+e.getMessage()).show();
        }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId=txtPaymentId.getText();
        BigDecimal amount = new BigDecimal(txtAmount.getText());// line no 1
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Assuming the date format is yyyy-MM-dd
            date = dateFormat.parse(txtDate.getText()); // Parse the text to a Date object
        } catch (ParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd format.").show();
            return;
        }
        String invoice=txtInvoice.getText();
        String method=txtPayMethods.getText();
        String transaction=txtTransaction.getText();
        BigDecimal tax = new BigDecimal(txtTax.getText());    // line no 2
        BigDecimal discount = new BigDecimal(txtDiscount.getText());// line no 3
        PaymentDto paymentDto=new PaymentDto(paymentId,amount,date,invoice,method,transaction,tax,discount);
        boolean isUpdated= PaymentModel.UpdatePayment(paymentDto);
        if(isUpdated){

            new Alert(Alert.AlertType.INFORMATION,"Payment updated successfully").show();
            refreshPage();
            loadNextPaymentId();
        }else{
            new Alert(Alert.AlertType.ERROR,"Failed to update payment").show();
        }
    }

    @FXML
    void tblPaymnetClickedOnAction(MouseEvent event) {
        PaymentTM paymentTM=tblPayment.getSelectionModel().getSelectedItem();
        if(paymentTM!=null){
            txtPaymentId.setText(paymentTM.getPay_id());
            txtAmount.setText(String.valueOf(paymentTM.getAmount()));
            txtDate.setText(String.valueOf(paymentTM.getDate()));
            txtInvoice.setText(paymentTM.getInvoice());
            txtPayMethods.setText(paymentTM.getMethod());
            txtTransaction.setText(paymentTM.getTransaction_reference());
            txtTax.setText(String.valueOf(paymentTM.getTax()));
            txtDiscount.setText(paymentTM.getDiscount_applied().toString());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);

        }

    }


}
