package com.beone.flagggaming.usuario;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.HomeAcitivity;
import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.usuario.LoginActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextRepeatPassword;
    private Button buttonRegister;
    private ProgressBar progressRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressRegister = findViewById(R.id.progressRegister);

        buttonRegister.setOnClickListener(this::registerButtonClicked);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerButtonClicked(View v) {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repeatPassword = editTextRepeatPassword.getText().toString().trim();


        if (TextUtils.isEmpty(firstName)) {
            editTextFirstName.setError("Ingrese su nombre");
            editTextFirstName.requestFocus();
            return;
        }

        if (containsSqlInjection(firstName)) {
            editTextFirstName.setError("Nombre inválido");
            editTextFirstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            editTextLastName.setError("Ingrese su apellido");
            editTextLastName.requestFocus();
            return;
        }

        if (containsSqlInjection(lastName)) {
            editTextLastName.setError("Apellido inválido");
            editTextLastName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Ingrese su correo electrónico");
            editTextEmail.requestFocus();
            return;
        }

        if (containsSqlInjection(email)) {
            editTextEmail.setError("Correo electrónico inválido");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Ingrese un correo electrónico válido");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Ingrese su contraseña");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        if (containsSqlInjection(password)) {
            editTextPassword.setError("Contraseña inválida");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(repeatPassword)) {
            editTextRepeatPassword.setError("Las contraseñas no coinciden");
            editTextRepeatPassword.requestFocus();
            return;
        }

        new RegisterTask().execute(firstName, lastName, email, password);

    }
    //////////////////////////////////////////////////////////////
    //VALIDACIONES DE INGRESO DE DATOS//
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
    //////////////////////////////////////////////////////////////

    public void openLoginActivity(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressRegister.setVisibility(View.VISIBLE);
            buttonRegister.setEnabled(false); // Disable register button to prevent multiple clicks
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String firstName = params[0];
            String lastName = params[1];
            String email = params[2];
            String password = params[3];
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            try (Connection con = DBHelper.conDB(RegisterActivity.this)) {
                if (con != null) {
                    PreparedStatement pst = con.prepareStatement("INSERT INTO USUARIOS (firstName, lastName, eMail, password, rolTienda) VALUES (?, ?, ?, ?, 0)");
                    pst.setString(1, firstName);
                    pst.setString(2, lastName);
                    pst.setString(3, email);
                    pst.setString(4, hashedPassword);
                    int rowsAffected = pst.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressRegister.setVisibility(View.GONE);
            buttonRegister.setEnabled(true); // Re-enable register button
            if (success) {
                Toast.makeText(RegisterActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registro fallido.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}