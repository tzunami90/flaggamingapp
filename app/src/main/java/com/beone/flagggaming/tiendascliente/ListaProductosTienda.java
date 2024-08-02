package com.beone.flagggaming.tiendascliente;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.producto.Categoria;
import com.beone.flagggaming.producto.ListaHardware;
import com.beone.flagggaming.producto.Producto;
import com.beone.flagggaming.producto.ProductoClienteAdapter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListaProductosTienda extends AppCompatActivity {

    private int idT;
    private RecyclerView recyclerView;
    private ProductoClienteAdapter productoClienteAdapter;
    private List<Producto> productoList;
    private List<Producto> filteredList;
    private ProgressBar progressBar;
    private boolean categoriesLoaded = false;
    private boolean productsLoaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_productos_tienda);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idT = extras.getInt("id");
            Log.d("ID", "ID de la tienda recibido: " + idT);
        }

        recyclerView = findViewById(R.id.recyclerViewProductos);
        SearchView searchView = findViewById(R.id.searchView);
        Spinner spinnerCategorias = findViewById(R.id.spinnerCategorias);
        progressBar = findViewById(R.id.progressBar);

        productoList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Mostrar el ProgressBar al inicio
        showProgressBar();

        // Cargar las categorías y productos
        new LoadCategoriesTask().execute();

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
                Categoria selectedCategory = (Categoria) spinnerCategorias.getSelectedItem();
                int categoryId = selectedCategory != null ? selectedCategory.getId_categoria() : 0;
                filtrarProductos(newText, categoryId);
                return true;
            }
        });

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria selectedCategory = (Categoria) parent.getSelectedItem();
                int categoryId = selectedCategory != null ? selectedCategory.getId_categoria() : 0;
                filtrarProductos(searchView.getQuery().toString(), categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Filtrar productos con categoría por defecto (todas las categorías)
                filtrarProductos(searchView.getQuery().toString(), 0);
            }
        });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listaproductostienda), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
    }

    private void filtrarProductos(String texto, int categoriaId) {
        List<Producto> filteredList = new ArrayList<>();
        for (Producto producto : productoList) {
            boolean matchesText = producto.getSkuTienda().toLowerCase().contains(texto.toLowerCase()) ||
                    producto.getDescTienda().toLowerCase().contains(texto.toLowerCase()) ||
                    producto.getMarca().toLowerCase().contains(texto.toLowerCase()) ||
                    producto.getCategoria().getDesc_categoria().toLowerCase().contains(texto.toLowerCase()) ||
                    producto.getTiendaNombre().toLowerCase().contains(texto.toLowerCase());
            boolean matchesCategory = categoriaId == 0 || producto.getIdCategoria() == categoriaId;

            if (matchesText && matchesCategory) {
                filteredList.add(producto);
            }
        }
        productoClienteAdapter.updateList(filteredList);
    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (categoriesLoaded && productsLoaded) {
            progressBar.setVisibility(View.GONE);
            // Llamar a filtrarProductos para mostrar todos los productos inicialmente
            filtrarProductos("", 0);
        }
    }

    private class LoadCategoriesTask extends AsyncTask<Void, Void, List<Categoria>> {
        @Override
        protected List<Categoria> doInBackground(Void... voids) {
            List<Categoria> categorias = new ArrayList<>();
            try {
                Connection connection = DBHelper.conDB(ListaProductosTienda.this);
                if (connection != null) {
                    String query = "SELECT id_categoria, desc_categoria, imagen_url FROM categorias";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    categorias.add(new Categoria(0, "Todas las categorías", "")); // Añadir opción por defecto

                    while (resultSet.next()) {
                        int idCategoria = resultSet.getInt("id_categoria");
                        String descCategoria = resultSet.getString("desc_categoria");
                        String imagenUrl = resultSet.getString("imagen_url");
                        Categoria categoria = new Categoria(idCategoria, descCategoria, imagenUrl);
                        categorias.add(categoria);
                    }

                    resultSet.close();
                    statement.close();
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categorias;
        }
        protected void onPostExecute(List<Categoria> categorias) {
            Spinner spinnerCategorias = findViewById(R.id.spinnerCategorias);
            ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(ListaProductosTienda.this, android.R.layout.simple_spinner_item, categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategorias.setAdapter(adapter);

            categoriesLoaded = true;
            hideProgressBar();

            // Cargar los productos después de configurar el Spinner
            new LoadProductsTask().execute();
        }
    }

    private class LoadProductsTask extends AsyncTask<Void, Void, List<Producto>> {
        @Override
        protected List<Producto> doInBackground(Void... voids) {
            List<Producto> productos = new ArrayList<>();
            try {
                Connection connection = DBHelper.conDB(ListaProductosTienda.this);
                if (connection != null) {
                    String query = "SELECT p.id_interno_producto, p.id_tienda, p.id_categoria, p.sku_tienda, p.desc_tienda, p.marca, p.precio_vta, p.estatus, c.desc_categoria, c.imagen_url, t.name " +
                            "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria JOIN tiendas t ON p.id_tienda = t.id " +
                            "WHERE p.estatus = 1 AND p.id_tienda = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, idT);
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
                        String imagenUrl = resultSet.getString("imagen_url");

                        Categoria categoria = new Categoria(idCategoria, descCategoria, imagenUrl);
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
            productsLoaded = true;
            hideProgressBar();

            productoList.clear();
            productoList.addAll(productos);
            filteredList.clear();
            filteredList.addAll(productos);
            productoClienteAdapter.notifyDataSetChanged();
            // Llamar a filtrarProductos para mostrar todos los productos inicialmente
            filtrarProductos("", 0);
        }
    }
}