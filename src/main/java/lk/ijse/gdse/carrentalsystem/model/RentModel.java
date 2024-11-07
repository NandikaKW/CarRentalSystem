package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.RentDto;
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentModel {
    public static ArrayList<RentDto> getAllRentData() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM rent");
        ArrayList<RentDto> rentDtos = new ArrayList<>();

        while (resultSet.next()) {
            rentDtos.add(new RentDto(
                    resultSet.getString("rent_id"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getString("cust_id"),
                    resultSet.getString("agreement_id") // Ensure this field is retrieved
            ));
        }

        return rentDtos;
    }



    public static boolean DeleteRent(String rentId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM rent WHERE rent_id = ?", rentId);

    }

    public static boolean saveRent(RentDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO rent VALUES (?,?,?,?,?)",
                dto.getRentId(), dto.getStartDate(), dto.getEndDate(), dto.getCustId(), dto.getAgreementId());
    }

    public static RentDto SearchRent(String rentId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Rent WHERE rent_id = ?", rentId);
        if (rst.next()) {
            return new RentDto(
                    rst.getString("rent_id"),
                    rst.getDate("start_date"),
                    rst.getDate("end_date"),
                    rst.getString("cust_id"),
                    rst.getString("agreement_id") // Added to retrieve the agreement ID
            );
        }
        return null;
    }


    public static boolean updateRent(RentDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Rent SET start_date=?, end_date=?, cust_id=?, agreement_id=? WHERE rent_id=?",
                dto.getStartDate(), dto.getEndDate(), dto.getCustId(), dto.getAgreementId(), dto.getRentId());
    }

    public static String loadNextRentId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT rent_id FROM rent ORDER BY rent_id DESC LIMIT 1");
        if (rst.next()) {
            String lastID = rst.getString(1);
            String substring = lastID.substring(1); // Strip the leading "C"
            int id = Integer.parseInt(substring);
            int newIndex = id + 1;
            return String.format("R%03d", newIndex); // Format the new ID as "Cxxx"
        }
        return "R001"; // If no previous customers exist
    }
    public String loadNextCustomerId() throws SQLException, ClassNotFoundException, SQLException {
        ResultSet rst = CrudUtil.execute("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");
        if (rst.next()) {
            String lastID = rst.getString(1);
            String substring = lastID.substring(1); // Strip the leading "C"
            int id = Integer.parseInt(substring);
            int newIndex = id + 1;
            return String.format("C%03d", newIndex); // Format the new ID as "Cxxx"
        }
        return "C001"; // If no previous customers exist
    }


}
