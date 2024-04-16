package com.beone.flagggaming.tiendas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.beone.flagggaming.R;
import com.beone.flagggaming.producto.Categoria;

import java.util.ArrayList;
import java.util.List;

public class NewProductFragment extends Fragment {
    private Spinner spinnerCategoria;
    private List<Categoria> categoriasList;
    private ArrayAdapter<Categoria> categoriaArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_product, container, false);

        spinnerCategoria = root.findViewById(R.id.spinnerCategoria);

        // Configurar el ArrayAdapter para el Spinner
        categoriaArrayAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, categoriasList);
        categoriaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriaArrayAdapter);

        // Cargar las categorías desde la base de datos
        cargarCategoriasDesdeBaseDeDatos();

        return root;
    }

    private void cargarCategoriasDesdeBaseDeDatos() {
        // Aquí deberías implementar la lógica para recuperar las categorías de tu base de datos
        // Por ejemplo, podrías usar una consulta SQL para obtener las categorías y luego agregarlas a la lista categoriasList
        // Supongamos que tienes un método obtenerCategoriasDesdeBaseDeDatos() que devuelve una lista de objetos Categoria
        // Implementa este método según tu lógica de base de datos
        categoriasList = obtenerCategoriasDesdeBaseDeDatos();

        // Notificar al ArrayAdapter que los datos han cambiado
        categoriaArrayAdapter.clear();
        categoriaArrayAdapter.addAll(categoriasList);
        categoriaArrayAdapter.notifyDataSetChanged();
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
}