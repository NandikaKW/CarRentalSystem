package lk.ijse.gdse.carrentalsystem.model;

import lk.ijse.gdse.carrentalsystem.dto.PackageDto;
import lk.ijse.gdse.carrentalsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PackageModel {
    public static ArrayList<PackageDto>  getAllPackages() throws SQLException, ClassNotFoundException {
        ResultSet resultSet =CrudUtil.execute("SELECT * FROM package");
        ArrayList<PackageDto> packageDtos=new ArrayList<>();
        while (resultSet.next()){
            PackageDto packageDto=new PackageDto(
                    resultSet.getString("package_id"),
                    resultSet.getString("package_name"),
                    resultSet.getBigDecimal("total_cost"),
                    resultSet.getBoolean("insurance_included"),
                    resultSet.getString("rental_duration"),
                    resultSet.getDate("rent_date"),
                    resultSet.getString("mileage_limit"),
                    resultSet.getString("description")
            );
            packageDtos.add(packageDto);

        }
        return packageDtos;

    }

    public static boolean deletePackage(String packageId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM package WHERE package_id = ?",packageId);

    }

    public static boolean savePackage(PackageDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO package VALUES (?,?,?,?,?,?,?,?)",dto.getPackageId(),dto.getPackageName(),dto.getTotalCost(),dto.isInsuranceIncluded(),dto.getRentalDuration(),dto.getRentDate(),dto.getMileageLimit(),dto.getDescription());


    }

    public static PackageDto searchPackage(String packageId) throws SQLException, ClassNotFoundException {
        // Corrected SQL query with WHERE clause
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM package WHERE package_id = ?", packageId);

        if (resultSet.next()) {
            return new PackageDto(
                    resultSet.getString("package_id"),        // Fetching by column name
                    resultSet.getString("package_name"),
                    resultSet.getBigDecimal("total_cost"),
                    resultSet.getBoolean("insurance_included"), // Correcting boolean type
                    resultSet.getString("rental_duration"),
                    resultSet.getDate("rent_date"),
                    resultSet.getString("mileage_limit"),
                    resultSet.getString("description")
            );
        }
        return null;
    }

    public static boolean updatePackage(PackageDto dto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE package SET package_name = ?, total_cost = ?, insurance_included = ?, rental_duration = ?, rent_date = ?, mileage_limit = ?, description = ? WHERE package_id = ?",
                dto.getPackageName(),          // package_name
                dto.getTotalCost(),            // total_cost
                dto.isInsuranceIncluded(),     // insurance_included
                dto.getRentalDuration(),       // rental_duration
                dto.getRentDate(),             // rent_date
                dto.getMileageLimit(),         // mileage_limit
                dto.getDescription(),          // description
                dto.getPackageId()             // WHERE package_id
        );

    }


    public static String loadNextPackageId() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT package_id FROM package ORDER BY package_id DESC LIMIT 1");
        if(rst.next()){
            String lastId= rst.getString("package_id"); //P001=rst.getString("package_id");
            String substring=lastId.substring(1);
            int id=Integer.parseInt(substring);
            int newIndex=id+1;
            return String.format("P%03d",newIndex);

        }
        return "P001";
    }
}
