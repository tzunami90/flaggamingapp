package com.beone.flagggaming.steamapi;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_juegos_steam);

        titulo = findViewById(R.id.titulo);
        recyclerView=findViewById(R.id.rv_juegossteam);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listasteam), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        getJuego();
    }

    private void getJuego(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.steampowered.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SteamApiService steamApiService = retrofit.create(SteamApiService.class);

        Call<List<Juegos>> call = steamApiService.getJuego();

        
        call.enqueue(new Callback<List<Juegos>>() {
            @Override
            public void onResponse(Call<List<Juegos>> call, Response<List<Juegos>> response) {

                if(!response.isSuccessful()){
                    titulo.setText("Codigo: " + response.code());
                    return;
                }

                listJuegos = response.body();
                steamJuegoAdapter = new SteamJuegoAdapter(listJuegos,getApplicationContext());
                recyclerView.setAdapter(steamJuegoAdapter);
            }

            @Override
            public void onFailure(Call<List<Juegos>> call, Throwable t) {

                Toast.makeText(ListaJuegosSteam.this, "ERROR DE CONEXIÓN CON API STEAM", Toast.LENGTH_SHORT).show();

            }
        });


    }

}