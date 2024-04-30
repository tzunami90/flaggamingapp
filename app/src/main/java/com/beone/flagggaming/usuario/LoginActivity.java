package com.beone.flagggaming.usuario;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    ProgressBar progressLogin;

    String name,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressLogin = findViewById(R.id.progressLogin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void loginButtonClicked(View v) {
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
        String name, mail;
        int id, rol;

        @Override
        protected void onPreExecute() {
            progressLogin.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false); // Disable login button while logging in
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            try (Connection con = conDB()) {
                if (con != null) {
                    PreparedStatement pst = con.prepareStatement("SELECT * FROM USUARIOS WHERE eMail = ? AND password = ?");
                    pst.setString(1, email);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        id = rs.getInt(1);
                        name = rs.getString(2) + " " + rs.getString(3);
                        mail = rs.getString(4);
                        rol = rs.getInt(6);
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressLogin.setVisibility(View.GONE);
            buttonLogin.setEnabled(true); // Re-enable login button
            if (success) {
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
    //Conexion a SQL
    private Connection conDB() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
