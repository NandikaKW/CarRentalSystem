package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;
import lk.ijse.gdse.carrentalsystem.dto.AgrimentDto;
import lk.ijse.gdse.carrentalsystem.dto.RentDto;
import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgrimentModel {
    public static boolean deleteAgriment(String agrimentId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM  rental_agreement WHERE agreement_id = ?", agrimentId);


    }

    public static ArrayList<AgrimentDto> getAllAgriment() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM rental_agreement");
        ArrayList<AgrimentDto> agrimentDtos = new ArrayList<>();

        while (resultSet.next()) {
            AgrimentDto agrimentDto = new AgrimentDto(
                    resultSet.getString("agreement_id"),
                    resultSet.getString("payment_terms"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getBigDecimal("deposit_amount"),
                    resultSet.getBigDecimal("total_rent_cost")
            );
            agrimentDtos.add(agrimentDto);  // Add each AgrimentDto to the list
        }

        return agrimentDtos;  // Return the populated list
    }


    public static AgrimentDto searchAgriment(String agrimentId) throws SQLException, ClassNotFoundException {
       ResultSet resultSet=CrudUtil.execute("Select * FROM rental_agreement WHERE agreement_id = ?",agrimentId);
       if (resultSet.next()){
           return new AgrimentDto(
                   resultSet.getString("agreement_id"),
                   resultSet.getString("payment_terms"),
                   resultSet.getDate("start_date"),
                   resultSet.getDate("end_date"),
                   resultSet.getBigDecimal("deposit_amount"),
                   resultSet.getBigDecimal("total_rent_cost")
           );
       }
       return null;

    }

    public static boolean saveAgriment(AgrimentDto agrimentDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO rental_agreement VALUES (?,?,?,?,?,?)",agrimentDto.getAgreement_id(),agrimentDto.getPayment_terms(),agrimentDto.getStart_date(),agrimentDto.getEnd_date(),agrimentDto.getDeposit_amount(),agrimentDto.getTotal_rent_cost());


    }

    public static boolean updateAgriment(AgrimentDto agrimentDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE rental_agreement SET payment_terms=?,start_date=?,end_date=?,deposit_amount=?,total_rent_cost=? WHERE agreement_id=?",agrimentDto.getPayment_terms(),agrimentDto.getStart_date(),agrimentDto.getEnd_date(),agrimentDto.getDeposit_amount(),agrimentDto.getTotal_rent_cost(),agrimentDto.getAgreement_id());





    }

    public static String getNextAgrimentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT agreement_id FROM rental_agreement ORDER BY agreement_id DESC LIMIT 1");

        if (resultSet.next()) {
            String lastID = resultSet.getString("agreement_id");

            // Extract the numeric part after "AG"
            String numericPart = lastID.substring(2);

            // Parse the numeric part and increment
            int id = Integer.parseInt(numericPart);
            int newId = id + 1;

            // Format the new ID with "AG" prefix and three-digit number
            return String.format("AG%03d", newId);
        }

        // Default if no ID exists in the table
        return "AG001"; // Default ID in case the table is empty
    }


    public static String loadCurrentAgreementId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT agreement_id FROM rental_agreement ORDER BY agreement_id DESC LIMIT 1");

        if (resultSet.next()) {
            return resultSet.getString("agreement_id");  // Return the latest agreement ID directly
        }
        return null;  // Return null if no records are available
    }





}
