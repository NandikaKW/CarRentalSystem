package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.VechileRentDetailDto;
import lk.ijse.gdse.carrentalsystem.dto.VehicleDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VehicleModel {
    public boolean deleteVehicle(String vehicleId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM vehicle WHERE vehicle_id = ?", vehicleId);
    }

    public ArrayList<VehicleDto> getAllVehicles() throws SQLException, ClassNotFoundException {
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

    public VehicleDto searchVehicle(String vehicleId) throws SQLException, ClassNotFoundException {
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


    public boolean saveVehicle(VehicleDto vehicleDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO vehicle VALUES (?,?,?,?,?,?)",vehicleDto.getVehicle_id(),vehicleDto.getModel(),vehicleDto.getColour(),vehicleDto.getCategory(),vehicleDto.getQuantity(),vehicleDto.getPackage_id());
    }

    public Boolean updateVehicle(VehicleDto vehicleDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE vehicle SET model=?,colour=?,category=?,quantity=?,package_id=? WHERE vehicle_id=?",vehicleDto.getModel(),vehicleDto.getColour(),vehicleDto.getCategory(),vehicleDto.getQuantity(),vehicleDto.getPackage_id(),vehicleDto.getVehicle_id());
    }

    public String loadNextVehicleId() throws SQLException, ClassNotFoundException {
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

    public String loadNextPackageId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT package_id FROM package ORDER BY package_id DESC LIMIT 1");
        if (resultSet.next()){
            String lastID=resultSet.getString("package_id");
            String substring=lastID.substring(1);
            int id=Integer.parseInt(substring);
            int newId=id+1;
            return String.format("P%03d",newId);


        }
        return "P001";

    }
    public boolean reduceVehicleQuantity(VechileRentDetailDto vechileRentDetailDto) throws SQLException, ClassNotFoundException {
        try {
            // Execute the update statement to reduce quantity by 1
            return CrudUtil.execute("UPDATE vehicle SET quantity = quantity - 1 WHERE vehicle_id = ?", vechileRentDetailDto.getVehicle_id());
        } catch (SQLException e) {
            System.err.println("Error while reducing vehicle quantity for vehicle_id: " + vechileRentDetailDto.getVehicle_id());
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found.");
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getAllVehicleIds() throws SQLException, ClassNotFoundException {
        // Execute SQL query to get all item IDs
        ResultSet rst = CrudUtil.execute("select vehicle_id from vehicle");

        // Create an ArrayList to store the item IDs
        ArrayList<String> vehicleIds = new ArrayList<>();

        // Iterate through the result set and add each item ID to the list
        while (rst.next()) {
            vehicleIds.add(rst.getString(1));
        }

        // Return the list of item IDs
        return vehicleIds;
    }

    public String loadCurrentPackageId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT package_id FROM package ORDER BY package_id DESC LIMIT 1");

        if (resultSet.next()) {
            return resultSet.getString("package_id");  // Return the most recent package_id directly
        }

        return null;  // Return null if there are no records in the package table
    }

}
