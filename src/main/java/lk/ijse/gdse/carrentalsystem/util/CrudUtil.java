package lk.ijse.gdse.carrentalsystem.util;

import lk.ijse.gdse.carrentalsystem.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    // A generic method to execute SQL queries, with a flexible return type `T`.
    public static <T> T execute(String sql, Object... obj) throws SQLException, ClassNotFoundException {
        // Get the connection from the DBConnection singleton
        Connection connection = DBConnection.getInstance().getConnection();
        // Prepare the SQL statement
        PreparedStatement pst = connection.prepareStatement(sql);

        // Loop through the variable arguments and set them in the prepared statement
        for (int i = 0; i < obj.length; i++) {
            // Check if the object is a Boolean and convert to 1 (true) or 0 (false)
            if (obj[i] instanceof Boolean) {
                pst.setInt(i + 1, (Boolean) obj[i] ? 1 : 0);
            } else {
                // Set other types of objects (e.g., Strings, BigDecimals, etc.)
                pst.setObject(i + 1, obj[i]);
            }
        }

        // Execute query and return results based on whether it's a SELECT query or an update/insert query
        if (sql.trim().toLowerCase().startsWith("select")) {
            ResultSet resultSet = pst.executeQuery();
            return (T) resultSet;
        } else {
            int result = pst.executeUpdate();
            boolean isSuccess = result > 0;
            return (T) (Boolean) isSuccess;
        }
    }
}
