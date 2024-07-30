package com.beone.flagggaming.db;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.beone.flagggaming.juegos.Juego;
import com.beone.flagggaming.steamapi.Juegos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper {

    public static Connection conDB(Context context) {
        Connection connection = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            // Conexion Servidor Remoto de AWS
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://13.50.158.9:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Flagg2024;");

            // Conexion Local PC
            //connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Alexx2003;");
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return connection;
    }

    public static List<Juego> getJuegos(Context context) {
        List<Juego> juegosList = new ArrayList<>();

        try (Connection connection = conDB(context)) {
            String query = "SELECT TOP (10000) [idFlagg], [idJuegoTienda], [nombre], [descripcionCorta], [tienda], [imagen], [imagenMini], [urlTienda], [requisitos], [estudio], [contadorVistas] FROM [flagg_test3].[dbo].[juegos]";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Juego juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getInt("idJuegoTienda"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcionCorta"),
                        resultSet.getString("tienda"),
                        resultSet.getString("imagen"),
                        resultSet.getString("imagenMini"),
                        resultSet.getString("urlTienda"),
                        resultSet.getString("requisitos"),
                        resultSet.getString("estudio"),
                        resultSet.getInt("contadorVistas")
                );
                juegosList.add(juego);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al obtener la lista de juegos: " + e.getMessage());
        }

        return juegosList;
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

    public static HashMap<String, String> getTiendaById(Context context, int idTienda) {
        HashMap<String, String> tiendaData = new HashMap<>();

        try (Connection connection = conDB(context)) {
            // Consulta para obtener los datos de la tienda por ID
            String query = "SELECT [dir], [hr], [mail], [days], [tel], [insta] FROM [tiendas] WHERE [id] = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idTienda);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtener los datos de la tienda del resultado de la consulta y agregarlos al HashMap
                tiendaData.put("dir", resultSet.getString("dir"));
                tiendaData.put("days", resultSet.getString("days"));
                tiendaData.put("hr", resultSet.getString("hr"));
                tiendaData.put("mail", resultSet.getString("mail"));
                tiendaData.put("tel", resultSet.getString("tel"));
                tiendaData.put("insta", resultSet.getString("insta"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiendaData;
    }

}
