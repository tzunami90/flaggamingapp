package com.beone.flagggaming.navbar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.beone.flagggaming.HomeAcitivity;
import com.beone.flagggaming.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

public class RegisterTiendaFragment extends Fragment {
    int idU,idT;
    EditText edtRazonsocial, edtCuit, edtName, edtMail, edtPass, edtDir, edtHr, edtDays, edtTel, edtInsta;
    Button btnSolicitud;
Layout mainpopup;
    Connection conection = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("idU");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View root = inflater.inflate(R.layout.fragment_register_tienda, container, false);

         edtRazonsocial = root.findViewById(R.id.edtRazonsocial);
         edtCuit = root.findViewById(R.id.edtCuit);
         edtName = root.findViewById(R.id.edtName);
         edtMail = root.findViewById(R.id.edtMail);
         edtPass = root.findViewById(R.id.edtPass);
         edtDir = root.findViewById(R.id.edtDir);
         edtHr = root.findViewById(R.id.edtHr);
         edtDays = root.findViewById(R.id.edtDays);
         edtTel = root.findViewById(R.id.edtTel);
         edtInsta = root.findViewById(R.id.edtInsta);
         btnSolicitud = root.findViewById(R.id.btnSolicitud);

        btnSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud();
            }
        });

        return root;
    }

    public void enviarSolicitud(){
        String rs = edtRazonsocial.getText().toString().trim();
        String cuit = edtCuit.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String mail = edtMail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String dir = edtDir.getText().toString().trim();
        String hr = edtHr.getText().toString().trim();
        String days = edtDays.getText().toString().trim();
        String tel = edtTel.getText().toString().trim();
        String insta = edtInsta.getText().toString().trim();

        //Validaciones de campos
        if (TextUtils.isEmpty(rs)) {
            edtRazonsocial.setError("Ingrese Razón Social de la Tienda");
            edtRazonsocial.requestFocus();
            return;
        }
        if (containsSqlInjection(rs)) {
            edtRazonsocial.setError("Dato inválido");
            edtRazonsocial.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(cuit)) {
            edtCuit.setError("Ingrese CUIT de la Tienda");
            edtCuit.requestFocus();
            return;
        }
        if (containsSqlInjection(cuit)) {
            edtCuit.setError("Dato inválido");
            edtCuit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Ingrese Nombre de Fantasía de la Tienda");
            edtName.requestFocus();
            return;
        }
        if (containsSqlInjection(name)) {
            edtName.setError("Dato inválido");
            edtName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mail)) {
            edtMail.setError("Ingrese E-Mail de la Tienda");
            edtMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            edtMail.setError("Ingrese un correo electrónico válido");
            edtMail.requestFocus();
            return;
        }
        if (containsSqlInjection(mail)) {
            edtMail.setError("Dato inválido");
            edtMail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Ingrese una contraseña para su Tienda");
            edtPass.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtPass.setError("La contraseña debe tener al menos 6 caracteres");
            edtPass.requestFocus();
            return;
        }
        if (containsSqlInjection(pass)) {
            edtPass.setError("Dato inválido");
            edtPass.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dir)) {
            edtDir.setError("Ingrese dirección de su Tienda");
            edtDir.requestFocus();
            return;
        }
        if (containsSqlInjection(dir)) {
            edtDir.setError("Dato inválido");
            edtDir.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(hr)) {
            edtHr.setError("Ingrese horarios de atención");
            edtHr.requestFocus();
            return;
        }
        if (containsSqlInjection(hr)) {
            edtHr.setError("Dato inválido");
            edtHr.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(days)) {
            edtDays.setError("Ingrese dias de atención");
            edtDays.requestFocus();
            return;
        }
        if (containsSqlInjection(days)) {
            edtDays.setError("Dato inválido");
            edtDays.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            edtTel.setError("Ingrese teléfono de su Tienda");
            edtTel.requestFocus();
            return;
        }
        if (containsSqlInjection(tel)) {
            edtTel.setError("Dato inválido");
            edtTel.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(insta)) {
            edtInsta.setError("Ingrese Instagram de su Tienda");
            edtInsta.requestFocus();
            return;
        }
        if (containsSqlInjection(insta)) {
            edtInsta.setError("Dato inválido");
            edtInsta.requestFocus();
            return;
        }

        // LOGICA REGISTRO SQL - Tabla Tiendas
        try{
            if(conDB() == null){
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            }else{
                PreparedStatement pstTienda = conDB().prepareStatement("INSERT INTO tiendas values(?,?,?,?,?,?,?,?,?,?)");
                pstTienda.setString(1,edtRazonsocial.getText().toString());
                pstTienda.setString(2,edtCuit.getText().toString());
                pstTienda.setString(3,edtName.getText().toString());
                pstTienda.setString(4,edtMail.getText().toString());
                pstTienda.setString(5,edtPass.getText().toString());
                pstTienda.setString(6,edtDir.getText().toString());
                pstTienda.setString(7,edtHr.getText().toString());
                pstTienda.setString(8,edtDays.getText().toString());
                pstTienda.setString(9,edtTel.getText().toString());
                pstTienda.setString(10,edtInsta.getText().toString());

               int rowsAffected = pstTienda.executeUpdate();

                if(rowsAffected == 1){
                    String selectQuery = "SELECT id FROM tiendas WHERE mail = ?";
                    PreparedStatement pstSelect = conDB().prepareStatement(selectQuery);
                    pstSelect.setString(1, edtMail.getText().toString());
                    ResultSet resultSet = pstSelect.executeQuery();

                    if(resultSet.next()){
                        idT = resultSet.getInt("id");
                        PreparedStatement pstUsuariosTiendas = conDB().prepareStatement("INSERT INTO usuarios_tiendas (idU,idT,active) values (?,?,?)");
                        pstUsuariosTiendas.setInt(1,idU);
                        pstUsuariosTiendas.setInt(2, idT);
                        pstUsuariosTiendas.setInt(3,0);
                        pstUsuariosTiendas.executeUpdate();
                        pstUsuariosTiendas.close();

                        //FALTA EN EL USUARIO ACTUALIZAR EL ROL TIENDA TAMBIEN

                        Toast.makeText(getContext(), "Solicitud Recibida. Número tienda: "+idT, Toast.LENGTH_SHORT).show();
                    }
                }
                // Cierra el PreparedStatement de tiendas
                pstTienda.close();

                //Redireccion a Fragment HOME
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new HomeFragment())
                        .addToBackStack(null)
                        .commit();
                }
        } catch(SQLException e){
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }

    }

    private boolean containsSqlInjection(String input) {
        // Implementación simple para detectar inyecciones SQL básicas
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "CREATE", "ALTER", "TRUNCATE"};
        input = input.toUpperCase();
        for (String keyword : sqlKeywords) {
            if (input.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    //Conexion a SQL
    public Connection conDB(){

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conection;
    }

}