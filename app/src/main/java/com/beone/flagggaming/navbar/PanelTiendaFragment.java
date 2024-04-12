package com.beone.flagggaming.navbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelTiendaFragment extends Fragment {
    Bundle bundleF;
    int idU,idT;
    TextView txNroTienda, txNombreTienda;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("id");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_panel_tienda, container, false);

        txNroTienda = root.findViewById(R.id.txNroTienda);
        txNombreTienda = root.findViewById(R.id.txNombreTienda);

        try {
            if (conDB() == null) {
                Toast.makeText(root.getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            } else {
                PreparedStatement pstT = conDB().prepareStatement("SELECT * FROM usuarios_tiendas WHERE idU =" + idU + ";");
                pstT.executeQuery();
                ResultSet rsT = pstT.getResultSet();
                if(rsT.next()) {
                    idT = rsT.getInt(2);
                    txNroTienda.setText("Tienda Nro.: " + Integer.toString(idT));
                }else{
                    Toast.makeText(getContext(), "Sin Resultados", Toast.LENGTH_SHORT).show();
                }


                PreparedStatement pst2 = conDB().prepareStatement("SELECT * FROM tiendas WHERE id =" + idT + ";");
                pst2.executeQuery();
                ResultSet rs2 = pst2.getResultSet();
                if(rs2.next()) {
                String nombreT = rs2.getString(4);
                    txNombreTienda.setText(nombreT);
                }else{
                    Toast.makeText(getContext(), "Sin Resultados", Toast.LENGTH_SHORT).show();
                }


            }
        }catch(SQLException e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    //Conexion a SQL
    public Connection conDB(){
        Connection conection2 = null;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conection2 = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conection2;
    }
}