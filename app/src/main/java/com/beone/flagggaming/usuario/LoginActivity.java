package com.beone.flagggaming.usuario;
import android.content.Intent;
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

        try {
            progressLogin.setVisibility(View.VISIBLE);
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Comprobación de nulidad
            if (editTextEmail == null || editTextPassword == null) {
                progressLogin.setVisibility(View.GONE);
                Toast.makeText(this, "EditText no inicializado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Comprobación de vacío
            if (email.isEmpty()) {
                progressLogin.setVisibility(View.GONE);
                editTextEmail.setError("Por favor, ingresa tu correo electrónico ");
                editTextEmail.requestFocus();
                return;
            }

            if (!isValidEmail(email)) {
                progressLogin.setVisibility(View.GONE);
                editTextEmail.setError("Correo Electrónico inválido");
                editTextEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                progressLogin.setVisibility(View.GONE);
                editTextPassword.setError("Por favor, ingresa tu contraseña");
                editTextPassword.requestFocus();
                return;
            }

            // Validación de prevención de inyecciones SQL
            if (containsSqlInjection(email) || containsSqlInjection(password)) {
                progressLogin.setVisibility(View.GONE);
                editTextEmail.setError("Correo Electrónico inválido");
                editTextEmail.requestFocus();
                return;
            }

            // Validaciones pasadas exitosamente:
            try{
                if(conDB() == null){
                    Toast.makeText(this, "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
                }else{

                    PreparedStatement pst = conDB().prepareStatement("SELECT * FROM USUARIOS WHERE eMail = '" + editTextEmail.getText() + "' AND password ='" + editTextPassword.getText()+"';");
                    pst.executeQuery();
                    ResultSet rs = pst.getResultSet();

                    if(rs.next()){
                        int id = rs.getInt(1);
                        name = rs.getString(2) + " " + rs.getString(3);
                        mail = rs.getString(4);
                        int rol = rs.getInt(6);
                        Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeAcitivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("mail", mail);
                        intent.putExtra("id", id);
                        intent.putExtra("rol", rol);
                        startActivity(intent);
                    } else{
                        Toast.makeText(this, "Mail y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }

                }

            } catch(SQLException e){
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ha ocurrido un error. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////////////////////
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
    //////////////////////////////////////////////////////////////


    public void openRegisterActivity(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Conexion a SQL
    public Connection conDB(){
        Connection conection = null;

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conection;
    }

}