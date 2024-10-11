package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.CustomerDto;
import lk.ijse.gdse.carrentalsystem.tm.CustomerTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {

    public String loadNextCustomerId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        if (rst.next()) {
            String LastID = rst.getString(1);
            String substring = LastID.substring(1);
            int i = Integer.parseInt(substring);
            int newIndex = i + 1;

            return String.format("C%03d", newIndex);


        }
        return "C001";

    }

    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO customer VALUES(?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setObject(1, dto.getCust_id());
        pst.setObject(2, dto.getCust_name());
        pst.setObject(3, dto.getAddress());
        pst.setObject(4, dto.getContact());
        pst.setObject(5, dto.getNic());
        pst.setObject(6, dto.getAdmin_id());

        int result = pst.executeUpdate();
        boolean isaved = result > 0;
        return isaved;


    }

    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE customer SET name = ?, address = ?, contact_number=?, nic_number = ?,admin_id = ? WHERE cust_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setObject(1,dto.getCust_name());
        pst.setObject(2, dto.getAddress());
        pst.setObject(3, dto.getContact());
        pst.setObject(4, dto.getNic());
        pst.setObject(5, dto.getAdmin_id());
        pst.setObject(6, dto.getCust_id());

        int result=pst.executeUpdate();
        return  result>0;
    }
    public CustomerDto searchCustomer(String cust_id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM customer WHERE cust_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,cust_id);
        ResultSet resultSet = statement.executeQuery();

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

        return null;
    }
    public boolean deleteCustomer(String cust_id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM customer WHERE cust_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, cust_id);

        int result = pst.executeUpdate();
        return result > 0;
    }


    public ArrayList<CustomerDto> getAllCustomer() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();

        ArrayList<CustomerDto> customerDtos = new ArrayList<>();
        while (rst.next()) {

        }
        return customerDtos;
    }
}
