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
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.producto.Categoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallesProductoFragment extends Fragment {
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

        private List<Categoria> categoriasListTemp = new ArrayList<>();
        private String sku, descripcion, marca;
        private double precio;
        private boolean estatus;
        private int idCategoria;


        @Override
        protected Void doInBackground(Void... voids) {
            cargarCategoriasDesdeBaseDeDatos(categoriasListTemp);
            cargarDatosProducto();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Ocultar ProgressBar después de cargar los datos
            progressBar.setVisibility(View.GONE);

            if (categoriasListTemp != null && !categoriasListTemp.isEmpty()) {
                // Configurar el ArrayAdapter para el Spinner
                categoriaArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriasListTemp);
                categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerModifCategoria.setAdapter(categoriaArrayAdapter);

                // Configurar "Seleccione una categoría" como el elemento seleccionado por defecto
                spinnerModifCategoria.setSelection(0);
            } else {
                Toast.makeText(getContext(), "No se encontraron categorías", Toast.LENGTH_SHORT).show();
                errorMessage = "No se encontraron categorías";
                Log.e("Error", "No se encontraron categorías");
            }

            // Mostrar los datos del producto en las vistas correspondientes
            if (sku != null && descripcion != null) {
                editTextModifSkuTienda.setText(sku);
                editTextModifDescTienda.setText(descripcion);
                editTextModifMarca.setText(marca);
                editTextModifPrecioVta.setText(String.valueOf(precio));
                checkBoxModifEstatus.setChecked(estatus);

                // Buscar la categoría correspondiente en la lista de categorías
                for (int i = 0; i < categoriasListTemp.size(); i++) {
                    if (categoriasListTemp.get(i).getId_categoria() == idCategoria) {
                        // Establecer la categoría correspondiente como la opción seleccionada en el Spinner
                        spinnerModifCategoria.setSelection(i);
                        break;
                    }
                }
            }
        }
        private void cargarCategoriasDesdeBaseDeDatos(List<Categoria> categoriasListTemp) {
            try (Connection connection = DBHelper.conDB(getContext())) {
                if (connection == null) {
                    errorMessage = "Error al conectar con la base de datos.";
                    Log.e("Error", errorMessage);
                    return;
                }

                // Consulta SQL para obtener las categorías
                String query = "SELECT id_categoria, desc_categoria, imagen_url FROM categorias";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Recorrer los resultados y agregar las categorías a la lista
                while (resultSet.next()) {
                    int idCategoria = resultSet.getInt("id_categoria");
                    String descCategoria = resultSet.getString("desc_categoria");
                    String imagenUrl = resultSet.getString("imagen_url");
                    Categoria categoria = new Categoria(idCategoria, descCategoria, imagenUrl);
                    categoriasListTemp.add(categoria);
                }

                // Cerrar recursos
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                errorMessage = "Error al cargar las categorías: " + e.getMessage();
                Log.e("Error", "Error al cargar las categorías", e);
            }
        }
        private void cargarDatosProducto() {
            try (Connection connection = DBHelper.conDB(getContext())) {
                if (connection == null) {
                    errorMessage = "Error al conectar con la base de datos.";
                    Log.e("Error", errorMessage);
                    return;
                }

                // Consulta SQL para obtener los detalles del producto
                String query = "SELECT sku_tienda, desc_tienda, marca, precio_vta, estatus, id_categoria FROM productos WHERE id_interno_producto = ? AND id_tienda = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, idP);
                statement.setInt(2, idT);
                ResultSet resultSet = statement.executeQuery();

                // Verificar si se encontró el producto
                if (resultSet.next()) {
                    // Obtener datos del producto
                    sku = resultSet.getString("sku_tienda");
                    descripcion = resultSet.getString("desc_tienda");
                    marca = resultSet.getString("marca");
                    precio = resultSet.getDouble("precio_vta");
                    estatus = resultSet.getBoolean("estatus");
                    idCategoria = resultSet.getInt("id_categoria");
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // Cerrar conexión y recursos
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Error", e.getMessage());
            }
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

        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                errorMessage = "Error al conectar con la base de datos.";
                Log.e("Error", errorMessage);
                return;
            }

            // Consulta SQL para actualizar el producto
            String query = "UPDATE productos SET sku_tienda = ?, desc_tienda = ?, marca = ?, precio_vta = ?, estatus = ?, id_categoria = ? WHERE id_interno_producto = ? AND id_tienda = ?";
            PreparedStatement statementUpdate = connection.prepareStatement(query);
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
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }
    }

    private void eliminarProducto() {
        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                errorMessage = "Error al conectar con la base de datos.";
                Log.e("Error", errorMessage);
                return;
            }

            // Consulta SQL para eliminar el producto
            String query = "DELETE FROM productos WHERE id_interno_producto = ? AND id_tienda = ?";
            PreparedStatement statementDelete = connection.prepareStatement(query);
            statementDelete.setInt(1, idP);
            statementDelete.setInt(2, idT);

            // Ejecutar la consulta
            int rowsDeleted = statementDelete.executeUpdate();
            if (rowsDeleted > 0) {
                Toast.makeText(getContext(), "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                Bundle bundleMP = new Bundle();
                bundleMP.putInt("idU", idU);
                bundleMP.putInt("idT", idT);
                MisProductosFragment misProductosFragment = new MisProductosFragment();
                misProductosFragment.setArguments(bundleMP);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, misProductosFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
            }

            // Cerrar conexión y recursos
            statementDelete.close();
        } catch (SQLException e) {
            Toast.makeText(getContext(), "Error al eliminar el producto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}