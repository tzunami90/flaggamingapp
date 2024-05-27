package com.beone.flagggaming.steamapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaJuegosSteam extends AppCompatActivity {

    private TextView titulo;
    private List<Juegos> listJuegos;
    private RecyclerView recyclerView;
    private SteamJuegoAdapter steamJuegoAdapter;
    private Button btnGrabarJuegos, buttonSearch;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_juegos_steam);

        // Obtener referencias a los elementos de la interfaz de usuario
        editTextSearch = findViewById(R.id.editText_search);
        buttonSearch = findViewById(R.id.button_search);
        titulo = findViewById(R.id.titulo);
        recyclerView=findViewById(R.id.rv_juegossteam);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        btnGrabarJuegos = findViewById(R.id.btnGrabarJuegos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listasteam), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        getJuego();

        //Boton para actualizar la BD -> podriam poner para que lo haga en 2do plano y siga trabajando
        btnGrabarJuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabarJuegos();
            }
        });

        // Clic del botón de búsqueda
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchGames(query);
                } else {
                    // Si el campo de búsqueda está vacío, mostrar mensaje de advertencia
                    Toast.makeText(ListaJuegosSteam.this, "Ingrese un nombre para buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getJuego(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.steampowered.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SteamApiService steamApiService = retrofit.create(SteamApiService.class);

        Call<SteamAppList> call = steamApiService.getAppList();
        call.enqueue(new Callback<SteamAppList>() {
            @Override
            public void onResponse(Call<SteamAppList> call, Response<SteamAppList> response) {

                if(!response.isSuccessful()){
                    titulo.setText("Codigo: " + response.code());
                    return;
                }

                SteamAppList steamAppList = response.body();
                if (steamAppList != null) {
                    listJuegos = steamAppList.getAppList().getApps();
                    steamJuegoAdapter = new SteamJuegoAdapter(listJuegos,getApplicationContext());
                    recyclerView.setAdapter(steamJuegoAdapter);
                }

                steamJuegoAdapter = new SteamJuegoAdapter(listJuegos,getApplicationContext());
                recyclerView.setAdapter(steamJuegoAdapter);
            }

            @Override
            public void onFailure(Call<SteamAppList> call, Throwable t) {

                Toast.makeText(ListaJuegosSteam.this, "ERROR CON API STEAM", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void grabarJuegos(){
        DBHelper.saveAppList(this, listJuegos);
    }

    private void searchGames(String query) {
        // Filtrar la lista de juegos según el nombre ingresado en el EditText
        List<Juegos> filteredList = new ArrayList<>();
        for (Juegos juego : listJuegos) {
            if (juego.getAppName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(juego);
            }
        }

        // Actualizar el RecyclerView con los resultados de la búsqueda
        steamJuegoAdapter.setJuegos(filteredList);
        steamJuegoAdapter.notifyDataSetChanged();
    }

}