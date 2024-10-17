package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.AdminDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminModel {


    public static boolean saveAdmin(AdminDto adminDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO admin VALUES(?,?,?,?)",adminDto.getAdmin_id(),adminDto.getUserName(),adminDto.getEmail(),adminDto.getPassword());

    }

    public static String loadNextAdminId() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT admin_id FROM admin ORDER BY admin_id DESC LIMIT 1");

        if (rst.next()) {
            String lastID = rst.getString("admin_id");
            String substring = lastID.substring(1);
            int id = Integer.parseInt(substring);
            int newId = id + 1;

            return String.format("A%03d", newId);
        }
        return "A001";

    }
    public static boolean updateAdmin(AdminDto adminDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE admin SET username=?,email=?,password=? WHERE admin_id=?",adminDto.getUserName(),adminDto.getEmail(),adminDto.getPassword(),adminDto.getAdmin_id());



    }
    public static boolean deleteAdmin(String adminId) throws SQLException, ClassNotFoundException {
      return  CrudUtil.execute("DELETE FROM admin WHERE admin_id=?",adminId);

    }

    public static AdminDto searchAdmin(String adminId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM admin WHERE admin_id=?",adminId);
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
    public static ArrayList<AdminDto> getAllAdmins() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT * FROM admin");
        ArrayList<AdminDto> adminDtos=new ArrayList<>();
        while(rst.next()){
            AdminDto adminDto=new AdminDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)


            );
            adminDtos.add(adminDto);

        }
        return adminDtos;
    }
}
