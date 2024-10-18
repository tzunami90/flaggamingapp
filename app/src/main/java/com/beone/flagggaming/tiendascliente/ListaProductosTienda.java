package com.beone.flagggaming.tiendascliente;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.beone.flagggaming.producto.Producto;
import com.beone.flagggaming.producto.ProductoClienteAdapter;
import com.beone.flagggaming.producto.ProductoDetalle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListaProductosTienda extends Fragment {

    private int idT;
    private RecyclerView recyclerView;
    private ProductoClienteAdapter productoClienteAdapter;
    private List<Producto> productoList;
    private List<Producto> filteredList;
    private ProgressBar progressBar;
    private boolean categoriesLoaded = false;
    private boolean productsLoaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_productos_tienda, container, false);

        if (getArguments() != null) {
            idT = getArguments().getInt("id"); // Obtener el ID desde los argumentos del fragmento
            Log.d("ID", "ID de la tienda recibido: " + idT);
        }

        recyclerView = view.findViewById(R.id.recyclerViewProductos);
        SearchView searchView = view.findViewById(R.id.searchView);
        Spinner spinnerCategorias = view.findViewById(R.id.spinnerCategorias);
        progressBar = view.findViewById(R.id.progressBar);

        productoList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Mostrar el ProgressBar al inicio
        showProgressBar();

        // Cargar las categorías y productos
        new LoadCategoriesTask().execute();

        filteredList.addAll(productoList);
        productoClienteAdapter = new ProductoClienteAdapter(getActivity(), filteredList, new ProductoClienteAdapter.OnProductoClickListener() {
            @Override
            public void onProductoClick(Producto producto) {
                // Aquí puedes manejar el clic sobre un producto
                // Redirigir a otro Fragment o Activity, por ejemplo:
                Fragment fragment = new ProductoDetalle();
                Bundle args = new Bundle();
                args.putSerializable("producto", producto);  // Pasar el objeto Producto al fragment
                // Ahora también puedes pasar idTienda si no está en el objeto Producto
                args.putInt("idTienda", producto.getIdTienda()); // Pasa el idTienda

                fragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment) // Reemplaza con tu container
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.listaproductostienda), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return view;
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
                Connection connection = DBHelper.conDB(getActivity());
                if (connection != null) {
                    String query = "SELECT id_categoria, desc_categoria, imagen_url FROM categorias";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery();

                    categorias.add(new Categoria(0, "Todas las categorías", "")); // Opción por defecto

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

        @Override
        protected void onPostExecute(List<Categoria> categorias) {
            Spinner spinnerCategorias = getView().findViewById(R.id.spinnerCategorias);
            ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categorias);
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
                Connection connection = DBHelper.conDB(getActivity());
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
                        // Precargar imágenes para un rendimiento más fluido
                        Glide.with(getActivity())
                                .load(imagenUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload();
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