package com.beone.flagggaming.navbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.juegos.DetalleJuego;
import com.beone.flagggaming.juegos.Juego;
import com.beone.flagggaming.juegos.HomeJuegoAdapter; // Asegúrate de importar el nuevo adaptador
import com.beone.flagggaming.juegos.JuegoAdapter;

import java.util.List;
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewOfertas;
    private RecyclerView recyclerViewDestacados;
    private HomeJuegoAdapter juegoAdapterOfertas;
    private HomeJuegoAdapter juegoAdapterDestacados;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar vistas
        recyclerViewOfertas = view.findViewById(R.id.recyclerViewOfertas);
        recyclerViewDestacados = view.findViewById(R.id.recyclerViewDestacados);
        progressBar = view.findViewById(R.id.progressBar);

        // Configurar los RecyclerViews
        recyclerViewOfertas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDestacados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Mostrar el ProgressBar y cargar los datos en segundo plano
        progressBar.setVisibility(View.VISIBLE);
        new Thread(this::cargarDatos).start(); // Llamar al método cargarDatos en un hilo separado

        return view;
    }

    private void cargarDatos() {
        // Cargar los juegos desde la base de datos
        List<Juego> ofertasDestacadas = DBHelper.getOfertasDestacadas(getContext());
        List<Juego> juegosDestacados = DBHelper.getJuegosDestacados(getContext());

        Log.d("HomeFragment", "Juegos ofertas obtenidos: " + ofertasDestacadas.size());
        Log.d("HomeFragment", "Juegos destacados obtenidos: " + juegosDestacados.size());

        // Volver al hilo principal para actualizar la UI
        getActivity().runOnUiThread(() -> {
            // Ocultar el ProgressBar
            progressBar.setVisibility(View.GONE);

            // Actualizar los adaptadores con los datos cargados
            juegoAdapterOfertas = new HomeJuegoAdapter(getContext(), ofertasDestacadas, this::onJuegoClick);
            recyclerViewOfertas.setAdapter(juegoAdapterOfertas);

            juegoAdapterDestacados = new HomeJuegoAdapter(getContext(), juegosDestacados, this::onJuegoClick);
            recyclerViewDestacados.setAdapter(juegoAdapterDestacados);
        });
    }

    // Manejar el clic en un juego
    public void onJuegoClick(String idFlagg) {
        new Thread(() -> DBHelper.incrementarContadorVistas(getContext(), idFlagg)).start();

        Bundle bundle = new Bundle();
        bundle.putString("idFlagg", idFlagg);

        DetalleJuego detalleJuegoFragment = new DetalleJuego();
        detalleJuegoFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detalleJuegoFragment)
                .addToBackStack(null)
                .commit();
    }
}




