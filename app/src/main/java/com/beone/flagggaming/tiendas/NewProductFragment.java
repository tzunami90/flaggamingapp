package com.beone.flagggaming.tiendas;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.producto.Producto;
import com.beone.flagggaming.producto.Categoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewProductFragment extends Fragment {
    private Spinner spinnerCategoria;
    private List<Categoria> categoriasList;
    private ArrayAdapter<Categoria> categoriaArrayAdapter;
    EditText editTextSkuTienda, editTextDescTienda, editTextMarca,editTextPrecioVta;
    Button buttonAgregarProducto;
    CheckBox checkBoxEstatus;
    private boolean estatusProducto = true; // Por defecto, producto activo
    int idU, idT;

    public NewProductFragment(){

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
        View root = inflater.inflate(R.layout.fragment_new_product, container, false);

        spinnerCategoria = root.findViewById(R.id.spinnerCategoria);
        editTextSkuTienda = root.findViewById(R.id.editTextSkuTienda);
        editTextDescTienda = root.findViewById(R.id.editTextDescTienda);
        editTextMarca = root.findViewById(R.id.editTextMarca);
        editTextPrecioVta = root.findViewById(R.id.editTextPrecioVta);
        buttonAgregarProducto = root.findViewById(R.id.buttonAgregarProducto);
        checkBoxEstatus = root.findViewById(R.id.checkBoxEstatus);

        checkBoxEstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Actualizar la variable estatusProducto basada en el estado del CheckBox
                estatusProducto = isChecked; // True si está activo, false si está inactivo
            }
        });

        // Cargar las categorías desde la base de datos
        cargarCategoriasDesdeBaseDeDatos();

        buttonAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearProducto();
            }
        });
        return root;
    }

    private void cargarCategoriasDesdeBaseDeDatos() {
        categoriasList = obtenerCategoriasDesdeBaseDeDatos();
        // Agregar un elemento adicional para "Seleccione una categoría"
        Categoria categoriaSeleccione = new Categoria(0, "Seleccione una categoría");
        categoriasList.add(0, categoriaSeleccione);

        // Configurar el ArrayAdapter para el Spinner
        categoriaArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriasList);
        categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriaArrayAdapter);

        // Configurar "Seleccione una categoría" como el elemento seleccionado por defecto
        spinnerCategoria.setSelection(0);
    }

    // Método simulado para obtener categorías desde la base de datos
    private List<Categoria> obtenerCategoriasDesdeBaseDeDatos() {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection connection = DBHelper.conDB(getContext())){
            if(connection == null){
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            }else {
                String selectQuery = "SELECT id_categoria, desc_categoria FROM categorias";
                PreparedStatement pstSelect = connection.prepareStatement(selectQuery);
                ResultSet resultSet = pstSelect.executeQuery();

                while (resultSet.next()) {
                    int idCategoria = resultSet.getInt("id_categoria");
                    String descCategoria = resultSet.getString("desc_categoria");
                    Categoria categoria = new Categoria(idCategoria, descCategoria);
                    categorias.add(categoria);
                }
                pstSelect.close();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
        }

        return categorias;
    }


    //Método para crear el producto en la BD
    private void crearProducto(){
        // Obtener la cantidad de productos creados por el usuario
        int cantidadProductosCreados = obtenerCantidadProductosCreados();

        // Verificar si el usuario tiene una suscripción premium
        boolean tieneSuscripcionPremium = verificarSuscripcionPremium();

        // Verificar si el usuario puede crear más productos según su tipo de suscripción
        if (!tieneSuscripcionPremium && cantidadProductosCreados >= 10) {
            // El usuario ha alcanzado el límite de productos para la suscripción gratuita
            Toast.makeText(getContext(), "¡Has alcanzado el límite de productos para la suscripción gratuita! Suscríbete al plan premium para crear más productos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String skuTienda = editTextSkuTienda.getText().toString().trim();
        String descTienda = editTextDescTienda.getText().toString().trim();
        String marca = editTextMarca.getText().toString().trim();
        String precioVtaStr = editTextPrecioVta.getText().toString().trim();
        Categoria categoriaSeleccionada = (Categoria) spinnerCategoria.getSelectedItem();

        // Validar que no haya campos vacíos
        if (skuTienda.isEmpty() || descTienda.isEmpty() || marca.isEmpty() || precioVtaStr.isEmpty() || categoriaSeleccionada == null) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el precio de venta
        double precioVta;
        try {
            precioVta = Double.parseDouble(precioVtaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Precio de venta inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el producto en la base de datos
        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Consulta SQL para insertar el producto en la base de datos
                String insertQuery = "INSERT INTO productos (id_tienda, id_categoria, sku_tienda, desc_tienda, marca, precio_vta, estatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstInsert = connection.prepareStatement(insertQuery);
                pstInsert.setInt(1, idT); // ID de la tienda
                pstInsert.setInt(2, categoriaSeleccionada.getId_categoria()); // ID de la categoría seleccionada
                pstInsert.setString(3, skuTienda); // SKU de la tienda
                pstInsert.setString(4, descTienda); // Descripción de la tienda
                pstInsert.setString(5, marca); // Marca
                pstInsert.setDouble(6, precioVta); // Precio de venta
                pstInsert.setBoolean(7, estatusProducto); // Estatus

                // Ejecutar la consulta
                int filasAfectadas = pstInsert.executeUpdate();

                if (filasAfectadas > 0) {
                    Toast.makeText(getContext(), "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                    // Limpiar los campos después de agregar el producto
                    editTextSkuTienda.setText("");
                    editTextDescTienda.setText("");
                    editTextMarca.setText("");
                    editTextPrecioVta.setText("");
                    checkBoxEstatus.setChecked(true); // Restaurar el estado del checkbox
                } else {
                    Toast.makeText(getContext(), "Error al crear el producto", Toast.LENGTH_SHORT).show();
                }

                pstInsert.close();
            }
        }catch (SQLException e) {
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            Log.d("Error", e.getMessage());
            }
    }
    // Método para obtener la cantidad de productos creados por el usuario
    private int obtenerCantidadProductosCreados() {
        int cantidadProductos = 0;
        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            } else {
                // Realizar una consulta SQL para contar los productos creados por el usuario
                String selectQuery = "SELECT COUNT(*) AS cantidad FROM productos WHERE id_tienda = ?";
                PreparedStatement pstSelect = connection.prepareStatement(selectQuery);
                pstSelect.setInt(1, idT);
                ResultSet resultSet = pstSelect.executeQuery();

                if (resultSet.next()) {
                    cantidadProductos = resultSet.getInt("cantidad");
                }

                pstSelect.close();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.getMessage());
        }

        return cantidadProductos;
    }

    // Método para verificar si la tienda tiene una suscripción premium
    private boolean verificarSuscripcionPremium() {
        boolean tieneSuscripcionPremium = false;
        try (Connection connection = DBHelper.conDB(getContext())) {
            if (connection == null) {
                Toast.makeText(getContext(), "ERROR DE CONEXION - Por favor reintente en unos momentos", Toast.LENGTH_SHORT).show();
            } else {
                // Realizar una consulta SQL para obtener el estado de la suscripción premium de la tienda del usuario
                String selectQuery = "SELECT premium FROM tiendas WHERE id = ?";
                PreparedStatement pstSelect = connection.prepareStatement(selectQuery);
                pstSelect.setInt(1, idT);
                ResultSet resultSet = pstSelect.executeQuery();

                if (resultSet.next()) {
                    // Obtener el valor del campo premium de la consulta
                    int premium = resultSet.getInt("premium");
                    tieneSuscripcionPremium = (premium == 1); // Si premium es 1, la tienda tiene suscripción premium
                }

                pstSelect.close();
            }
        } catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Error", e.getMessage());
        }

        return tieneSuscripcionPremium;
    }
}