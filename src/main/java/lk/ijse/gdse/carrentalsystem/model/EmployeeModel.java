package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.EmployeeDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public static String loadNextEmployeeId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT emp_id FROM employee ORDER BY emp_id DESC LIMIT 1");
        if(resultSet.next()){
            String lastID=resultSet.getString("emp_id");
            String substring=lastID.substring(1);
            int id= Integer.parseInt(substring);
            int newId=id+1;
            return String.format("E%03d",newId);


        }
        return "E001";
    }

    public static boolean deleteEmployee(String employeeID) throws SQLException, ClassNotFoundException {
       return  CrudUtil.execute("DELETE FROM employee WHERE emp_id=?",employeeID);

    }

    public static EmployeeDto SearchEmployee(String employeeId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM employee WHERE emp_id=?",employeeId);
        if(resultSet.next()){
            return new EmployeeDto(
                    resultSet.getString("emp_id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("job_role"),
                    resultSet.getBigDecimal("salary"),
                    resultSet.getString("admin_id")
            );

        }
        return null;
    }

    public static boolean updateEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
       return CrudUtil.execute("UPDATE employee SET name = ?, address = ?, job_role = ?, salary = ?, admin_id = ? WHERE emp_id = ?",employeeDto.getEmp_name(),employeeDto.getAddress(),employeeDto.getJob(),employeeDto.getSalary(),employeeDto.getAdmin_id(),employeeDto.getEmp_id());
    }

    public static ArrayList<EmployeeDto> getAllEmployees() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM employee");
        ArrayList<EmployeeDto> employeeDtos=new ArrayList<>();
        while(resultSet.next()){
            EmployeeDto employeeDto=new EmployeeDto(
                    resultSet.getString("emp_id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("job_role"),
                    resultSet.getBigDecimal("salary"),
                    resultSet.getString("admin_id")
            );
            employeeDtos.add(employeeDto);

        }
        return employeeDtos;
    }

    public static boolean saveEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
           return CrudUtil.execute("INSERT INTO employee VALUES(?,?,?,?,?,?)",employeeDto.getEmp_id(),employeeDto.getEmp_name(),employeeDto.getAddress(),employeeDto.getJob(),employeeDto.getSalary(),employeeDto.getAdmin_id());
         }


}


