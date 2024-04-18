package com.beone.flagggaming.tiendas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerfilTiendaFragment extends Fragment {

    Connection conection = null;
    int idU, idT;
    private EditText edtRazonSocial, edtCuit, edtNombreFantasia, edtMail, edtPassword,
            edtDireccion, edtHorarios, edtDiasAtencion, edtTelefono, edtInstagram;
    private Button btnActualizarPerfil;
    private TextInputLayout layoutPassword;
    public PerfilTiendaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("idU");
        idT = getArguments().getInt("idT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil_tienda, container, false);

        // Inicialización de vistas
        edtRazonSocial = root.findViewById(R.id.edtModifRazonsocial);
        edtCuit = root.findViewById(R.id.edtModifCuit);
        edtNombreFantasia = root.findViewById(R.id.edtModifName);
        edtMail = root.findViewById(R.id.edtModifMail);
        edtPassword = root.findViewById(R.id.edtModifPass);
        layoutPassword = root.findViewById(R.id.layoutModifinput);
        edtDireccion = root.findViewById(R.id.edtModifDir);
        edtHorarios = root.findViewById(R.id.edtModifHr);
        edtDiasAtencion = root.findViewById(R.id.edtModifDays);
        edtTelefono = root.findViewById(R.id.edtModifTel);
        edtInstagram = root.findViewById(R.id.edtModifInsta);
        btnActualizarPerfil = root.findViewById(R.id.btnModificarPerfil);

        // Establecer la conexión a la base de datos
        conection = conDB();

        // Obtener y mostrar los datos de la tienda
        obtenerDatosTienda();

        // Configuración del botón de actualización
        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para actualizar los datos de la tienda
                actualizarDatosTienda();
            }
        });

        return root;
    }

    // Método para conectar a la base de datos
    public Connection conDB() {
        Connection con = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return con;
    }

    // Método para obtener y mostrar los datos de la tienda
    private void obtenerDatosTienda() {
        if (conection != null) {
            try {
                String query = "SELECT * FROM tiendas WHERE id = ?";
                PreparedStatement statement = conection.prepareStatement(query);
                statement.setInt(1, idT);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    edtRazonSocial.setText(resultSet.getString("razonSocial"));
                    edtCuit.setText(resultSet.getString("cuit"));
                    edtNombreFantasia.setText(resultSet.getString("name"));
                    edtMail.setText(resultSet.getString("mail"));
                    edtDireccion.setText(resultSet.getString("dir"));
                    edtHorarios.setText(resultSet.getString("hr"));
                    edtDiasAtencion.setText(resultSet.getString("days"));
                    edtTelefono.setText(resultSet.getString("tel"));
                    edtInstagram.setText(resultSet.getString("insta"));
                    edtPassword.setText(resultSet.getString("password"));
                }
            } catch (SQLException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
                Log.d("Error", e.getMessage());
            }
        }
    }

    // Método para actualizar los datos de la tienda
    private void actualizarDatosTienda() {
        // Obtener los nuevos datos de la tienda desde las vistas
        String rs = edtRazonSocial.getText().toString().trim();
        String cuit = edtCuit.getText().toString().trim();
        String name = edtNombreFantasia.getText().toString().trim();
        String mail = edtMail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String dir = edtDireccion.getText().toString().trim();
        String hr = edtHorarios.getText().toString().trim();
        String days = edtDiasAtencion.getText().toString().trim();
        String tel = edtTelefono.getText().toString().trim();
        String insta = edtInstagram.getText().toString().trim();

        // Realizar validaciones de campos
        if (TextUtils.isEmpty(rs)) {
            edtRazonSocial.setError("Ingrese Razón Social de la Tienda");
            edtRazonSocial.requestFocus();
            return;
        }
        if (containsSqlInjection(rs)) {
            edtRazonSocial.setError("Dato inválido");
            edtRazonSocial.requestFocus();
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
            edtNombreFantasia.setError("Ingrese Nombre de Fantasía de la Tienda");
            edtNombreFantasia.requestFocus();
            return;
        }
        if (containsSqlInjection(name)) {
            edtNombreFantasia.setError("Dato inválido");
            edtNombreFantasia.requestFocus();
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
            edtPassword.setError("Ingrese una contraseña para su Tienda");
            edtPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtPassword.setError("La contraseña debe tener al menos 6 caracteres");
            edtPassword.requestFocus();
            return;
        }
        if (containsSqlInjection(pass)) {
            edtPassword.setError("Dato inválido");
            edtPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(dir)) {
            edtDireccion.setError("Ingrese dirección de su Tienda");
            edtDireccion.requestFocus();
            return;
        }
        if (containsSqlInjection(dir)) {
            edtDireccion.setError("Dato inválido");
            edtDireccion.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(hr)) {
            edtHorarios.setError("Ingrese horarios de atención");
            edtHorarios.requestFocus();
            return;
        }
        if (containsSqlInjection(hr)) {
            edtHorarios.setError("Dato inválido");
            edtHorarios.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(days)) {
            edtDiasAtencion.setError("Ingrese dias de atención");
            edtDiasAtencion.requestFocus();
            return;
        }
        if (containsSqlInjection(days)) {
            edtDiasAtencion.setError("Dato inválido");
            edtDiasAtencion.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            edtTelefono.setError("Ingrese teléfono de su Tienda");
            edtTelefono.requestFocus();
            return;
        }
        if (containsSqlInjection(tel)) {
            edtTelefono.setError("Dato inválido");
            edtTelefono.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(insta)) {
            edtInstagram.setError("Ingrese Instagram de su Tienda");
            edtInstagram.requestFocus();
            return;
        }
        if (containsSqlInjection(insta)) {
            edtInstagram.setError("Dato inválido");
            edtInstagram.requestFocus();
            return;
        }


        // Lógica para actualizar los datos en la base de datos
        try {
            if (conDB() == null) {
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            } else {
                // Construir la consulta SQL de actualización
                String updateQuery = "UPDATE tiendas SET razonSocial=?, cuit=?, name=?, mail=?, password=?, dir=?, hr=?, days=?, tel=?, insta=? WHERE id=?";
                PreparedStatement pstUpdate = conDB().prepareStatement(updateQuery);
                pstUpdate.setString(1, rs);
                pstUpdate.setString(2, cuit);
                pstUpdate.setString(3, name);
                pstUpdate.setString(4, mail);
                pstUpdate.setString(5, pass);
                pstUpdate.setString(6, dir);
                pstUpdate.setString(7, hr);
                pstUpdate.setString(8, days);
                pstUpdate.setString(9, tel);
                pstUpdate.setString(10, insta);
                pstUpdate.setInt(11, idT);
                int rowsAffected = pstUpdate.executeUpdate();
                pstUpdate.close();

                if (rowsAffected > 0) {
                    Toast.makeText(getContext(), "Datos de la tienda actualizados correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar los datos de la tienda", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }
    }

    // Método para verificar inyecciones SQL en una cadena
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

}