package com.beone.flagggaming.navbar;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.tiendas.Tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterTiendaFragment extends Fragment {
    int idU,idT;
    EditText edtRazonsocial, edtCuit, edtName, edtMail, edtPass, edtDir, edtHr, edtDays, edtTel, edtInsta;
    Button btnSolicitud;
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

        if (!validarCampos(rs, cuit, name, mail, pass, dir, hr, days, tel, insta)) return;

        Tienda tienda = new Tienda(0, name, mail, dir, days, hr, insta, tel);

        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (registrarTienda(connection, tienda, rs, cuit, pass)) {
                Toast.makeText(getContext(), "Solicitud Recibida. Número tienda: " + idT, Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean validarCampos(String rs, String cuit, String name, String mail, String pass, String dir, String hr, String days, String tel, String insta) {
        if (TextUtils.isEmpty(rs)) {
            edtRazonsocial.setError("Ingrese Razón Social de la Tienda");
            edtRazonsocial.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(cuit)) {
            edtCuit.setError("Ingrese CUIT de la Tienda");
            edtCuit.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Ingrese Nombre de Fantasía de la Tienda");
            edtName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mail) || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            edtMail.setError("Ingrese un correo electrónico válido");
            edtMail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            edtPass.setError("La contraseña debe tener al menos 6 caracteres");
            edtPass.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(dir)) {
            edtDir.setError("Ingrese dirección de su Tienda");
            edtDir.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(hr)) {
            edtHr.setError("Ingrese horarios de atención");
            edtHr.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(days)) {
            edtDays.setError("Ingrese días de atención");
            edtDays.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(tel)) {
            edtTel.setError("Ingrese teléfono de su Tienda");
            edtTel.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(insta)) {
            edtInsta.setError("Ingrese Instagram de su Tienda");
            edtInsta.requestFocus();
            return false;
        }
        return true;
    }

    private boolean registrarTienda(Connection connection, Tienda tienda, String rs, String cuit, String pass) throws SQLException {
        String insertQuery = "INSERT INTO tiendas (razonSocial, cuit, name, mail, password, dir, hr, days, tel, insta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstTienda = connection.prepareStatement(insertQuery)) {
            pstTienda.setString(1, rs);
            pstTienda.setString(2, cuit);
            pstTienda.setString(3, tienda.getName());
            pstTienda.setString(4, tienda.getMail());
            pstTienda.setString(5, pass);
            pstTienda.setString(6, tienda.getDir());
            pstTienda.setString(7, tienda.getHr());
            pstTienda.setString(8, tienda.getDays());
            pstTienda.setString(9, tienda.getTel());
            pstTienda.setString(10, tienda.getInsta());

            int rowsAffected = pstTienda.executeUpdate();
            if (rowsAffected == 1) {
                return vincularUsuarioTienda(connection, tienda.getMail());
            }
        }
        return false;
    }

    private boolean vincularUsuarioTienda(Connection connection, String mail) throws SQLException {
        String selectQuery = "SELECT id FROM tiendas WHERE mail = ?";
        try (PreparedStatement pstSelect = connection.prepareStatement(selectQuery)) {
            pstSelect.setString(1, mail);
            try (ResultSet resultSet = pstSelect.executeQuery()) {
                if (resultSet.next()) {
                    idT = resultSet.getInt("id");
                    String insertUsuariosTiendas = "INSERT INTO usuarios_tiendas (idU, idT, active) VALUES (?, ?, ?)";
                    try (PreparedStatement pstUsuariosTiendas = connection.prepareStatement(insertUsuariosTiendas)) {
                        pstUsuariosTiendas.setInt(1, idU);
                        pstUsuariosTiendas.setInt(2, idT);
                        pstUsuariosTiendas.setInt(3, 0);
                        pstUsuariosTiendas.executeUpdate();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}