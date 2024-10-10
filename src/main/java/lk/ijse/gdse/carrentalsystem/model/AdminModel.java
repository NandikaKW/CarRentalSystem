package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.AdminDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminModel {
    public  boolean saveAdmin(AdminDto adminDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO admin (admin_id, username, email, password) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, adminDto.getAdmin_id());
        pst.setString(2, adminDto.getUserName());
        pst.setString(3, adminDto.getEmail());
        pst.setString(4, adminDto.getPassword());
        return pst.executeUpdate() > 0;

    }
    public static String loadNextAdminId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT admin_id FROM admin ORDER BY admin_id DESC LIMIT 1";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        if (rst.next()) {
            String lastID = rst.getString(1);
            String substring = lastID.substring(1);
            int id = Integer.parseInt(substring);
            int newId = id + 1;


            return String.format("A%03d", newId);
        }
        return "A001";

    }
    public  boolean updateAdmin(AdminDto adminDto) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "UPDATE admin SET username = ?, email = ?, password = ? WHERE admin_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, adminDto.getUserName());
        pst.setString(2, adminDto.getEmail());
        pst.setString(3, adminDto.getPassword());
        pst.setString(4, adminDto.getAdmin_id());

        int result = pst.executeUpdate();
        return result > 0;


    }
    public static boolean deleteAdmin(String adminId) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="DELETE FROM admin WHERE admin_id=?";
        PreparedStatement pst=connection.prepareStatement(sql);
        pst.setString(1,adminId);

        int result=pst.executeUpdate();
        return result>0;

    }
    public static AdminDto searchAdmin(String adminId) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM admin WHERE admin_id=?";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,adminId);
        ResultSet resultSet= statement.executeQuery();

        if(resultSet.next()){
            return new AdminDto(
                    resultSet.getString("admin_id"),
            resultSet.getString("username"),
            resultSet.getString("email"),
            resultSet.getString("password")


            );



        }
        return null;

    }

}
