package com.beone.flagggaming.navbar;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.tiendas.EliminarTiendaFragment;
import com.beone.flagggaming.tiendas.MisProductosFragment;
import com.beone.flagggaming.tiendas.NewProductFragment;
import com.beone.flagggaming.tiendas.PerfilTiendaFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelTiendaFragment extends Fragment {
    int idU,idT;
    TextView txNroTienda, txNombreTienda;
    CardView cvNuevoProducto, cvPerfilTienda, cvMisProductos, cvEliminarTienda;

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
        cvNuevoProducto = root.findViewById(R.id.cvNuevoProducto);
        cvPerfilTienda = root.findViewById(R.id.cvPerfilTienda);
        cvMisProductos = root.findViewById(R.id.cvMisProductos);
        cvEliminarTienda = root.findViewById(R.id.cvEliminarTIenda);

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


        //Habilito los click listener de los card view
        cvNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNuevoProducto();
            }
        });
        cvPerfilTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPerfilTienda();
            }
        });
        cvMisProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMisProductos();
            }
        });
        cvEliminarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEliminarTienda();
            }
        });

        return root;
    }


    //Métodos de enlace cardviews
    public void goNuevoProducto(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new NewProductFragment())
                .addToBackStack(null)
                .commit();
    }
    public void goPerfilTienda(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new PerfilTiendaFragment())
                .addToBackStack(null)
                .commit();
    }
    public void goMisProductos(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new MisProductosFragment())
                .addToBackStack(null)
                .commit();
    }
    public void goEliminarTienda(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new EliminarTiendaFragment())
                .addToBackStack(null)
                .commit();
    }


    //Conexion a SQL
    public Connection conDB(){
        Connection conection2 = null;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conection2 = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conection2;
    }
}