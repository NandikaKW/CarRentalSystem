package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
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

    // Save a new customer to the database
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO customer VALUES (?,?,?,?,?,?)",
                dto.getCust_id(), dto.getCust_name(), dto.getAddress(),
                dto.getContact(), dto.getNic(), dto.getAdmin_id()
        );
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
                "UPDATE customer SET name = ?, address = ?, contact_number = ?, nic_number = ?, admin_id = ? WHERE cust_id = ?",
                dto.getCust_name(), dto.getAddress(), dto.getContact(), dto.getNic(), dto.getAdmin_id(), dto.getCust_id()
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
                    resultSet.getString("contact_number"),
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
                    rst.getString("contact_number"),
                    rst.getString("nic_number"),
                    rst.getString("admin_id")
            ));
        }
        return customerDtos;
    }

}
