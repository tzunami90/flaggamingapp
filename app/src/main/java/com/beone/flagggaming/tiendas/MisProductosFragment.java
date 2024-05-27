package com.beone.flagggaming.tiendas;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.producto.Categoria;
import com.beone.flagggaming.producto.Producto;
import com.beone.flagggaming.producto.ProductoAdapter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MisProductosFragment extends Fragment {
    Connection conection = null;
    int idU, idT;
    View root;

    public MisProductosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("idU");
        idT = getArguments().getInt("idT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_mis_productos, container, false);

        // Configura el RecyclerView
        RecyclerView recyclerViewProductos = root.findViewById(R.id.recyclerViewProducts);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Muestra el ProgressBar
        ProgressBar progressBarMisProductos = root.findViewById(R.id.progressBarMisProductos);
        progressBarMisProductos.setVisibility(View.VISIBLE);

        // Realiza la tarea de obtención de productos en segundo plano
        obtenerProductosDesdeBaseDeDatos();

        return root;
    }

    private void obtenerProductosDesdeBaseDeDatos() {
        // Realiza la tarea de obtención de productos en segundo plano
        new AsyncTask<Void, Void, List<Producto>>() {
            @Override
            protected List<Producto> doInBackground(Void... voids) {
                // Coloca aquí tu lógica para obtener productos desde la base de datos
                return obtenerProductos();
            }

            @Override
            protected void onPostExecute(List<Producto> productosList) {
                // Oculta el ProgressBar
                ProgressBar progressBarMisProductos = root.findViewById(R.id.progressBarMisProductos);
                progressBarMisProductos.setVisibility(View.GONE);

                // Crea y establece el adaptador para el RecyclerView
                ProductoAdapter productosAdapter = new ProductoAdapter(productosList);
                RecyclerView recyclerViewProductos = root.findViewById(R.id.recyclerViewProducts);
                recyclerViewProductos.setAdapter(productosAdapter);

                // Establecer el listener de clic en el adaptador
                productosAdapter.setOnItemClickListener(new ProductoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int productId) {
                        // Crear el fragmento de detalles del producto y pasar el ID del producto
                        DetallesProductoFragment detallesFragment = new DetallesProductoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("idP", productId); // Pasar el ID del producto al fragmento
                        bundle.putInt("idU", idU);
                        bundle.putInt("idT", idT);
                        detallesFragment.setArguments(bundle);

                        // Reemplazar el fragmento actual con el fragmento de detalles del producto
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, detallesFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        }.execute();
    }

    private List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Obtener la conexión a la base de datos
            conection = conDB();

            // Consulta SQL para seleccionar productos
            String query = "SELECT p.id_interno_producto, p.id_tienda, p.id_categoria, p.sku_tienda, p.desc_tienda, p.marca, p.precio_vta, p.estatus, c.desc_categoria " +
                    "FROM productos p " +
                    "JOIN categorias c ON p.id_categoria = c.id_categoria " +
                    "WHERE p.id_tienda = ?";
            statement = conection.prepareStatement(query);
            statement.setInt(1, idT); // Asignar el valor de idT al parámetro de la consulta
            resultSet = statement.executeQuery();

            // Recorrer los resultados y crear objetos Producto
            while (resultSet.next()) {
                int idInternoProducto = resultSet.getInt("id_interno_producto");
                int idTienda = resultSet.getInt("id_tienda"); // Agregado idTienda
                int idCategoria = resultSet.getInt("id_categoria");
                String skuTienda = resultSet.getString("sku_tienda");
                String descTienda = resultSet.getString("desc_tienda");
                String marca = resultSet.getString("marca");
                double precioVenta = resultSet.getDouble("precio_vta");
                boolean estatus = resultSet.getBoolean("estatus");
                String descCategoria = resultSet.getString("desc_categoria");

                BigDecimal precioVentaDecimal = BigDecimal.valueOf(precioVenta); // Convertir a BigDecimal

                String descrCategoria = resultSet.getString("desc_categoria");
                Categoria categoria = new Categoria(); // Crear un nuevo objeto Categoria
                categoria.setDesc_categoria(descrCategoria); // Establecer la descripción de la categoría en el objeto Categoria
                Producto producto = new Producto(idInternoProducto, idTienda, idCategoria, skuTienda, descTienda, marca, precioVentaDecimal, estatus);
                producto.setCategoria(categoria); // Asignar el objeto Categoria al Producto

                productos.add(producto);
            }
            statement.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            // Cerrar los recursos JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conection != null) conection.close();
            } catch (SQLException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return productos;
    }


    //Conexion a SQL
    public Connection conDB(){

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //Conexion AWS
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://16.171.5.184:1433;instance=SQLEXPRESS;databaseName=flagg_test3;user=sa;password=Flagg2024;");
            //Conexion Local
            //conection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return conection;
    }
}