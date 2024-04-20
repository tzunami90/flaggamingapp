package com.beone.flagggaming.steamapi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beone.flagggaming.steamapi.details.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.beone.flagggaming.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarJuegoSteam extends AppCompatActivity {

    private ImageView imageViewCapsule;
    private TextView textViewName;
    private TextView textViewShortDescription;
    private TextView textViewId;
    private TextView textViewPcRequirements;
    private TextView textViewMacRequirements;
    private TextView textViewLinuxRequirements;
    private TextView textViewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mostrar_juego_steam);

        // Obtener referencias de los elementos de la interfaz de usuario
         imageViewCapsule = findViewById(R.id.imageView_capsule);
         textViewName = findViewById(R.id.textView_name);
         textViewShortDescription = findViewById(R.id.textView_short_description);
         textViewId = findViewById(R.id.textView_id);
         textViewPcRequirements = findViewById(R.id.textView_pc_requirements);
         textViewMacRequirements = findViewById(R.id.textView_mac_requirements);
         textViewLinuxRequirements = findViewById(R.id.textView_linux_requirements);
         textViewPrice = findViewById(R.id.textView_price);

        // Obtener el ID del juego seleccionado del intent
        String id = getIntent().getStringExtra("id");

        Log.d("ID del juego", "ID: " + id);
        // Llama al método para obtener los detalles del juego
        obtenerDetallesJuego(id);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void obtenerDetallesJuego(String id) {
        Log.d("DetallesJuego", "Iniciando solicitud para el juego con ID: " + id);

        // Obtener el servicio de la API de Steam
        SteamDetailApiService apiService = SteamDetailApiAdapter.getApiService();
        // Realizar la solicitud de detalles del juego
        Call<AppDetailsResponse> call = apiService.getAppDetails(id, "ar");
        call.enqueue(new Callback<AppDetailsResponse>() {
            @Override
            public void onResponse(Call<AppDetailsResponse> call, Response<AppDetailsResponse> response) {
                if (response.isSuccessful()) {
                    // Registro del código de respuesta
                    Log.d("Response", "Response code: " + response.code());

                    // Registro del cuerpo de la respuesta
                    if (response.body() != null) {
                        Log.d("Response", "Response body: " + response.body().toString());
                    }

                    // Obtener los detalles del juego desde la respuesta
                    AppDetailsResponse appDetailsResponse = response.body();

                    // Verificar si la respuesta contiene datos y si el mapa de datos no es nulo
                    if (appDetailsResponse != null) {
                        // Obtener el mapa de datos completo
                        Data data = appDetailsResponse.getData();
                        // Verificar si el mapa de datos contiene la información del juego
                        if (data != null) {
                            // Verificar si los detalles del juego no son nulos
                            if (data != null) {
                                // Establecer los datos en las vistas correspondientes
                                textViewName.setText(data.getName());
                                textViewShortDescription.setText(data.getShortDescription());
                                textViewPcRequirements.setText("Requisitos de PC:\nMínimos: " + data.getPcRequirements().getMinimum() + "\nRecomendados: " + data.getPcRequirements().getRecommended());
                                // Verificar si hay requisitos para Mac
                                if (data.getMacRequirements() != null) {
                                    textViewMacRequirements.setText("Requisitos de Mac:\nMínimos: " + data.getMacRequirements().getMinimum() + "\nRecomendados: " + data.getMacRequirements().getRecommended());
                                } else {
                                    textViewMacRequirements.setText("Requisitos de Mac: No especificado");
                                }
                                // Verificar si hay requisitos para Linux
                                if (data.getLinuxRequirements() != null) {
                                    textViewLinuxRequirements.setText("Requisitos de Linux:\nMínimos: " + data.getLinuxRequirements().getMinimum() + "\nRecomendados: " + data.getLinuxRequirements().getRecommended());
                                } else {
                                    textViewLinuxRequirements.setText("Requisitos de Linux: No especificado");
                                }
                                // Verificar si el juego es gratuito
                                if (data.isIsFree()) {
                                    textViewPrice.setText("Precio: GRATIS");
                                } else {
                                    // Verificar si se proporciona información de precio
                                    if (data.getPriceOverview() != null) {
                                        textViewPrice.setText("Precio: " + data.getPriceOverview().getFinalFormatted());
                                    } else {
                                        // Si no se proporciona información de precio, establecer precio como 0
                                        textViewPrice.setText("Precio: 0");
                                    }
                                }
                                // Mostrar el ID del juego en el textViewId
                                textViewId.setText("ID: " + id);
                                // Cargar la imagen del juego usando Glide
                                Glide.with(MostrarJuegoSteam.this)
                                        .load(data.getHeaderImage())
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                Log.e("Glide", "Error al cargar la imagen", e);
                                                // Aquí puedes realizar acciones adicionales en caso de que falle la carga de la imagen
                                                return false;
                                            }
                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                // La imagen se ha cargado correctamente
                                                return false;
                                            }
                                        })
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .placeholder(R.drawable.placeholder_image)
                                        .error(R.drawable.placeholder_image)
                                        .into(imageViewCapsule);
                            } else {
                                // Los detalles del juego son nulos
                                Toast.makeText(MostrarJuegoSteam.this, "Los detalles del juego son nulos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // La respuesta no contiene información del juego
                            Toast.makeText(MostrarJuegoSteam.this, "La respuesta no contiene información del juego", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // La respuesta no contiene datos o el mapa de datos es nulo
                        Toast.makeText(MostrarJuegoSteam.this, "La respuesta no contiene datos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // La solicitud no fue exitosa
                    Toast.makeText(MostrarJuegoSteam.this, "La solicitud no fue exitosa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppDetailsResponse> call, Throwable t) {
                // Manejar el fallo de la solicitud
                Log.e("Response", "Error de red", t);
                Toast.makeText(MostrarJuegoSteam.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}