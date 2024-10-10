package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeModel {

    public static String loadNextEmployeeId() throws SQLException, ClassNotFoundException {
        Connection connection= DBConnection.getInstance().getConnection();
        String sql="SELECT emp_id FROM employee ORDER BY emp_id DESC LIMIT 1";
        PreparedStatement pst=connection.prepareStatement(sql);
        ResultSet rst=pst.executeQuery();
        if(rst.next()){
            String lastId=rst.getString(1);
            String substring=lastId.substring(1);
            int id=Integer.parseInt(substring);
            int newId=id+1;
            return String.format("E%03d",newId);


        }
        return "E001";
    }

    public static boolean deleteEmployee(String employeeID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "DELETE FROM employee WHERE emp_id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, employeeID);

        int result = pst.executeUpdate();
        return result > 0;


    }

    public static EmployeeDto SearchEmployee(String employeeId) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM employee WHERE emp_id=?";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,employeeId);
        ResultSet resultSet=statement.executeQuery();

        if(resultSet.next()){
            return new EmployeeDto(
                    resultSet.getString("emp_id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("job_role"),
                    resultSet.getString("salary"),
                    resultSet.getString("admin_id")

            );

        }
        return null;
    }

    public static boolean updateEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="UPDATE employee SET name=?,address=?,job_role=?,salary=?,admin_id=? WHERE emp_id=?";
        PreparedStatement pst=connection.prepareStatement(sql);
        pst.setObject(1,employeeDto.getEmp_name());
        pst.setObject(2,employeeDto.getAddress());
        pst.setObject(3,employeeDto.getJob());
        pst.setObject(4,employeeDto.getSalary());
        pst.setObject(5,employeeDto.getAdmin_id());
        pst.setObject(6,employeeDto.getEmp_id());
        int result=pst.executeUpdate();
        return result>0;
    }

    public boolean saveEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        String sql="INSERT INTO employee VALUES(?,?,?,?,?,?)";
        PreparedStatement pst=connection.prepareStatement(sql);
        pst.setString(1,employeeDto.getEmp_id());
        pst.setString(2,employeeDto.getEmp_name());
        pst.setString(3,employeeDto.getAddress());
        pst.setString(4,employeeDto.getJob());
        pst.setString(5,employeeDto.getSalary());
        pst.setString(6,employeeDto.getAdmin_id());

        return pst.executeUpdate()>0;

    }
    public static String loadNextAdminId() throws SQLException, ClassNotFoundException {

        String sql = "SELECT admin_id FROM admin ORDER BY admin_id DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String lastAdminId = resultSet.getString(1);
            String[] splitId = lastAdminId.split("A");
            int id = Integer.parseInt(splitId[1]);
            id++;
            return String.format("A%03d", id);
        }
        return "A001";
    }

}


