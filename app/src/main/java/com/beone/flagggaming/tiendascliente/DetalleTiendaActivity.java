package com.beone.flagggaming.tiendascliente;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.beone.flagggaming.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class DetalleTiendaActivity extends Fragment implements OnMapReadyCallback {

    private TextView tvName, tvMail, tvDir, tvHr, tvDays, tvTel, tvInsta;
    private GoogleMap mMap;
    private String dir;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ImageView iconAbrirEnMaps, iconAbrirInsta, iconAbrirMail, iconAbrirTel ;
    private String address, subject;
    private Button btn_ver_productos;
    private int idT;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detalle_tienda, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvMail = view.findViewById(R.id.tv_mail);
        tvDir = view.findViewById(R.id.tv_dir);
        tvHr = view.findViewById(R.id.tv_hr);
        tvDays = view.findViewById(R.id.tv_days);
        tvTel = view.findViewById(R.id.tv_tel);
        tvInsta = view.findViewById(R.id.tv_insta);
        iconAbrirEnMaps = view.findViewById(R.id.iconAbrirEnMaps);
        iconAbrirInsta = view.findViewById(R.id.iconAbrirInsta);
        iconAbrirMail = view.findViewById(R.id.iconAbrirMail);
        iconAbrirTel = view.findViewById(R.id.iconAbrirTel);
        btn_ver_productos = view.findViewById(R.id.btn_ver_productos);

        // Recibir los datos de la tienda
        Bundle extras = getArguments();
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

        // Verificar y solicitar permisos de ubicación
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }

        // Listeners de los íconos
        iconAbrirEnMaps.setOnClickListener(v -> abrirEnMaps());
        iconAbrirInsta.setOnClickListener(v -> abrirEnInstagram());
        iconAbrirMail.setOnClickListener(v -> {
            subject = "Contacto desde Flagg Gaming";
            abrirCorreo(address, subject);
        });
        iconAbrirTel.setOnClickListener(v -> abrirTelefono());

        btn_ver_productos.setOnClickListener(view1 -> verProductos());

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.detalletienda), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return view;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMap();
        } else {
            Toast.makeText(getActivity(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        // Convertir la dirección a coordenadas y mostrarla en el mapa
        LatLng location = getLocationFromAddress(dir);
        if (location != null) {
            mMap.addMarker(new MarkerOptions().position(location).title("Ubicación de la Tienda"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        } else {
            LatLng defaultLocation = new LatLng(-34.6037, -58.3816); // Coordenadas de Buenos Aires
            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación no encontrada"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getActivity());
        List<Address> addressList;
        LatLng p1 = null;

        try {
            addressList = coder.getFromLocationName(strAddress, 5);
            if (addressList != null && !addressList.isEmpty()) {
                Address location = addressList.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }

    public void abrirEnMaps() {
        if (!dir.isEmpty()) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(dir));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);
        } else {
            Toast.makeText(getActivity(), "La dirección está vacía.", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirEnInstagram() {
        String username = tvInsta.getText().toString().replace("Instagram: ", "").trim();
        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        if (!username.isEmpty()) {
            Uri uri = Uri.parse("https://www.instagram.com/" + username);
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(instagramIntent);
        } else {
            Toast.makeText(getActivity(), "La tienda no tiene Instagram o no la registró.", Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirCorreo(String address, String subject) {
        Uri uri = Uri.parse("mailto:" + Uri.encode(address) + "?subject=" + Uri.encode(subject));
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(mailIntent);
    }

    public void abrirTelefono() {
        String telefono = tvTel.getText().toString();
        if (!telefono.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono));
            startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "El número de teléfono está vacío.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para ver los productos de la tienda
    public void verProductos() {
        // Crear una instancia del Fragment
        ListaProductosTienda fragment = new ListaProductosTienda();

// Crear el bundle con el ID que quieres pasar
        Bundle bundle = new Bundle();
        bundle.putInt("id", idT); // Pasar el ID de la tienda
        fragment.setArguments(bundle);

// Reemplazar el contenido actual con el nuevo Fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // R.id.fragment_container es el ID del contenedor de Fragmentos
        transaction.addToBackStack(null); // Opcional: Si quieres que pueda volver atrás
        transaction.commit();
    }
}