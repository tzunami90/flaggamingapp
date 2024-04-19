package com.beone.flagggaming.db;

import android.util.Log;
import android.widget.Toast;

import com.beone.flagggaming.steamapi.Juegos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBHelper {

    public static final String DB_URL = "jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;";

    public static Connection conDB() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void saveAppList(List<Juegos> appList) {
        try (Connection connection = conDB()) {
            // Insertar los datos en la tabla (si no existen)
            String insertSQL = "INSERT INTO SteamList (appid, name, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            String selectSQL = "SELECT 1 FROM SteamList WHERE appid = ?";

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                 PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

                for (Juegos app : appList) {
                    selectStatement.setInt(1, app.getAppId());
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (!resultSet.next()) {
                        insertStatement.setInt(1, app.getAppId());
                        insertStatement.setString(2, app.getAppName());
                        insertStatement.executeUpdate();
                    }
                }
                insertStatement.close();
                selectStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }

    }

}
