package com.beone.flagggaming.tiendascliente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.tiendas.Tienda;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaTiendas extends Fragment {

    private RecyclerView recyclerView;
    private TiendaAdapter tiendaAdapter;
    private List<Tienda> tiendaList;
    private ProgressBar progressBar;
    private SearchView searchView;
    private AdView adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_tiendas, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.search_view);
        adView = view.findViewById(R.id.adView);

        // Crear y cargar el anuncio
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Cargar las tiendas desde la base de datos
        new LoadTiendasTask().execute();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (tiendaAdapter != null) {
                    if (TextUtils.isEmpty(newText)) {
                        tiendaAdapter.getFilter().filter("");
                    } else {
                        tiendaAdapter.getFilter().filter(newText);
                    }
                }
                return true;
            }
        });
        return view;
    }

    private class LoadTiendasTask extends AsyncTask<Void, Void, List<Tienda>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected List<Tienda> doInBackground(Void... voids) {
            return loadTiendasFromDatabase();
        }

        @Override
        protected void onPostExecute(List<Tienda> tiendas) {
            super.onPostExecute(tiendas);
            progressBar.setVisibility(ProgressBar.GONE);
            tiendaList = tiendas;
            tiendaAdapter = new TiendaAdapter(tiendaList);
            recyclerView.setAdapter(tiendaAdapter);

            tiendaAdapter.setOnItemClickListener(new TiendaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Tienda tienda) {
                    DetalleTiendaActivity detalleTiendaFragment = new DetalleTiendaActivity();

                    // Pasar los datos al fragmento de detalle
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", tienda.getId());
                    bundle.putString("name", tienda.getName());
                    bundle.putString("mail", tienda.getMail());
                    bundle.putString("dir", tienda.getDir());
                    bundle.putString("hr", tienda.getHr());
                    bundle.putString("days", tienda.getDays());
                    bundle.putString("tel", tienda.getTel());
                    bundle.putString("insta", tienda.getInsta());
                    detalleTiendaFragment.setArguments(bundle);

                    // Reemplazar el fragmento actual con el de detalle
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, detalleTiendaFragment) // Asegúrate de que "fragment_container" es el id correcto del contenedor en tu layout
                            .addToBackStack(null) // Añadir a la pila para poder volver atrás
                            .commit();
                }
            });
        }
    }

    private List<Tienda> loadTiendasFromDatabase() {
        List<Tienda> tiendaList = new ArrayList<>();

        try (Connection connection = DBHelper.conDB(getActivity())) {
            String query = "SELECT [id], [name], [mail], [dir], [days], [hr], [insta], [tel] FROM [tiendas]";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String mail = resultSet.getString("mail");
                String dir = resultSet.getString("dir");
                String days = resultSet.getString("days");
                String hr = resultSet.getString("hr");
                String insta = resultSet.getString("insta");
                String tel = resultSet.getString("tel");

                tiendaList.add(new Tienda(id, name, mail, dir, days, hr, insta, tel));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiendaList;
    }
}