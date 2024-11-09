package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.DamageDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DamageModel {
    public static boolean deleteDamage(String employeeId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM damage WHERE damage_id = ?",employeeId);
    }

    public static ArrayList<DamageDto> getAllDamages() throws SQLException, ClassNotFoundException {
         ResultSet resultSet =CrudUtil.execute("SELECT * FROM damage");
        ArrayList<DamageDto> damageDtos=new ArrayList<>();
        while(resultSet.next()){
            DamageDto damageDto=new DamageDto(
                    resultSet.getString("damage_id"),
                    resultSet.getBigDecimal("repair_cost"),
                    resultSet.getString("detail"),
                    resultSet.getString("rent_id")
            );
            damageDtos.add(damageDto);

        }
        return damageDtos;
    }
    public static boolean saveDamage(DamageDto damageDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO damage VALUES(?,?,?,?)",damageDto.getDamage_id(),damageDto.getRepair_cost(),damageDto.getDetail(),damageDto.getRent_id());
    }

    public static DamageDto searchDamage(String damageId) throws SQLException, ClassNotFoundException {
        System.out.println("Executing query for Damage ID: " + damageId); // Debugging line
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM damage WHERE damage_id = ?", damageId);

        if (resultSet.next()) {
            System.out.println("Damage record found in database"); // Debugging line

            return new DamageDto(
                    resultSet.getString("damage_id"),
                    resultSet.getBigDecimal("repair_cost"),
                    resultSet.getString("detail"),
                    resultSet.getString("rent_id")
            );
        } else {
            System.out.println("No damage record found for Damage ID: " + damageId); // Debugging line
        }

        return null;
    }
    public static boolean updateDamage(DamageDto damageDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE damage SET repair_cost=?,detail=?,rent_id=? WHERE damage_id=?",damageDto.getRepair_cost(),damageDto.getDetail(),damageDto.getRent_id(),damageDto.getDamage_id());




    }

    public static String loadNextDamageID() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT damage_id  FROM damage ORDER BY damage_id DESC LIMIT 1");
             if(resultSet.next()){
                 String lastID=resultSet.getString("damage_id");
                 String substring=lastID.substring(1);
                 int id=Integer.parseInt(substring);
                 int newId=id+1;
                 return String.format("D%03d",newId);

             }
             return "D001";


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

    public static String loadCurrentRentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT rent_id FROM rent ORDER BY rent_id DESC LIMIT 1");

        if (resultSet.next()) {
            return resultSet.getString("rent_id");  // Return the most recent rent_id directly
        }

        return null;
    }

}
