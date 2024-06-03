package com.beone.flagggaming.tiendascliente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.tiendas.Tienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaTiendas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TiendaAdapter tiendaAdapter;
    private List<Tienda> tiendaList;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tiendas);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                    Intent intent = new Intent(ListaTiendas.this, DetalleTiendaActivity.class);
                    intent.putExtra("id", tienda.getId());
                    intent.putExtra("name", tienda.getName());
                    intent.putExtra("mail", tienda.getMail());
                    intent.putExtra("dir", tienda.getDir());
                    intent.putExtra("hr", tienda.getHr());
                    intent.putExtra("days", tienda.getDays());
                    intent.putExtra("tel", tienda.getTel());
                    intent.putExtra("insta", tienda.getInsta());
                    intent.putExtra("id", tienda.getId());
                    startActivity(intent);
                }
            });
        }
    }

    private List<Tienda> loadTiendasFromDatabase() {
        List<Tienda> tiendaList = new ArrayList<>();

        try (Connection connection = DBHelper.conDB(this)) {
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