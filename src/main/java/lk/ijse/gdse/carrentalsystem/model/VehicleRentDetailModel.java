package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleRentDetailModel {
    public static boolean deleteVehicleRent(String rentId, String vehicleId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM vehicle_rent_details WHERE rent_id = ? AND vehicle_id = ?", rentId, vehicleId);

    }

    public static  ArrayList<VechileRentDetailDto> getAllVehicleRent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet =CrudUtil.execute("SELECT * FROM vehicle_rent_details");
        ArrayList<VechileRentDetailDto> vechileRentDetailDtos=new ArrayList<>();
        while(resultSet.next()){
            VechileRentDetailDto vechileRentDetailDto=new VechileRentDetailDto(
                    resultSet.getString("vehicle_id"),
                    resultSet.getString("rent_id"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getDate("rent_date"),
                    resultSet.getBigDecimal("cost"),
                    resultSet.getString("vehicle_condition")
            );
            vechileRentDetailDtos.add(vechileRentDetailDto);
        }
        return vechileRentDetailDtos;


    }

    public static boolean saveVehicleRent(VechileRentDetailDto vechileRentDetailDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO vehicle_rent_details VALUES (?,?,?,?,?,?,?)",
                vechileRentDetailDto.getVehicle_id(),
                vechileRentDetailDto.getRent_id(),
                vechileRentDetailDto.getStart_date(),
                vechileRentDetailDto.getEnd_date(),
                vechileRentDetailDto.getRent_date(),
                vechileRentDetailDto.getCost(),
                vechileRentDetailDto.getVehicle_condition()
        );

    }

    public static VechileRentDetailDto searchVehicleRent(String vehicleId, String rentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM vehicle_rent_details WHERE vehicle_id = ? AND rent_id = ?", vehicleId, rentId);
        if (resultSet.next()){
            return new VechileRentDetailDto(
                    resultSet.getString("vehicle_id"),
                    resultSet.getString("rent_id"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getDate("rent_date"),
                    resultSet.getBigDecimal("cost"),
                    resultSet.getString("vehicle_condition")
            );
        }
        return null;


    }

    public static boolean isVehicleRentUpdated(VechileRentDetailDto vechileRentDetailDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE vehicle_rent_details SET start_date=?,end_date=?,rent_date=?,cost=?,vehicle_condition=? WHERE vehicle_id=? AND rent_id=?",vechileRentDetailDto.getStart_date(),vechileRentDetailDto.getEnd_date(),vechileRentDetailDto.getRent_date(),vechileRentDetailDto.getCost(),vechileRentDetailDto.getVehicle_condition(),vechileRentDetailDto.getVehicle_id(),vechileRentDetailDto.getRent_id());

    }

    public static String loadNextVehicleId() throws SQLException, ClassNotFoundException {
       ResultSet resultSet=CrudUtil.execute("SELECT vehicle_id FROM vehicle ORDER BY vehicle_id DESC LIMIT 1");
       if (resultSet.next()){
           String lastID=resultSet.getString("vehicle_id");
           String substring=lastID.substring(1);
           int id=Integer.parseInt(substring);
           int newId=id+1;
           return String.format("V%03d",newId);
       }
       return "V001";
    }

    public static String loadNextRentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT rent_id FROM rent ORDER BY rent_id DESC LIMIT 1");
       if (resultSet.next()){
           String lastID=resultSet.getString("rent_id");
           String substring=lastID.substring(1);
           int id=Integer.parseInt(substring);
           int newId=id+1;
           return String.format("R%03d",newId);
       }
       return "R001";
    }
    public static boolean saveVehicleRentList(ArrayList<VechileRentDetailDto> vechileRentDetailDtos) throws SQLException, ClassNotFoundException {
        for (VechileRentDetailDto vechileRentDetailDto : vechileRentDetailDtos) {

            // Save individual vehicle rent detail
            boolean isVehicleRentSaved = saveVehicleRent(vechileRentDetailDto);
            if (!isVehicleRentSaved) {
                return false;  // If saving fails, return false to trigger rollback
            }

            // Update vehicle quantity after saving rent detail
            boolean isVehicleUpdated = VehicleModel.reduceVehicleQuantity(vechileRentDetailDto);
            if (!isVehicleUpdated) {
                return false;  // If update fails, return false to trigger rollback
            }
        }
        return true;
    }






}
