package com.beone.flagggaming.usuario;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    ProgressBar progressLogin;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressLogin = findViewById(R.id.progressLogin);
        adView = findViewById(R.id.adView);

        // Configurar el AdMob
        MobileAds.initialize(this, initializationStatus -> {});

        // Crear y cargar el anuncio
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void loginButtonClicked(View v) {
        // Oculta el teclado virtual inmediatamente después de hacer clic en el botón
        hideKeyboard();

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!isValidEmail(email)) {
            editTextEmail.setError("Correo Electrónico inválido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Por favor, ingresa tu contraseña");
            editTextPassword.requestFocus();
            return;
        }

        if (containsSqlInjection(email) || containsSqlInjection(password)) {
            Toast.makeText(this, "Correo Electrónico o Contraseña inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginTask().execute(email, password);

    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private int id;
        private String name;
        private String mail;
        private int rol;
        private boolean connectionFailed = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLogin.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false); // Disable login button to prevent multiple clicks
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            try (Connection con = DBHelper.conDB(LoginActivity.this)) {
                if (con != null) {
                    PreparedStatement pst = con.prepareStatement("SELECT * FROM USUARIOS WHERE eMail = ?");
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        String storedHashedPassword = rs.getString("password");
                        if (BCrypt.checkpw(password, storedHashedPassword)) {
                            id = rs.getInt("id");
                            name = rs.getString("firstName") + " " + rs.getString("lastName");
                            mail = rs.getString("eMail");
                            rol = rs.getInt("rolTienda");
                            return true;
                        }
                    }
                } else {
                    connectionFailed = true; // No hay conexión con el servidor
                }
            } catch (SQLException e) {
                e.printStackTrace();
                connectionFailed = true; // Error de conexión detectado
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressLogin.setVisibility(View.GONE);
            buttonLogin.setEnabled(true); // Re-enable login button

            if (connectionFailed) {
                // Muestra un toast si no pudo conectar con el servidor
                Toast.makeText(LoginActivity.this, "No se pudo conectar con el servidor. Por favor, reintente más tarde.", Toast.LENGTH_LONG).show();
            } else if (success) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeAcitivity.class);
                intent.putExtra("name", name);
                intent.putExtra("mail", mail);
                intent.putExtra("id", id);
                intent.putExtra("rol", rol);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Mail y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //VALIDACIONES DE INGRESO DE DATOS//
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean containsSqlInjection(String input) {
        String[] sqlKeywords = {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "TABLE"};
        for (String keyword : sqlKeywords) {
            if (input.toUpperCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public void openRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Método para ocultar el teclado virtual
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
