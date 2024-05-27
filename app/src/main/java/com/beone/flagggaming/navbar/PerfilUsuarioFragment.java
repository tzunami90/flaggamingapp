package com.beone.flagggaming.navbar;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beone.flagggaming.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerfilUsuarioFragment extends Fragment {
    Connection conection = null;
    int idU;
    EditText editTextFirstNameModif, editTextLastNameModif, editTextEmailModif, editTextPasswordModif, editTextRepeatPasswordModif;
    Button buttonModifcarUsuario;
    String firstName, lastName, email, password;
    ProgressBar progressBar;
    public PerfilUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        // Encuentra la referencia al ProgressBar
        ProgressBar progressBar = root.findViewById(R.id.progressBar);

        // Mostrar el ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        editTextFirstNameModif = root.findViewById(R.id.editTextFirstNameModif);
        editTextLastNameModif = root.findViewById(R.id.editTextLastNameModif);
        editTextEmailModif = root.findViewById(R.id.editTextEmailModif);
        editTextPasswordModif = root.findViewById(R.id.editTextPasswordModif);
        editTextRepeatPasswordModif = root.findViewById(R.id.editTextRepeatPasswordModif);
        buttonModifcarUsuario = root.findViewById(R.id.buttonModifcarUsuario);

        // Cargar datos del usuario en segundo plano
        new LoadUserDataAsyncTask(progressBar).execute();

        // Configurar clics de botones
        buttonModifcarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para modificar el usuario
                modificarPerfilUsuario();
            }
        });

        return root;
    }
    private class LoadUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressBar progressBar;
        public LoadUserDataAsyncTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatosUsuario();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Actualizar los EditText con los datos del usuario
            editTextFirstNameModif.setText(firstName);
            editTextLastNameModif.setText(lastName);
            editTextEmailModif.setText(email);
            editTextPasswordModif.setText(password);
            editTextRepeatPasswordModif.setText(password);
            // Ocultar el ProgressBar
            progressBar.setVisibility(View.GONE);
        }
    }

    // Método para conectar a la base de datos
    public Connection conDB() {
        conection = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //Conexion AWS
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://16.171.5.184:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Flagg2024;");
            //Conexion Local
            //conection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return conection;
    }

    // Método para cargar datos del usuario desde la base de datos
    private void cargarDatosUsuario() {
        try {
            conection = conDB();
            if (conection != null) {
                PreparedStatement statement = conection.prepareStatement("SELECT firstName, lastName, eMail, password FROM usuarios WHERE id = ?");
                statement.setInt(1, idU);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                   firstName = resultSet.getString("firstName");
                   lastName = resultSet.getString("lastName");
                   email = resultSet.getString("eMail");
                   password = resultSet.getString("password");
                }
                statement.close();
                resultSet.close();
                conection.close();
            } else {
                Toast.makeText(getContext(), "Error de conexión a la base de datos", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void modificarPerfilUsuario() {
        String firstName = editTextFirstNameModif.getText().toString().trim();
        String lastName = editTextLastNameModif.getText().toString().trim();
        String email = editTextEmailModif.getText().toString().trim();
        String password = editTextPasswordModif.getText().toString().trim();
        String repeatPassword = editTextRepeatPasswordModif.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(repeatPassword)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que el correo electrónico sea válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que no haya inyección SQL en los campos
        if (containsSqlInjection(firstName) || containsSqlInjection(lastName) || containsSqlInjection(email) || containsSqlInjection(password) || containsSqlInjection(repeatPassword)) {
            Toast.makeText(getContext(), "Los campos contienen datos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            conection = conDB();
            if (conection != null) {
                PreparedStatement statementUpdate = conection.prepareStatement("UPDATE usuarios SET firstName = ?, lastName = ?, eMail = ?, password = ? WHERE id = ?");
                statementUpdate.setString(1, firstName);
                statementUpdate.setString(2, lastName);
                statementUpdate.setString(3, email);
                statementUpdate.setString(4, password);
                statementUpdate.setInt(5, idU);

                int rowsAffected = statementUpdate.executeUpdate();

                if (rowsAffected > 0) {
                    Toast.makeText(getContext(), "Perfil de usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se pudo actualizar el perfil del usuario", Toast.LENGTH_SHORT).show();
                }

                statementUpdate.close();
                conection.close();
            } else {
                Toast.makeText(getContext(), "Error de conexión a la base de datos", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }
    }

}