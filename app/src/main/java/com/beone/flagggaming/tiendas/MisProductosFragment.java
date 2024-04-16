package com.beone.flagggaming.tiendas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beone.flagggaming.R;
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
        View root = inflater.inflate(R.layout.fragment_mis_productos, container, false);

        // Configura el RecyclerView
        RecyclerView recyclerViewProductos = root.findViewById(R.id.recyclerViewProducts);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crea y establece el adaptador para el RecyclerView
        List<Producto> productosList = obtenerProductosDesdeBaseDeDatos();
        ProductoAdapter productosAdapter = new ProductoAdapter(productosList);
        recyclerViewProductos.setAdapter(productosAdapter);

        return root;
    }

    private List<Producto> obtenerProductosDesdeBaseDeDatos() {
        List<Producto> productos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Obtener la conexión a la base de datos
            connection = conDB();

            // Consulta SQL para seleccionar productos
            String query = "SELECT id_interno_producto, id_tienda, id_categoria, sku_tienda, desc_tienda, marca, precio_vta, estatus FROM productos WHERE id_tienda = ?";
            statement = connection.prepareStatement(query);
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

                BigDecimal precioVentaDecimal = BigDecimal.valueOf(precioVenta); // Convertir a BigDecimal

                Producto producto = new Producto(idInternoProducto, idTienda, idCategoria, skuTienda, descTienda, marca, precioVentaDecimal, estatus);
                productos.add(producto);
            }
            statement.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        } finally {
            // Cerrar la conexión y los recursos JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
                Log.d("Error", e.getMessage());
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
            conection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.2.2:1433;instance=SQLEXPRESS;databaseName=flagg_test2;user=sa;password=Alexx2003;");
        } catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conection;
    }
}