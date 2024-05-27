package com.beone.flagggaming.tiendas;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.producto.Categoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallesProductoFragment extends Fragment {
    Connection conection = null;
    private int idU, idP, idT;
    EditText editTextModifSkuTienda, editTextModifDescTienda, editTextModifMarca, editTextModifPrecioVta;
    Spinner spinnerModifCategoria;
    CheckBox checkBoxModifEstatus;
    Button buttonModifProducto, buttonElimProducto;
    private List<Categoria> categoriasList;
    private ArrayAdapter<Categoria> categoriaArrayAdapter;
    View root;
    private ProgressBar progressBar;
    private String errorMessage;
    public DetallesProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idU = getArguments().getInt("idU");
        idT = getArguments().getInt("idT");
        idP = getArguments().getInt("idP");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_detalles_producto, container, false);
        // Enlazar vistas
        editTextModifSkuTienda = root.findViewById(R.id.editTextModifSkuTienda);
        editTextModifDescTienda = root.findViewById(R.id.editTextModifDescTienda);
        editTextModifMarca = root.findViewById(R.id.editTextModifMarca);
        editTextModifPrecioVta = root.findViewById(R.id.editTextModifPrecioVta);
        spinnerModifCategoria = root.findViewById(R.id.spinnerModifCategoria);
        checkBoxModifEstatus = root.findViewById(R.id.checkBoxModifEstatus);
        buttonModifProducto = root.findViewById(R.id.buttonModifProducto);
        buttonElimProducto = root.findViewById(R.id.buttonElimProducto);
        progressBar = root.findViewById(R.id.progressBarDetallesProducto);

        // Mostrar ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Inicializar categoriasList
        categoriasList = new ArrayList<>();

        // Realizar la carga de datos en segundo plano
        new LoadDataAsyncTask().execute();

        // Configurar clics de botones
        buttonModifProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para modificar el producto
                modificarProducto();
            }
        });

        buttonElimProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para eliminar el producto
                eliminarProducto();
            }
        });

        return root;
    }

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private List<Categoria> categoriasList; // Aquí almacenaremos las categorías cargadas

        @Override
        protected Void doInBackground(Void... voids) {
            categoriasList = new ArrayList<>(); // Inicializamos la lista de categorías aquí

            try {
                // Establecer conexión a la base de datos
                conection = conDB();

                // Consulta SQL para obtener las categorías
                String query = "SELECT id_categoria, desc_categoria FROM categorias";
                PreparedStatement statement = conection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Recorrer los resultados y agregar las categorías a la lista
                while (resultSet.next()) {
                    int idCategoria = resultSet.getInt("id_categoria");
                    String descCategoria = resultSet.getString("desc_categoria");
                    Categoria categoria = new Categoria(idCategoria, descCategoria);
                    categoriasList.add(categoria);
                }

                // Cerrar conexión y recursos
                resultSet.close();
                statement.close();
                conection.close();
            } catch (SQLException e) {
                errorMessage = "Error al cargar las categorías: " + e.getMessage();
                Log.e("Error", "Error al cargar las categorías", e);
            }

            // Llamar al método para cargar los datos del producto
            cargarDatosProducto();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Ocultar ProgressBar después de cargar los datos
            progressBar.setVisibility(View.GONE);

            if (categoriasList != null && !categoriasList.isEmpty()) {
                // Configurar el ArrayAdapter para el Spinner
                categoriaArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriasList);
                categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModifCategoria.setAdapter(categoriaArrayAdapter);

                // Configurar "Seleccione una categoría" como el elemento seleccionado por defecto
                spinnerModifCategoria.setSelection(0);
            } else {
                Toast.makeText(getContext(), "No se encontraron categorías", Toast.LENGTH_SHORT).show();
                errorMessage = "No se encontraron categorías";
                Log.e("Error", "No se encontraron categorías");
            }
        }
    }

    private void cargarDatosProducto() {
        try {
            // Establecer conexión a la base de datos
            conection = conDB();

            // Consulta SQL para obtener los detalles del producto
            String query = "SELECT sku_tienda, desc_tienda, marca, precio_vta, estatus, id_categoria FROM productos WHERE id_interno_producto = ? AND id_tienda = ?";
            PreparedStatement statement = conection.prepareStatement(query);
            statement.setInt(1, idP);
            statement.setInt(2, idT);
            ResultSet resultSet = statement.executeQuery();

            // Verificar si se encontró el producto
            if (resultSet.next()) {
                // Obtener datos del producto
                String sku = resultSet.getString("sku_tienda");
                String descripcion = resultSet.getString("desc_tienda");
                String marca = resultSet.getString("marca");
                double precio = resultSet.getDouble("precio_vta");
                boolean estatus = resultSet.getBoolean("estatus");
                int idCategoria = resultSet.getInt("id_categoria");

                // Mostrar los datos en las vistas correspondientes
                editTextModifSkuTienda.setText(sku);
                editTextModifDescTienda.setText(descripcion);
                editTextModifMarca.setText(marca);
                editTextModifPrecioVta.setText(String.valueOf(precio));
                checkBoxModifEstatus.setChecked(estatus);

                // Buscar la categoría correspondiente en la lista de categorías
                for (int i = 0; i < categoriasList.size(); i++) {
                    if (categoriasList.get(i).getId_categoria() == idCategoria) {
                        // Establecer la categoría correspondiente como la opción seleccionada en el Spinner
                        spinnerModifCategoria.setSelection(i);
                        break;
                    }
                }
            } else {
                Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión y recursos
            resultSet.close();
            statement.close();
            conection.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }
    }

    private void modificarProducto() {
        // Obtener nuevos valores de las vistas
        String sku = editTextModifSkuTienda.getText().toString();
        String descripcion = editTextModifDescTienda.getText().toString();
        String marca = editTextModifMarca.getText().toString();
        String precio = editTextModifPrecioVta.getText().toString().trim();
        boolean estatus = checkBoxModifEstatus.isChecked();
        Categoria categoriaSeleccionada = (Categoria) spinnerModifCategoria.getSelectedItem();

        // Validar que no haya campos vacíos
        if (sku.isEmpty() || descripcion.isEmpty() || marca.isEmpty() || precio.isEmpty() || categoriaSeleccionada == null) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el precio de venta
        double precioVta;
        try {
            precioVta = Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio de venta inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Establecer conexión a la base de datos
            conection = conDB();

            // Consulta SQL para actualizar el producto
            String query = "UPDATE productos SET sku_tienda = ?, desc_tienda = ?, marca = ?, precio_vta = ?, estatus = ?, id_categoria = ? WHERE id_interno_producto = ? AND id_tienda = ?";
            PreparedStatement statementUpdate = conection.prepareStatement(query);
            statementUpdate.setString(1, sku);
            statementUpdate.setString(2, descripcion);
            statementUpdate.setString(3, marca);
            statementUpdate.setDouble(4, precioVta);
            statementUpdate.setBoolean(5, estatus);
            statementUpdate.setInt(6, categoriaSeleccionada.getId_categoria());
            statementUpdate.setInt(7, idP);
            statementUpdate.setInt(8, idT);

            // Ejecutar la consulta
            int rowsUpdated = statementUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No se pudo actualizar el producto", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión y recursos
            statementUpdate.close();
            conection.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            // Establecer conexión a la base de datos
            conection = conDB();

            // Consulta SQL para eliminar el producto
            String query = "DELETE FROM productos WHERE id_interno_producto = ? AND id_tienda = ?";
            PreparedStatement statementDelete = conection.prepareStatement(query);
            statementDelete.setInt(1, idP);
            statementDelete.setInt(2, idT);

            // Ejecutar la consulta
            int rowsDeleted = statementDelete.executeUpdate();
            if (rowsDeleted > 0) {
                Toast.makeText(getContext(), "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                Bundle bundleMP = new Bundle();
                bundleMP.putInt("idU",idU);
                bundleMP.putInt("idT",idT);
                MisProductosFragment misProductosFragment = new MisProductosFragment();
                misProductosFragment.setArguments(bundleMP);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,misProductosFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión y recursos
            statementDelete.close();
            conection.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Error al eliminar el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void cargarCategoriasDesdeBaseDeDatos() {
        // Elimina la inicialización redundante de categoriasList aquí
        // No es necesario inicializarla de nuevo

        try {
            // Establecer conexión a la base de datos
            conection = conDB();

            // Consulta SQL para obtener las categorías
            String query = "SELECT id_categoria, desc_categoria FROM categorias";
            PreparedStatement statement = conection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Recorrer los resultados y agregar las categorías a la lista
            while (resultSet.next()) {
                int idCategoria = resultSet.getInt("id_categoria");
                String descCategoria = resultSet.getString("desc_categoria");
                Categoria categoria = new Categoria(idCategoria, descCategoria);
                categoriasList.add(categoria);
            }

            // Cerrar conexión y recursos
            resultSet.close();
            statement.close();
            conection.close();
        } catch (SQLException e) {
            errorMessage = "Error al cargar las categorías: " + e.getMessage();
            Log.e("Error", "Error al cargar las categorías", e);
        }
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
        }
        return conection;
    }
}