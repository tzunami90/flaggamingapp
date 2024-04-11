package com.beone.flagggaming;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.db.ConnectionDB;
import com.beone.flagggaming.steamapi.ListaJuegosSteam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    Connection conn;
    String str;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword, editTextRepeatPassword;
    private Button buttonRegister;

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

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void register() {
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

        // LOGICA REGISTRO SQL - Tabla Usuarios

        try{
            if(conDB() == null){
                Toast.makeText(this, "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            }else{
                PreparedStatement pst = conDB().prepareStatement("INSERT INTO usuarios values(?,?,?,?)");
                pst.setString(1,editTextFirstName.getText().toString());
                pst.setString(2,editTextLastName.getText().toString());
                pst.setString(3,editTextEmail.getText().toString());
                pst.setString(4,editTextPassword.getText().toString());
                pst.executeUpdate();
                Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                String name = editTextFirstName.getText().toString() + " " + editTextLastName.getText().toString();
                String mail = editTextEmail.getText().toString();
                Intent intent = new Intent(this, HomeAcitivity.class);
                intent.putExtra("name", name);
                intent.putExtra("mail", mail);
                startActivity(intent);
            }

        } catch(SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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