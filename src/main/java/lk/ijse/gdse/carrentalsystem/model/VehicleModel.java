package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleModel {
    public static boolean deleteVehicle(String vehicleId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM vehicle WHERE vehicle_id = ?", vehicleId);
    }

    public static ArrayList<VehicleDto> getAllVehicles() throws SQLException, ClassNotFoundException {
           ResultSet resultSet =CrudUtil.execute("SELECT * FROM vehicle");
           ArrayList<VehicleDto> vehicleDtos=new ArrayList<>();
           while (resultSet.next()){
               VehicleDto vehicleDto=new VehicleDto(
                       resultSet.getString("vehicle_id"),
                       resultSet.getString("model"),
                       resultSet.getString("colour"),
                       resultSet.getString("category"),
                       resultSet.getInt("quantity"),
                       resultSet.getString("package_id")
               );
               vehicleDtos.add(vehicleDto);
           }
           return vehicleDtos;
    }

    public static VehicleDto searchVehicle(String vehicleId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM vehicle WHERE vehicle_id = ?", vehicleId);
        if (resultSet.next()){
            return new VehicleDto(
                    resultSet.getString("vehicle_id"),
                    resultSet.getString("model"),
                    resultSet.getString("colour"),
                    resultSet.getString("category"),
                    resultSet.getInt("quantity"),
                    resultSet.getString("package_id")

            );

        }
        return null;
    }


    public static boolean saveVehicle(VehicleDto vehicleDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO vehicle VALUES (?,?,?,?,?,?)",vehicleDto.getVehicle_id(),vehicleDto.getModel(),vehicleDto.getColour(),vehicleDto.getCategory(),vehicleDto.getQuantity(),vehicleDto.getPackage_id());
    }

    public static Boolean updateVehicle(VehicleDto vehicleDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE vehicle SET model=?,colour=?,category=?,quantity=?,package_id=? WHERE vehicle_id=?",vehicleDto.getModel(),vehicleDto.getColour(),vehicleDto.getCategory(),vehicleDto.getQuantity(),vehicleDto.getPackage_id(),vehicleDto.getVehicle_id());
    }

    public static String loadNextVehicleId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT vehicle_id FROM vehicle ORDER BY vehicle_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastID = resultSet.getString("vehicle_id");
            String substring = lastID.substring(1); // Get the numeric part
            int id = Integer.parseInt(substring);   // Convert it to an integer
            int newId = id + 1;                     // Increment by 1
            return String.format("V%03d", newId);   // Format as "V" followed by a 3-digit number
        }
        return "V001"; // Default if no vehicles are found
    }

}
