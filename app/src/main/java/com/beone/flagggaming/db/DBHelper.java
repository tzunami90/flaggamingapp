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
import java.util.Collections;
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
            String query = "SELECT TOP (10000) [idFlagg], [idJuegoTienda], [nombre], [descripcionCorta], [tienda], [imagen], [imagenMini], [urlTienda], [urlEpic], [requisitos], [estudio], [contadorVistas] FROM [flagg_test3].[dbo].[juegos]";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Juego juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getString("idJuegoTienda"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcionCorta"),
                        resultSet.getString("tienda"),
                        resultSet.getString("imagen"),
                        resultSet.getString("imagenMini"),
                        resultSet.getString("urlTienda"),
                        resultSet.getString("urlEpic"),
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

    public static Juego getJuegoByIdFlagg(Context context, String idFlagg) {
        Juego juego = null;

        try (Connection connection = conDB(context)) {
            String query = "SELECT [idFlagg], [idJuegoTienda], [nombre], [descripcionCorta], [tienda], [imagen], [imagenMini], [urlTienda],  [urlEpic], [requisitos], [estudio], [contadorVistas] FROM [flagg_test3].[dbo].[juegos] WHERE [idFlagg] = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idFlagg);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getString("idJuegoTienda"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcionCorta"),
                        resultSet.getString("tienda"),
                        resultSet.getString("imagen"),
                        resultSet.getString("imagenMini"),
                        resultSet.getString("urlTienda"),
                        resultSet.getString("urlEpic"),
                        resultSet.getString("requisitos"),
                        resultSet.getString("estudio"),
                        resultSet.getInt("contadorVistas")
                );
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al obtener el juego por idFlagg: " + e.getMessage());
        }

        return juego;
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

    public static void incrementarContadorVistas(Context context, String idFlagg) {
        try (Connection connection = conDB(context)) {
            // Consulta para obtener el valor actual del contadorVistas
            String selectQuery = "SELECT contadorVistas FROM juegos WHERE idFlagg = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, idFlagg);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int contadorActual = resultSet.getInt("contadorVistas");

                // Incrementar el contador
                contadorActual++;

                // Consulta para actualizar el contadorVistas
                String updateQuery = "UPDATE juegos SET contadorVistas = ? WHERE idFlagg = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, contadorActual);
                updateStatement.setString(2, idFlagg);

                // Ejecutar la actualización
                updateStatement.executeUpdate();

                updateStatement.close();
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al incrementar contadorVistas: " + e.getMessage());
        }
    }

    public static List<Juego> getOfertasDestacadas(Context context) {
        List<Juego> juegosList = new ArrayList<>();

        try (Connection connection = conDB(context)) {
            String query = "SELECT TOP (100) j.[idFlagg], j.[nombre], j.[imagen], o.[discount_percent] " +
                    "FROM [juegos] j " +
                    "INNER JOIN [ofertas] o ON j.[idOferta] = o.[idOferta] " +
                    "WHERE j.[imagen] IS NOT NULL AND j.[idOferta] IS NOT NULL";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Juego juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getString("nombre"),
                        resultSet.getString("imagen"),
                        resultSet.getString("discount_percent")
                );
                juegosList.add(juego);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al obtener juegos con ofertas destacadas: " + e.getMessage());
        }

        Log.d("DBHelper", "Ofertas destacadas obtenidas: " + juegosList.size());

        // Seleccionar aleatoriamente 8 juegos
        Collections.shuffle(juegosList);
        return juegosList.size() > 8 ? juegosList.subList(0, 8) : juegosList;
    }

    public static List<Juego> getJuegosDestacados(Context context) {
        List<Juego> juegosList = new ArrayList<>();

        try (Connection connection = conDB(context)) {
            String query = "SELECT TOP (8) [idFlagg], [nombre], [imagen] " +
                    "FROM [juegos] " +
                    "WHERE [imagen] IS NOT NULL " +
                    "ORDER BY [contadorVistas] DESC";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Juego juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getString("nombre"),
                        resultSet.getString("imagen"),
                        null // o un valor predeterminado
                );
                juegosList.add(juego);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al obtener juegos destacados: " + e.getMessage());
        }

        Log.d("DBHelper", "Juegos destacados obtenidos: " + juegosList.size());

        return juegosList;
    }

    public static List<Juego> getJuegosAleatorios(Context context, int cantidad) {
        List<Juego> juegosList = new ArrayList<>();

        try (Connection connection = conDB(context)) {
            // Consulta para obtener una cantidad específica de juegos aleatorios
            String query = "SELECT TOP (?) [idFlagg], [idJuegoTienda], [nombre], [descripcionCorta], [tienda], [imagen], [imagenMini], [urlTienda], [urlEpic], [requisitos], [estudio], [contadorVistas] " +
                    "FROM [flagg_test3].[dbo].[juegos] " +
                    "ORDER BY NEWID()"; // NEWID() genera un GUID aleatorio para cada fila, lo que permite seleccionar de manera aleatoria

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cantidad);  // Pasar la cantidad de juegos aleatorios que queremos seleccionar
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Juego juego = new Juego(
                        resultSet.getString("idFlagg"),
                        resultSet.getString("idJuegoTienda"),
                        resultSet.getString("nombre"),
                        resultSet.getString("descripcionCorta"),
                        resultSet.getString("tienda"),
                        resultSet.getString("imagen"),
                        resultSet.getString("imagenMini"),
                        resultSet.getString("urlTienda"),
                        resultSet.getString("urlEpic"),
                        resultSet.getString("requisitos"),
                        resultSet.getString("estudio"),
                        resultSet.getInt("contadorVistas")
                );
                juegosList.add(juego);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            Log.e("DBHelper", "Error al obtener juegos aleatorios: " + e.getMessage());
        }

        Log.d("DBHelper", "Juegos aleatorios obtenidos: " + juegosList.size());

        return juegosList;
    }



}
