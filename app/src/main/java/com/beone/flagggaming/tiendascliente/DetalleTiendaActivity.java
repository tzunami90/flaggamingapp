package com.beone.flagggaming.tiendascliente;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class DetalleTiendaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvName, tvMail, tvDir, tvHr, tvDays, tvTel, tvInsta;
    private GoogleMap mMap;
    private String dir;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ImageView iconAbrirEnMaps, iconAbrirInsta, iconAbrirMail, iconAbrirTel ;
    private String address, subject;
    private Button btn_ver_productos;
    private int idT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_tienda);

        tvName = findViewById(R.id.tv_name);
        tvMail = findViewById(R.id.tv_mail);
        tvDir = findViewById(R.id.tv_dir);
        tvHr = findViewById(R.id.tv_hr);
        tvDays = findViewById(R.id.tv_days);
        tvTel = findViewById(R.id.tv_tel);
        tvInsta = findViewById(R.id.tv_insta);
        iconAbrirEnMaps = findViewById(R.id.iconAbrirEnMaps);
        iconAbrirInsta = findViewById(R.id.iconAbrirInsta);
        iconAbrirMail = findViewById(R.id.iconAbrirMail);
        iconAbrirTel = findViewById(R.id.iconAbrirTel);
        btn_ver_productos = findViewById(R.id.btn_ver_productos);

        // Recibir los datos de la tienda
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvName.setText(extras.getString("name"));
            tvMail.setText("Correo Electrónico: " + extras.getString("mail"));
            tvDir.setText("Dirección: " + extras.getString("dir"));
            tvDays.setText("Dias de Atención: "+ extras.getString("days"));
            tvHr.setText("Horario: " + extras.getString("hr"));
            tvTel.setText("Telefono: "+extras.getString("tel"));
            tvInsta.setText("Instagram: "+extras.getString("insta"));
            dir = extras.getString("dir");
            address = extras.getString("mail");
            idT = extras.getInt("id");
            Log.d("TAG", "Dirección recibida: " + dir);
            Log.d("MAIL", "Mail recibido: " + address);
            Log.d("ID", "ID recibido: " + idT);
        }

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Inicializar el mapa
            initMap();
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

        btn_ver_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verProductos();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalletienda), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Convertir la dirección a coordenadas y mostrarla en el mapa
        LatLng location = getLocationFromAddress(dir);
        if (location != null) {
            mMap.addMarker(new MarkerOptions().position(location).title("Ubicación de la Tienda"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            Log.d("TAG", "Ubicación de la tienda: " + location.toString());
        } else {
            Log.e("TAG", "No se pudo obtener la ubicación de la dirección proporcionada");
            // Manejo de errores: ubicación por defecto
            LatLng defaultLocation = new LatLng(-34.6037, -58.3816); // Coordenadas de Buenos Aires, por ejemplo
            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación no encontrada"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.isEmpty()) {
                Log.e("TAG", "Geocodificación fallida: dirección no encontrada");
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("TAG", "Geocodificación exitosa: " + p1.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("TAG", "Error en la geocodificación", ex);
        }

        return p1;
    }

    public void abrirEnMaps() {
        Log.d("ProductoDetalle", "Método abrirEnMaps llamado");
        String direccion = dir;
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
        String username = tvInsta.getText().toString().replace("Instagram: ", "").trim();

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
    //Metodo para hacer llamada a la tienda
    public void abrirTelefono() {
        Log.d("ProductoDetalle", "Método abrirTelefono llamado");
        String telefono = tvTel.getText().toString();
        if (!telefono.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + telefono));
            if (callIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "No se encontró ninguna aplicación para manejar el Intent.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "El número de teléfono está vacío.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para ver los productos de la tienda
    public void verProductos() {
        Intent intent = new Intent(DetalleTiendaActivity.this, ListaProductosTienda.class);
        intent.putExtra("id", idT);
        startActivity(intent);
    }
}