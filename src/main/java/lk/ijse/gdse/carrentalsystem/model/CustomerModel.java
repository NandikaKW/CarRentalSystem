package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public static String loadCurrentCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");

        if (rst.next()) {
            return rst.getString("cust_id");  // Return the latest customer ID if exists
        }
        return "C001";  // Default to "C001" if no customer exists
    }


    // Load the next customer ID
    public static String loadNextCustomerId() throws SQLException, ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");
        if (rst.next()) {
            String lastID = rst.getString(1);
            String substring = lastID.substring(1); // Strip the leading "C"
            int id = Integer.parseInt(substring);
            int newIndex = id + 1;
            return String.format("C%03d", newIndex); // Format the new ID as "Cxxx"
        }
        return "C001";
    }

    public static boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false); // Disable auto-commit

            // Check if the NIC already exists in the database
            String checkNicQuery = "SELECT COUNT(*) FROM customer WHERE nic_number = ?";
            PreparedStatement checkNicStmt = connection.prepareStatement(checkNicQuery);
            checkNicStmt.setString(1, dto.getNic());
            ResultSet rs = checkNicStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                connection.rollback(); // Rollback the transaction
                return false;
            }

            // Proceed with customer insertion
            String insertQuery = "INSERT INTO customer (cust_id, name, address, email, nic_number, admin_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1, dto.getCust_id());
            pstmt.setString(2, dto.getCust_name());
            pstmt.setString(3, dto.getAddress());
            pstmt.setString(4, dto.getEmail());
            pstmt.setString(5, dto.getNic());
            pstmt.setString(6, dto.getAdmin_id());

            boolean isCustomerSaved = pstmt.executeUpdate() > 0;

            if (isCustomerSaved) {
                boolean isCustomerPaymentSaved = CustomerPaymentModel.saveCustomerPaymentList(dto.getCustomerPaymentDtos());
                if (isCustomerPaymentSaved) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ArrayList<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet rst =  CrudUtil.execute("select * from customer");
        ArrayList<CustomerDto> customerDTOS = new ArrayList<>();
        while (rst.next()){
            CustomerDto customerDTO = new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }



    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE customer SET name = ?, address = ?, email = ?, nic_number = ?, admin_id = ? WHERE cust_id = ?",
                dto.getCust_name(), dto.getAddress(), dto.getEmail(), dto.getNic(), dto.getAdmin_id(), dto.getCust_id()
        );
    }

    // Search for a customer by ID
    public CustomerDto searchCustomer(String cust_id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE cust_id = ?", cust_id);
        if (resultSet.next()) {
            return new CustomerDto(
                    resultSet.getString("cust_id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("email"),
                    resultSet.getString("nic_number"),
                    resultSet.getString("admin_id")
            );
        }
        return null; // If the customer is not found
    }

    // Delete a customer from the database by ID
    public boolean deleteCustomer(String cust_id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE cust_id = ?", cust_id);
    }

    // Get a list of all customers
    public ArrayList<CustomerDto> getAllCustomer() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM customer");
        ArrayList<CustomerDto> customerDtos = new ArrayList<>();
        while (rst.next()) {
            customerDtos.add(new CustomerDto(
                    rst.getString("cust_id"),
                    rst.getString("name"),
                    rst.getString("address"),
                    rst.getString("email"),
                    rst.getString("nic_number"),
                    rst.getString("admin_id")
            ));
        }
        return customerDtos;
    }

}
