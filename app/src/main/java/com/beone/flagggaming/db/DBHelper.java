package com.beone.flagggaming.db;

import android.content.Context;
import android.os.StrictMode;
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

    public static Connection conDB(Context context) {
        Connection connection = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            // Conexion AWS o local
            //connection = DriverManager.getConnection("jdbc:jtds:sqlserver://16.171.5.184:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Flagg2024;");
            // Conexion Local
             connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Alexx2003;");
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return connection;
    }

    public static void saveAppList(Context context, List<Juegos> appList) {
        try (Connection connection = conDB(context)) {
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
