package com.beone.flagggaming.db;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    String classes =  "net.sourceforge.jtds.jdbc.Driver";
    protected static String ip ="DESKTOP-LL2FGQ9";
    protected static String port ="1433";
    protected static String db ="flagg_test";
    protected static String un ="sa";
    protected static String password ="Alexx2003";

    public Connection CONN() {

        Connection conection = null;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(classes);
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip +
                   // ":" + port +
                    ";databaseName=" + db + ";User=" + un + ";passowrd=" + password +";");
        } catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
        return conection;
    }
}
