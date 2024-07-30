package com.beone.flagggaming.juegos;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;

import java.util.List;


public class ListadoJuegos extends Fragment {

    private RecyclerView recyclerView;
    private JuegoAdapter juegosAdapter;
    private List<Juego> juegosList;
    private SearchView searchView;
    private ProgressBar progressBar;

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

    private void loadJuegos() {
        progressBar.setVisibility(View.VISIBLE);

        // Simulando la obtención de datos desde la base de datos
        new Thread(() -> {
            List<Juego> juegosList = DBHelper.getJuegos(getContext());
            getActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                juegosAdapter = new JuegoAdapter(getContext(), juegosList);
                recyclerView.setAdapter(juegosAdapter);
            });
        }).start();
    }
}