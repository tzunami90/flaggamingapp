package com.beone.flagggaming.producto;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class ProductoDetalle extends Fragment {

    private TextView textViewSkuTienda, textViewDescTienda, textViewMarca, textViewPrecioVta, textViewCategoria;
    private TextView textViewTiendaNombre, textViewTiendaDireccion, textViewTiendaDias, textViewTiendaHorario, textViewTiendaMail, textViewTiendaTel, textViewTiendaInsta;
    private ImageView iconAbrirEnMaps, iconAbrirInsta, iconAbrirMail, iconAbrirTel;
    private String address, subject;
    private ProgressBar progressBarTienda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_producto_detalle, container, false);

        ImageView imageViewCategoria = rootView.findViewById(R.id.imageViewCategoria);
        textViewSkuTienda = rootView.findViewById(R.id.textViewSkuTienda);
        textViewDescTienda = rootView.findViewById(R.id.textViewDescTienda);
        textViewMarca = rootView.findViewById(R.id.textViewMarca);
        textViewPrecioVta = rootView.findViewById(R.id.textViewPrecioVta);
        textViewCategoria = rootView.findViewById(R.id.textViewCategoria);
        textViewTiendaNombre = rootView.findViewById(R.id.textViewTiendaNombre);
        textViewTiendaDireccion = rootView.findViewById(R.id.textViewTiendaDireccion);
        textViewTiendaDias = rootView.findViewById(R.id.textViewTiendaDias);
        textViewTiendaHorario = rootView.findViewById(R.id.textViewTiendaHorario);
        textViewTiendaMail = rootView.findViewById(R.id.textViewTiendaMail);
        textViewTiendaTel = rootView.findViewById(R.id.textViewTiendaTel);
        textViewTiendaInsta = rootView.findViewById(R.id.textViewTiendaInsta);
        iconAbrirEnMaps = rootView.findViewById(R.id.iconAbrirEnMaps);
        iconAbrirInsta = rootView.findViewById(R.id.iconAbrirInsta);
        iconAbrirMail = rootView.findViewById(R.id.iconAbrirMail);
        iconAbrirTel = rootView.findViewById(R.id.iconAbrirTel);
        progressBarTienda = rootView.findViewById(R.id.progressBarTienda);


        if (getArguments() != null) {
            Producto producto = (Producto) getArguments().getSerializable("producto");

            if (producto != null) {
                String skuTienda = producto.getSkuTienda();
                String descTienda = producto.getDescTienda();
                String marca = producto.getMarca();
                BigDecimal precioVta = producto.getPrecioVta();
                int idCategoria = producto.getIdCategoria();
                String categoriaDesc = producto.getCategoria().getDesc_categoria();
                String tiendaNombre = producto.getTiendaNombre();
                int idTienda = producto.getIdTienda();
                String imagenUrl = producto.getCategoria().getImagenUrl();

                // Setear los datos en las vistas
                textViewSkuTienda.setText(skuTienda);
                textViewDescTienda.setText(descTienda);
                textViewMarca.setText(marca);
                textViewPrecioVta.setText("$ " + precioVta);
                textViewCategoria.setText(categoriaDesc);
                textViewTiendaNombre.setText("Tienda: " + tiendaNombre);
                // Cargar la imagen de la categoría desde la URL usando Glide
                Glide.with(this)
                        .load(imagenUrl)
                        .placeholder(R.drawable.nopic)
                        .error(R.drawable.nopic)
                        .into(imageViewCategoria);

                new LoadTiendaTask().execute(idTienda);
            }
        }

        // Configuración de los iconos de interacción
        iconAbrirEnMaps.setOnClickListener(v -> abrirEnMaps());
        iconAbrirInsta.setOnClickListener(v -> abrirEnInstagram());
        iconAbrirMail.setOnClickListener(v -> abrirCorreo(address, subject));
        iconAbrirTel.setOnClickListener(v -> abrirTelefono());

        ViewCompat.setOnApplyWindowInsetsListener(rootView.findViewById(R.id.layoutproductodetalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return rootView;

    }

    private class LoadTiendaTask extends AsyncTask<Integer, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBarTienda != null) {
                progressBarTienda.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected HashMap<String, String> doInBackground(Integer... params) {
            int idTienda = params[0]; // Obtiene el idTienda pasado
            return DBHelper.getTiendaById(getContext(), idTienda); // Obtén los datos de la tienda
        }

        @Override
        protected void onPostExecute(HashMap<String, String> tiendaData) {
            progressBarTienda.setVisibility(View.GONE);
            if (tiendaData != null) {
                textViewTiendaDireccion.setText("Dirección: " + tiendaData.get("dir"));
                textViewTiendaDias.setText("Días de Atención: " + tiendaData.get("days"));
                textViewTiendaHorario.setText("Horario de Atención: " + tiendaData.get("hr"));
                textViewTiendaMail.setText("Mail de Tienda: " + tiendaData.get("mail"));
                textViewTiendaTel.setText("Teléfono de Tienda: " + tiendaData.get("tel"));
                textViewTiendaInsta.setText("Instagram: " + tiendaData.get("insta"));

                address = tiendaData.get("mail");

                iconAbrirEnMaps.setVisibility(View.VISIBLE);
                iconAbrirInsta.setVisibility(View.VISIBLE);
                iconAbrirMail.setVisibility(View.VISIBLE);
                iconAbrirTel.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Error al cargar los datos de la tienda", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirEnMaps() {
        Log.d("ProductoDetalle", "Método abrirEnMaps llamado");
        String direccion = textViewTiendaDireccion.getText().toString().replace("Dirección: ", "");
        Log.d("ProductoDetalle", "Dirección obtenida: " + direccion);
        if (!direccion.isEmpty()) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);
        } else {
            Log.d("ProductoDetalle", "La dirección está vacía.");
            Toast.makeText(getContext(), "La dirección está vacía.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para abrir Instagram con el nombre de usuario
    private void abrirEnInstagram() {
        String username = textViewTiendaInsta.getText().toString().replace("Instagram: ", "").trim();
        if (username.startsWith("@")) {
            username = username.substring(1);
        }
        if (!username.isEmpty()) {
            Uri uri = Uri.parse("https://www.instagram.com/" + username);
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(instagramIntent);
        } else {
            Toast.makeText(getContext(), "La tienda no tiene Instagram o no la registró.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para enviar un correo a la tienda
    public void abrirCorreo(String address, String subject) {
        Log.d("Correo", "Mail destinatario: " + address);
        Log.d("Correo", "Subject: " + subject);

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

        if (!telefono.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono));
            startActivity(callIntent);
        } else {
            Log.d("ProductoDetalle", "El número de teléfono está vacío.");
            Toast.makeText(getContext(), "El número de teléfono está vacío.", Toast.LENGTH_SHORT).show();
        }
    }
}