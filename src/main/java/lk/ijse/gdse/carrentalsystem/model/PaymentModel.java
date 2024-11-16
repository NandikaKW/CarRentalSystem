package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.CustomerPaymentDto;
import lk.ijse.gdse.carrentalsystem.dto.PackageDto;
import lk.ijse.gdse.carrentalsystem.dto.PaymentDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public static boolean deletePayment(String paymentId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM payment WHERE pay_id=?", paymentId);
    }

    public static PaymentDto searchPayment(String paymentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payment WHERE pay_id=?", paymentId);
        if (resultSet.next()) {
            return new PaymentDto(
                    resultSet.getString("pay_id"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getDate("date"),
                    resultSet.getString("invoice"),
                    resultSet.getString("method"),
                    resultSet.getString("transaction_reference"),
                    resultSet.getBigDecimal("tax"),
                    resultSet.getBigDecimal("discount")

            );

        }
        return null;
    }

    public static boolean savePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO payment VALUES(?,?,?,?,?,?,?,?)", paymentDto.getPay_id(), paymentDto.getAmount(), paymentDto.getDate(), paymentDto.getInvoice(), paymentDto.getMethod(), paymentDto.getTransaction_reference(), paymentDto.getTax(), paymentDto.getDiscount_applied());

    }
    public static boolean UpdatePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE payment SET pay_id=?, amount=?, date=?, invoice=?, method=?, transaction_reference=?, tax=?, discount=? WHERE pay_id=?",
                paymentDto.getPay_id(),                // Correctly pass pay_id here for the first column in the update
                paymentDto.getAmount(),
                paymentDto.getDate(),
                paymentDto.getInvoice(),
                paymentDto.getMethod(),
                paymentDto.getTransaction_reference(),
                paymentDto.getTax(),
                paymentDto.getDiscount_applied(),
                paymentDto.getPay_id());               // Correctly pass pay_id again for the WHERE clause
    }


    public static ArrayList<PaymentDto> getAllPayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM payment");
        ArrayList<PaymentDto> paymentDtos = new ArrayList<>();
        while (resultSet.next()) {
            PaymentDto paymentDto = new PaymentDto(
                    resultSet.getString("pay_id"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getDate("date"),
                    resultSet.getString("invoice"),
                    resultSet.getString("method"),
                    resultSet.getString("transaction_reference"),
                    resultSet.getBigDecimal("tax"),
                    resultSet.getBigDecimal("discount")
            );
            paymentDtos.add(paymentDto);

        }
        return paymentDtos;

    }

    public static String loadNextPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT pay_id FROM payment ORDER BY pay_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastID = resultSet.getString("pay_id");


            String numericPart = lastID.replaceAll("\\D", ""); // Remove all non-digit characters

            // Parse the numeric part to an integer
            int id = Integer.parseInt(numericPart);

            // Increment the ID
            int newId = id + 1;

            // Format the new ID, keeping the prefix "PAY"
            String newID = String.format("PAY%03d", newId);
            return newID; // Return the newly generated ID
        }
        return "PAY001"; // If no record is found, start with "PAY001"
    }

    public static ArrayList<String> getAllPaymentIDs() throws SQLException, ClassNotFoundException {
        // Execute SQL query to get all item IDs
        ResultSet rst = CrudUtil.execute("select pay_id from payment");

        // Create an ArrayList to store the item IDs
        ArrayList<String> paymentIds = new ArrayList<>();

        // Iterate through the result set and add each item ID to the list
        while (rst.next()) {
            paymentIds.add(rst.getString(1));
        }

        // Return the list of item IDs
        return paymentIds;
    }

    public static boolean reducePaymentAmount(CustomerPaymentDto customerPaymentDto) throws SQLException, ClassNotFoundException {
        try {
            return  CrudUtil.execute("UPDATE payment SET amount = amount - ? WHERE pay_id = ?",customerPaymentDto.getAmount(),customerPaymentDto.getPay_id());
        } catch (SQLException e) {
            System.out.println("Error while reducing payment amount for pay_id: " + customerPaymentDto.getPay_id());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Database driver not found.");
            e.printStackTrace();
            return false;
        }
    }

    public static BigDecimal getAvailablePaymentAmount(String paymentId) throws SQLException, ClassNotFoundException {
        String query = "SELECT amount FROM payment WHERE pay_id = ?";
        ResultSet rs = CrudUtil.execute(query, paymentId);

        if (rs.next()) {
            return rs.getBigDecimal("amount");
        } else {
            throw new SQLException("Payment ID not found: " + paymentId);
        }
    }
}
