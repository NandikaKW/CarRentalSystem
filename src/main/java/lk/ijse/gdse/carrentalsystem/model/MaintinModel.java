package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.MaintainDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintinModel {
    public static boolean deleteMaintain(String maintainId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM maintenance WHERE maintain_id = ?", maintainId);
    }

    public static ArrayList<MaintainDto> getAllMaintain() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM maintenance");
        ArrayList<MaintainDto> maintainDtos=new ArrayList<>();
        while(resultSet.next()){
            MaintainDto maintainDto=new MaintainDto(
                    resultSet.getString("maintain_id"),
                    resultSet.getBigDecimal("cost"),
                    resultSet.getDate("maintain_date"),
                    resultSet.getString("description"),
                    resultSet.getString("duration"),
                    resultSet.getString("vehicle_id")
            );
            maintainDtos.add(maintainDto);

        }
        return maintainDtos;
    }

    public static MaintainDto searchMaintain(String maintainId) throws SQLException, ClassNotFoundException {
            ResultSet resultSet=CrudUtil.execute("SELECT * FROM maintenance WHERE maintain_id = ?", maintainId);
           if(resultSet.next()){
               return new MaintainDto(
                       resultSet.getString("maintain_id"),
                       resultSet.getBigDecimal("cost"),
                       resultSet.getDate("maintain_date"),
                       resultSet.getString("description"),
                       resultSet.getString("duration"),
                       resultSet.getString("vehicle_id")
               );


           }
        return null;

    }

    public static boolean saveMaintain(MaintainDto maintainDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO maintenance VALUES(?,?,?,?,?,?)",maintainDto.getMaintain_id(),maintainDto.getCost(),maintainDto.getMaintain_date(),maintainDto.getDescription(),maintainDto.getDuration(),maintainDto.getVehicle_id());
    }

    public static boolean updateMaintain(MaintainDto maintainDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE maintenance SET cost=?,maintain_date=?,description=?,duration=?,vehicle_id=? WHERE maintain_id=?",maintainDto.getCost(),maintainDto.getMaintain_date(),maintainDto.getDescription(),maintainDto.getDuration(),maintainDto.getVehicle_id(),maintainDto.getMaintain_id());


    }

    public static String loadNextMaintainId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT maintain_id FROM maintenance ORDER BY maintain_id DESC LIMIT 1");
        if (resultSet.next()){
            String lastID=resultSet.getString("maintain_id");
            String substring=lastID.substring(1);
            int id= Integer.parseInt(substring);
            int newId=id+1;
            return String.format("M%03d",newId);

        }
        return "M001";

    }

    public static String loadNextVehicleId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT vehicle_id FROM vehicle ORDER BY vehicle_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastID = resultSet.getString("vehicle_id");
            String substring = lastID.substring(1);
            int id = Integer.parseInt(substring);
            int newId = id + 1;
            return String.format("V%03d", newId);
        }
        return "V001";
    }


    public static String loadCurrentVehicleId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT vehicle_id FROM vehicle ORDER BY vehicle_id DESC LIMIT 1");

        if (resultSet.next()) {
            return resultSet.getString("vehicle_id");  // Return the most recent vehicle_id directly
        }

        return null;  // Return null if there are no records in the vehicle table
    }

}
