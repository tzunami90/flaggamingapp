package com.beone.flagggaming.producto;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class ProductoDetalle extends AppCompatActivity {

    private TextView textViewSkuTienda;
    private TextView textViewDescTienda;
    private TextView textViewMarca;
    private TextView textViewPrecioVta;
    private TextView textViewCategoria;
    private TextView textViewTiendaNombre;
    private TextView textViewTiendaDireccion;
    private TextView textViewTiendaDias;
    private TextView textViewTiendaHorario;
    private TextView textViewTiendaMail;
    private TextView textViewTiendaTel;
    private TextView textViewTiendaInsta;
    private ImageView iconAbrirEnMaps, iconAbrirInsta, iconAbrirMail, iconAbrirTel ;
    private String address, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_producto_detalle);

        ImageView imageViewCategoria = findViewById(R.id.imageViewCategoria);
        textViewSkuTienda = findViewById(R.id.textViewSkuTienda);
        textViewDescTienda = findViewById(R.id.textViewDescTienda);
        textViewMarca = findViewById(R.id.textViewMarca);
        textViewPrecioVta = findViewById(R.id.textViewPrecioVta);
        textViewCategoria = findViewById(R.id.textViewCategoria);
        textViewTiendaNombre = findViewById(R.id.textViewTiendaNombre);
        textViewTiendaDireccion = findViewById(R.id.textViewTiendaDireccion);
        textViewTiendaDias = findViewById(R.id.textViewTiendaDias);
        textViewTiendaHorario = findViewById(R.id.textViewTiendaHorario);
        textViewTiendaMail = findViewById(R.id.textViewTiendaMail);
        textViewTiendaTel = findViewById(R.id.textViewTiendaTel);
        textViewTiendaInsta = findViewById(R.id.textViewTiendaInsta);
        iconAbrirEnMaps = findViewById(R.id.iconAbrirEnMaps);
        iconAbrirInsta = findViewById(R.id.iconAbrirInsta);
        iconAbrirMail = findViewById(R.id.iconAbrirMail);
        iconAbrirTel = findViewById(R.id.iconAbrirTel);


        Intent intent = getIntent();

        if (intent != null) {
            // Obtener datos del intent
            String skuTienda = getIntent().getStringExtra("skuTienda");
            String descTienda = getIntent().getStringExtra("descTienda");
            String marca = getIntent().getStringExtra("marca");
            String precioVta = getIntent().getStringExtra("precioVta");
            int idCategoria = getIntent().getIntExtra("idCategoria", -1);
            String categoriaDesc = getIntent().getStringExtra("categoriaDesc");
            String tiendaNombre = getIntent().getStringExtra("tiendaNombre");
            int idTienda = intent.getIntExtra("idTienda", -1);
            String imagenUrl = intent.getStringExtra("imagenUrl"); // Añadir la URL de la imagen



            // Setear los datos en las vistas
            textViewSkuTienda.setText(skuTienda);
            textViewDescTienda.setText(descTienda);
            textViewMarca.setText(marca);
            textViewPrecioVta.setText("$ "+precioVta);
            textViewCategoria.setText(categoriaDesc);
            textViewTiendaNombre.setText("Tienda: " + tiendaNombre);
            // Cargar la imagen de la categoría desde la URL usando Glide
            Glide.with(this)
                    .load(imagenUrl)
                    .placeholder(R.drawable.nopic)
                    .error(R.drawable.nopic)
                    .into(imageViewCategoria);

            new ProductoDetalle.LoadTiendaTask().execute();
        }
        iconAbrirEnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEnMaps();
            }
        });
        iconAbrirInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEnInstagram();
            }
        });
        iconAbrirMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = "Contacto desde Flagg Gaming";
                abrirCorreo(address, subject);
            }
        });
        iconAbrirTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelefono();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutproductodetalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private int getCategoriaImageResource(int idCategoria) {
        switch (idCategoria) {
            case 1: return R.drawable.cat1;
            case 2: return R.drawable.cat2;
            case 3: return R.drawable.cat3;
            case 4: return R.drawable.cat4;
            case 5: return R.drawable.cat5;
            case 6: return R.drawable.cat6;
            case 7: return R.drawable.cat7;
            case 8: return R.drawable.cat8;
            case 9: return R.drawable.cat9;
            case 10: return R.drawable.cat10;
            case 11: return R.drawable.cat11;
            default: return R.drawable.nopic;
        }
    }
    private class LoadTiendaTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBarTienda = findViewById(R.id.progressBarTienda);
            progressBarTienda.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... voids) {
            Intent intent = getIntent();
            int idTienda = intent.getIntExtra("idTienda", -1);

            // Llama al método en DBHelper para obtener los datos de la tienda
            return DBHelper.getTiendaById(ProductoDetalle.this, idTienda);
        }

        @Override
        protected void onPostExecute(HashMap<String, String> tiendaData) {
            ProgressBar progressBarTienda = findViewById(R.id.progressBarTienda);
            progressBarTienda.setVisibility(View.GONE);
            if (tiendaData != null) {
                // Actualiza las vistas con los datos de la tienda
                textViewTiendaDireccion.setText("Dirección: " + tiendaData.get("dir"));
                textViewTiendaDias.setText("Dias de Atención: " + tiendaData.get("days"));
                textViewTiendaHorario.setText("Horario de Atención: " + tiendaData.get("hr"));
                textViewTiendaMail.setText("Mail de Tienda: " + tiendaData.get("mail"));
                textViewTiendaTel.setText("Teléfono de Tienda: " + tiendaData.get("tel"));
                textViewTiendaInsta.setText("Instagram: " + tiendaData.get("insta"));

                address = tiendaData.get("mail");

                // Hacer visible los iconos de redireccion
                iconAbrirEnMaps.setVisibility(View.VISIBLE);
                iconAbrirInsta.setVisibility(View.VISIBLE);
                iconAbrirMail.setVisibility(View.VISIBLE);
                iconAbrirTel.setVisibility(View.VISIBLE);

            }
        }
    }

    public void abrirEnMaps() {
        Log.d("ProductoDetalle", "Método abrirEnMaps llamado");
        String direccion = textViewTiendaDireccion.getText().toString().replace("Dirección: ", "");
        Log.d("ProductoDetalle", "Dirección obtenida: " + direccion);
        if (!direccion.isEmpty()) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));

            // Crear Intent para Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);

        } else {
            Log.d("ProductoDetalle", "La dirección está vacía.");
            Toast.makeText(this, "La dirección está vacía.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para abrir Instagram con el nombre de usuario
    private void abrirEnInstagram() {
        String username = textViewTiendaInsta.getText().toString().replace("Instagram: ", "").trim();

        // Si el nombre de usuario empieza con '@', lo eliminamos
        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        if (!username.isEmpty()) {
            Uri uri = Uri.parse("https://www.instagram.com/" + username);
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(instagramIntent);
        } else {
            Toast.makeText(this, "La tienda no tiene Instagram o no la registró.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para enviar un correo a la tienda
    public void abrirCorreo(String address, String subject) {
        Log.d("Correo", "Mail destinatario: " + address);
        Log.d("Correo", "Subject: " + subject);

        // Sanitizar los valores y codificarlos para que sean seguros en la URI
        String sanitizedAddress = Uri.encode(address.trim());
        String sanitizedSubject = Uri.encode(subject.trim());

        Uri uri = Uri.parse("mailto:" + sanitizedAddress + "?subject=" + sanitizedSubject);

        Intent mail = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(mail);
    }
    // Método para hacer llamada a la tienda
    public void abrirTelefono() {
        Log.d("ProductoDetalle", "Método abrirTelefono llamado");
        String telefono = textViewTiendaTel.getText().toString().trim();

        // Verificar que el número de teléfono no esté vacío
        if (!telefono.isEmpty()) {
            // Crear el intent para marcar el teléfono
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + telefono));

            // Verificar si hay alguna aplicación que pueda manejar el intent
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(callIntent, 0);
            boolean isIntentSafe = activities.size() > 0;

            // Si hay al menos una aplicación que puede manejar el intent, iniciar la actividad
            if (isIntentSafe) {
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "No se encontró ninguna aplicación para manejar la llamada.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "El número de teléfono está vacío.", Toast.LENGTH_SHORT).show();
        }
    }
}