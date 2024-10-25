package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.RentPayemntDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentPaymentModel {
    public static boolean deleteRentPayment(String rentId, String paymentId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM rent_payment_details WHERE rent_id = ? AND pay_id = ?", rentId, paymentId);

    }

    public static ArrayList<RentPayemntDto> getAllPayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet =CrudUtil.execute("SELECT * FROM rent_payment_details");
        ArrayList<RentPayemntDto> rentPayemntDtos=new ArrayList<>();
        while(resultSet.next()){
            RentPayemntDto rentPayemntDto=new RentPayemntDto(
                   resultSet.getString("rent_id"),
                    resultSet.getString("pay_id"),
                    resultSet.getDate("payment_date"),
                    resultSet.getString("duration"),
                    resultSet.getString("description"),
                    resultSet.getBigDecimal("pay_amount"),
                    resultSet.getString("payment_method")

            );
            rentPayemntDtos.add(rentPayemntDto);

        }
        return rentPayemntDtos;

    }

    public static RentPayemntDto searchRentPayment(String rentId, String paymentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM rent_payment_details WHERE rent_id = ? AND pay_id = ?", rentId, paymentId);
        if (resultSet.next()){
            return new RentPayemntDto(
                    resultSet.getString("rent_id"),
                    resultSet.getString("pay_id"),
                    resultSet.getDate("payment_date"),
                    resultSet.getString("duration"),
                    resultSet.getString("description"),
                    resultSet.getBigDecimal("pay_amount"),
                    resultSet.getString("payment_method")
            );

        }
        return null;
    }

    public static boolean saveRentPayment(RentPayemntDto rentPayemntDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO rent_payment_details VALUES (?,?,?,?,?,?,?)",rentPayemntDto.getRent_id(),rentPayemntDto.getPay_id(),rentPayemntDto.getPayment_date(),rentPayemntDto.getDuration(),rentPayemntDto.getDescription(),rentPayemntDto.getPay_amount(),rentPayemntDto.getPayment_method());


    }

    public static boolean UpdateRentPayment(RentPayemntDto rentPayemntDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE rent_payment_details SET payment_date=?,duration=?,description=?,pay_amount=?,payment_method=? WHERE rent_id=? AND pay_id=?",rentPayemntDto.getPayment_date(),rentPayemntDto.getDuration(),rentPayemntDto.getDescription(),rentPayemntDto.getPay_amount(),rentPayemntDto.getPayment_method(),rentPayemntDto.getRent_id(),rentPayemntDto.getPay_id());

    }

    public static String loadNextPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT pay_id FROM payment ORDER BY pay_id DESC LIMIT 1");

        if (resultSet.next()) {
            String lastPaymentId = resultSet.getString("pay_id");

            // Extract the numeric part after 'PAY'
            String numericPart = lastPaymentId.substring(3);

            // Parse it to an integer, increment it by 1
            int id = Integer.parseInt(numericPart);
            int newId = id + 1;

            // Return the new ID formatted as 'PAY' followed by the number with leading zeros
            return String.format("PAY%02d", newId);
        }

        // If no records are found, return the first ID
        return "PAY01";
    }


    public static String loadNextRentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT rent_id FROM rent ORDER BY rent_id DESC LIMIT 1");
        if(resultSet.next()){
            String lastRentId=resultSet.getString("rent_id");
            String subString=lastRentId.substring(1);
            int id=Integer.parseInt(subString);
            int newId=id+1;
            return String.format("R%03d",newId);


        }
        return "R001";

    }

}
