package com.beone.flagggaming.producto;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;

import java.util.HashMap;

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


            // Setear los datos en las vistas
            textViewSkuTienda.setText(skuTienda);
            textViewDescTienda.setText(descTienda);
            textViewMarca.setText(marca);
            textViewPrecioVta.setText("$ "+precioVta);
            textViewCategoria.setText(categoriaDesc);
            textViewTiendaNombre.setText("Tienda: " + tiendaNombre);
            imageViewCategoria.setImageResource(getCategoriaImageResource(idCategoria));

            new ProductoDetalle.LoadTiendaTask().execute();
        }

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

            }
        }
    }

}