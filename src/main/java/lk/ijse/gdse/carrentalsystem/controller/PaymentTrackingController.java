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
import lk.ijse.gdse.carrentalsystem.dto.PaymentDto;
import lk.ijse.gdse.carrentalsystem.model.PackageModel;
import lk.ijse.gdse.carrentalsystem.model.PaymentModel;
import lk.ijse.gdse.carrentalsystem.dto.tm.PaymentTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

        try {

            refreshPage();
            loadTableData();
            loadNextPaymentId();
            refreshTableData();  // Called twice, but you can remove one if not needed

        } catch (SQLException e) {
            // Handle database-related errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            // Handle class not found errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        } catch (NullPointerException e) {
            // Handle null pointer exceptions if any
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Null value encountered during initialization: " + e.getMessage()).show();
        } catch (Exception e) {
            // Handle any other unexpected errors
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred during initialization: " + e.getMessage()).show();
        }

        try {
            // Initialize date combo boxes
            initializeDateCombos();
        } catch (Exception e) {
            // Handle errors specific to the date combo initialization
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error initializing date combos: " + e.getMessage()).show();
        }
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
    private TextField txtFinalAmount;


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

        // Check if Payment ID is empty
        if (paymentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Enter Payment Id").show();
            return; // Exit the method if paymentId is empty
        }

        // Confirmation Alert before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this payment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        // If user confirms deletion
        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                // Attempt to delete the payment
                boolean isDeleted = PaymentModel.deletePayment(paymentId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully").show();
                    clearFields();
                    loadNextPaymentId();
                    refreshTableData();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete payment").show();
                }

            } catch (SQLException e) {
                // Handle SQLException (e.g., database issues)
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Database error occurred while deleting payment: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                // Handle ClassNotFoundException (e.g., issues with class loading)
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Class loading error occurred while deleting payment: " + e.getMessage()).show();
            } catch (Exception e) {
                // Handle any other unexpected exceptions
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An unexpected error occurred while deleting payment: " + e.getMessage()).show();
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
    void btnReportOnAction(ActionEvent event) throws ClassNotFoundException {

        PaymentTM paymentTM = tblPayment.getSelectionModel().getSelectedItem();

        // Check if a payment is selected
        if (paymentTM == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a payment to generate the report!").show();
            return; // Exit if no payment is selected
        }

        try {
            // Attempt to compile the Jasper report
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/report/PaymentReport.jrxml")
            );

            // Get database connection
            Connection connection = DBConnection.getInstance().getConnection();

            // Prepare report parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_Date", LocalDate.now().toString());
            parameters.put("P_Payment_Id", paymentTM.getPay_id());

            // Fill the report with data from the database
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            // View the report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            // Handle JasperReport exceptions (e.g., report compilation or filling issues)
            new Alert(Alert.AlertType.ERROR, "Failed to generate report...!").show();
            e.printStackTrace(); // Log the exception stack trace
        } catch (SQLException e) {
            // Handle SQLException (e.g., database connection or query issues)
            new Alert(Alert.AlertType.ERROR, "Database error...!").show();
            e.printStackTrace(); // Log the exception stack trace
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred!").show();
            e.printStackTrace(); // Log the exception stack trace
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        refreshPage();
        loadNextPaymentId();

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId = txtPaymentId.getText();
        String invoice = txtInvoice.getText();
        String method = txtPayMethods.getText();
        String transaction = txtTransaction.getText();

        // Regex patterns
        String paymentIdPattern = "^PAY\\d{3}$"; // Matches PAY001, PAY002, etc.
        String invoicePattern = "^[A-Za-z0-9]+$"; // Allows alphanumeric characters only
        String methodPattern = "^(Cash|Credit Card|Bank Transfer|Mobile Payment|Bank Deposit)$"; // Valid payment methods
        String transactionPattern = "^[A-Za-z0-9]+$"; // Allows alphanumeric characters only

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidPaymentId = paymentId.matches(paymentIdPattern);
        boolean isValidInvoice = invoice.matches(invoicePattern);
        boolean isValidMethod = method.matches(methodPattern);
        boolean isValidTransaction = transaction.matches(transactionPattern);

        // Validate numeric and date fields separately
        BigDecimal amount = null;
        BigDecimal tax = null;
        BigDecimal discount = null;
        Date date = null;

        try {
            amount = new BigDecimal(txtAmount.getText());
        } catch (NumberFormatException e) {
            txtAmount.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid amount format. Please enter a valid number.").show();
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(txtDate.getText());
        } catch (ParseException e) {
            txtDate.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd format.").show();
            return;
        }

        try {
            tax = new BigDecimal(txtTax.getText());
            discount = new BigDecimal(txtDiscount.getText());
        } catch (NumberFormatException e) {
            txtTax.setStyle("-fx-border-color: red;");
            txtDiscount.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid tax or discount format. Please enter valid numbers.").show();
            return;
        }

        // Alert message for validation errors
        if (!isValidPaymentId || !isValidInvoice || !isValidMethod || !isValidTransaction) {
            StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

            if (!isValidPaymentId) {
                txtPaymentId.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Payment ID (Expected format: PAY001)\n");
            }
            if (!isValidInvoice) {
                txtInvoice.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Invoice (Only alphanumeric characters allowed)\n");
            }
            if (!isValidMethod) {
                txtPayMethods.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Payment Method (Allowed: Cash, Credit Card, Bank Transfer, Mobile Payment, Bank Deposit)\n");
            }
            if (!isValidTransaction) {
                txtTransaction.setStyle("-fx-border-color: red;");
                errorMessage.append("- Invalid Transaction Reference (Only alphanumeric characters allowed)\n");
            }

            new Alert(Alert.AlertType.WARNING, errorMessage.toString()).show();
            return;
        }

        // Calculate total after applying tax and discount
        BigDecimal taxAmount = amount.multiply(tax).divide(new BigDecimal("100"));
        BigDecimal discountAmount = amount.multiply(discount).divide(new BigDecimal("100"));
        BigDecimal finalAmount = amount.add(taxAmount).subtract(discountAmount);

        // Set the final amount in the text field
        txtFinalAmount.setText(finalAmount.toString());

        // Create the PaymentDto object
        PaymentDto paymentDto = new PaymentDto(paymentId, finalAmount, date, invoice, method, transaction, tax, discount);

        // Save payment
        boolean isSaved = false;
        try {
            isSaved = PaymentModel.savePayment(paymentDto);
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving payment: " + e.getMessage()).show();
            e.printStackTrace();
            return;
        }

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Payment saved successfully").show();
            refreshPage();
            loadNextPaymentId();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save payment").show();
        }
    }

    // Utility function to reset field styles
    private void resetFieldStyles() {
        txtPaymentId.setStyle("");
        txtAmount.setStyle("");
        txtDate.setStyle("");
        txtInvoice.setStyle("");
        txtPayMethods.setStyle("");
        txtTransaction.setStyle("");
        txtTax.setStyle("");
        txtDiscount.setStyle("");
    }


    @FXML
    void btnSearchOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId = txtPaymentId.getText();

        // Check if the payment ID is empty and show an error alert
        if (paymentId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter Payment ID").show();
            return; // Exit if the ID is empty
        }

        try {
            // Attempt to search for the payment using the provided Payment ID
            PaymentDto payment = PaymentModel.searchPayment(paymentId);

            if (payment != null) {
                // If the payment is found, populate the fields with the payment details
                txtPaymentId.setText(payment.getPay_id());
                txtAmount.setText(String.valueOf(payment.getAmount()));
                txtDate.setText(String.valueOf(payment.getDate()));
                txtInvoice.setText(payment.getInvoice());
                txtPayMethods.setText(payment.getMethod());
                txtTransaction.setText(payment.getTransaction_reference());
                txtTax.setText(String.valueOf(payment.getTax()));
                txtDiscount.setText(String.valueOf(payment.getDiscount_applied()));

                // Show a success alert indicating that the payment data was found
                new Alert(Alert.AlertType.INFORMATION, "Payment data found successfully!").show();
            } else {
                // If payment is not found, show an error and reset the fields
                new Alert(Alert.AlertType.ERROR, "Payment Not Found").show();
                loadNextPaymentId();  // Load next payment ID after a failed search
                clearFields();        // Clear the fields to avoid showing old data
            }

        } catch (SQLException e) {
            // Handle any SQL-related exceptions, such as connection issues or query errors
            new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage()).show();
            e.printStackTrace(); // Optionally log the exception details for debugging purposes
        } catch (ClassNotFoundException e) {
            // Handle any ClassNotFoundException, such as missing database driver or model class
            new Alert(Alert.AlertType.ERROR, "Class not found: " + e.getMessage()).show();
            e.printStackTrace(); // Log the exception details
        } catch (Exception e) {
            // Catch any other exceptions that may occur unexpectedly
            new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage()).show();
            e.printStackTrace(); // Log the error
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String paymentId = txtPaymentId.getText();
        String amountStr = txtAmount.getText();
        String dateStr = txtDate.getText();
        String invoice = txtInvoice.getText();
        String method = txtPayMethods.getText();
        String transaction = txtTransaction.getText();
        String taxStr = txtTax.getText();
        String discountStr = txtDiscount.getText();

        // Regex patterns for validation
        String paymentIdPattern = "^PAY\\d{3}$"; // Matches PAY001, PAY002, etc.
        String invoicePattern = "^[A-Za-z0-9]+$"; // Allows alphanumeric characters only
        String methodPattern = "^[A-Za-z ]+$"; // Allows letters and spaces
        String transactionPattern = "^TXN\\d{3}$"; // Matches TXN001, TXN002, etc.

        // Reset field styles
        resetFieldStyles();

        // Validation checks
        boolean isValidPaymentId = paymentId.matches(paymentIdPattern);
        boolean isValidInvoice = invoice.matches(invoicePattern);
        boolean isValidMethod = method.matches(methodPattern);
        boolean isValidTransaction = transaction.matches(transactionPattern);

        BigDecimal amount = null, tax = null, discount = null;
        Date date = null;

        // Validate numeric fields
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            txtAmount.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid amount format").show();
            return;
        }

        try {
            tax = new BigDecimal(taxStr);
        } catch (NumberFormatException e) {
            txtTax.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid tax format").show();
            return;
        }

        try {
            discount = new BigDecimal(discountStr);
        } catch (NumberFormatException e) {
            txtDiscount.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid discount format").show();
            return;
        }

        // Validate date format
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            txtDate.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use yyyy-MM-dd format.").show();
            return;
        }

        // Check string fields
        if (!isValidPaymentId) {
            txtPaymentId.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Payment ID (Expected format: PAY001)").show();
            return;
        }

        if (!isValidInvoice) {
            txtInvoice.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Invoice format (Only alphanumeric characters allowed)").show();
            return;
        }

        if (!isValidMethod) {
            txtPayMethods.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Payment Method (Only letters and spaces allowed)").show();
            return;
        }

        if (!isValidTransaction) {
            txtTransaction.setStyle("-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid Transaction Reference (Expected format: TXN001)").show();
            return;
        }

        // Create PaymentDto object
        PaymentDto paymentDto = new PaymentDto(paymentId, amount, date, invoice, method, transaction, tax, discount);

        try {
            boolean isUpdated = PaymentModel.UpdatePayment(paymentDto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Payment updated successfully").show();
                refreshPage();
                loadNextPaymentId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update payment").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class not found: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage()).show();
            e.printStackTrace();
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
