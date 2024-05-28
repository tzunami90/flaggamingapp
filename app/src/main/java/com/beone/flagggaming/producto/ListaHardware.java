package com.beone.flagggaming.producto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListaHardware extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductoClienteAdapter productoClienteAdapter;
    private List<Producto> productoList;
    private List<Producto> filteredList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_hardware);

        recyclerView = findViewById(R.id.recyclerViewProductos);
        SearchView searchView = findViewById(R.id.searchView);

        productoList = new ArrayList<>();
        filteredList = new ArrayList<>();

        new LoadProductsTask().execute();

        filteredList.addAll(productoList);
        productoClienteAdapter = new ProductoClienteAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productoClienteAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarProductos(newText);
                return true;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listahardware), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    private void filtrarProductos(String texto) {
        filteredList.clear();
        if (TextUtils.isEmpty(texto)) {
            filteredList.addAll(productoList);
        } else {
            for (Producto producto : productoList) {
                if (producto.getDescTienda().toLowerCase().contains(texto.toLowerCase()) ||
                        producto.getMarca().toLowerCase().contains(texto.toLowerCase())) {
                    filteredList.add(producto);
                }
            }
        }
        productoClienteAdapter.notifyDataSetChanged();
    }

    private class LoadProductsTask extends AsyncTask<Void, Void, List<Producto>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected List<Producto> doInBackground(Void... voids) {
            List<Producto> productos = new ArrayList<>();
            try {
                Connection connection = DBHelper.conDB(ListaHardware.this);
                if (connection != null) {
                    String query = "SELECT p.id_interno_producto, p.id_tienda, p.id_categoria, p.sku_tienda, p.desc_tienda, p.marca, p.precio_vta, p.estatus, c.desc_categoria, t.name " +
                            "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria JOIN tiendas t ON p.id_tienda = t.id " +
                            "WHERE p.estatus = 1";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        int idInternoProducto = resultSet.getInt("id_interno_producto");
                        int idTienda = resultSet.getInt("id_tienda");
                        int idCategoria = resultSet.getInt("id_categoria");
                        String skuTienda = resultSet.getString("sku_tienda");
                        String descTienda = resultSet.getString("desc_tienda");
                        String marca = resultSet.getString("marca");
                        BigDecimal precioVta = resultSet.getBigDecimal("precio_vta");
                        boolean estatus = resultSet.getBoolean("estatus");
                        String descCategoria = resultSet.getString("desc_categoria");
                        String tiendaNombre = resultSet.getString("name");

                        Categoria categoria = new Categoria(idCategoria, descCategoria);
                        Producto producto = new Producto(idInternoProducto, idTienda, idCategoria, skuTienda, descTienda, marca, precioVta, estatus);
                        producto.setCategoria(categoria);
                        producto.setTiendaNombre(tiendaNombre);
                        productos.add(producto);
                    }

                    resultSet.close();
                    statement.close();
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return productos;
        }

        @Override
        protected void onPostExecute(List<Producto> productos) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            productoList.clear();
            productoList.addAll(productos);
            filteredList.clear();
            filteredList.addAll(productos);
            productoClienteAdapter.notifyDataSetChanged();
        }
    }
}