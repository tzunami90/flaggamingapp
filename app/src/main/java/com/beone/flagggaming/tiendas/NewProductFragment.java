package com.beone.flagggaming.tiendas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.beone.flagggaming.producto.Categoria;

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
        // Aquí deberías implementar la lógica para recuperar las categorías de tu base de datos
        // Por ejemplo, podrías usar una consulta SQL para obtener las categorías y luego agregarlas a la lista categoriasList
        // Supongamos que tienes un método obtenerCategoriasDesdeBaseDeDatos() que devuelve una lista de objetos Categoria
        // Implementa este método según tu lógica de base de datos
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
        // Aquí deberías implementar la lógica real para recuperar las categorías de tu base de datos
        // Por ahora, simularemos una lista estática de categorías
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(1, "Placa Video"));
        categorias.add(new Categoria(2, "Procesador"));
        categorias.add(new Categoria(3, "Memoria RAM"));
        return categorias;
    }

    //Método para crear el producto en la BD
    private void crearProducto(){
        int estatusEnBaseDeDatos = estatusProducto ? 1 : 0; // Convertir el booleano a 1 o 0
        Toast.makeText(getContext(),"Producto Creado Exitosamente",Toast.LENGTH_SHORT).show();

    }
}