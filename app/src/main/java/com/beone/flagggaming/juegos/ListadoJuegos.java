package com.beone.flagggaming.juegos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;


public class ListadoJuegos extends Fragment implements JuegoAdapter.OnJuegoClickListener{

    private RecyclerView recyclerView;
    private JuegoAdapter juegosAdapter;
    private SearchView searchView;
    private ProgressBar progressBar;
    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listado_juegos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewJuegos);
        searchView = view.findViewById(R.id.searchViewJuegos);
        progressBar = view.findViewById(R.id.progressBarJ);
        adView = view.findViewById(R.id.adView);

        // Configurar el AdMob
        MobileAds.initialize(getContext(), initializationStatus -> {});

        // Crear y cargar el anuncio
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadJuegos();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (juegosAdapter != null) {
                    juegosAdapter.filter(newText);
                }
                return true;
            }
        });

        return view;
    }

    public void onJuegoClick(String idFlagg) {
        // Incrementar el contador de vistas en un hilo secundario
        new Thread(() -> DBHelper.incrementarContadorVistas(getContext(), idFlagg)).start();

        // Navegar al fragmento de detalles del juego
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

    private void loadJuegos() {
        progressBar.setVisibility(View.VISIBLE);

        // Simulando la obtención de datos desde la base de datos
        new Thread(() -> {
            List<Juego> juegosList = DBHelper.getJuegos(getContext());
            getActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                juegosAdapter = new JuegoAdapter(getContext(), juegosList, ListadoJuegos.this); // Usar ListadoJuegos.this para pasar la instancia correcta
                recyclerView.setAdapter(juegosAdapter);
            });
        }).start();
    }
}