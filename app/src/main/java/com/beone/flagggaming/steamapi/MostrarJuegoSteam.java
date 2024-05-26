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

import com.beone.flagggaming.dolarapi.DolarAPIManager;
import com.beone.flagggaming.dolarapi.DolarApiResponse;
import com.beone.flagggaming.steamapi.details.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.beone.flagggaming.R;
import com.google.gson.Gson;
import java.text.DecimalFormat;

import java.util.Map;

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
    private TextView textView_priceARS;
    private DolarAPIManager dolarAPIManager;
    double dolarCompra, dolarVenta;

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
         textViewPrice = findViewById(R.id.textView_price);
        textView_priceARS = findViewById(R.id.textView_priceARS);

        // Obtener el ID del juego seleccionado del intent
        String id = getIntent().getStringExtra("id");

        Log.d("ID del juego", "ID: " + id);
        // Llama al método para obtener los detalles del juego
        obtenerDetallesJuego(id);


        dolarAPIManager = new DolarAPIManager();

        fetchDataFromAPI();

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
        Call<Map<String, AppDetailsResponse.GameDetails>> call = apiService.getAppDetails(id, "ar");

        call.enqueue(new Callback<Map<String, AppDetailsResponse.GameDetails>>() {
            @Override
            public void onResponse(Call<Map<String, AppDetailsResponse.GameDetails>> call, Response<Map<String, AppDetailsResponse.GameDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, AppDetailsResponse.GameDetails> appDetailsResponse = response.body();
                    Log.d("Response Body", response.body().toString());

                    // Obtener los detalles del juego y mostrarlos en la interfaz de usuario
                    mostrarDetallesJuego(appDetailsResponse, id);
                } else {
                    // Mostrar mensaje de error si la solicitud no fue exitosa
                    mostrarMensaje("La solicitud no fue exitosa. Código de respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, AppDetailsResponse.GameDetails>> call, Throwable t) {
                // Manejar el fallo de la solicitud
                Log.e("Response", "Error de red", t);
                mostrarMensaje("Error de red: " + t.getMessage());
            }
        });
    }



    private void mostrarDetallesJuego(Map<String, AppDetailsResponse.GameDetails> appDetailsResponse, String id) {
        if (appDetailsResponse == null) {
            mostrarMensaje("La respuesta del servidor es nula. No se pudieron obtener los detalles del juego.");
            return;
        }

        // Obtener los detalles específicos del juego
        AppDetailsResponse.GameDetails gameDetails = appDetailsResponse.get(id);
        if (gameDetails == null || !gameDetails.isSuccess()) {
            mostrarMensaje("La solicitud para el juego con ID: " + id + " no fue exitosa. Los detalles del juego no están disponibles.");
            return;
        }
        Log.d("gameDetails", gameDetails.toString());

        // Obtener los datos del juego
        Data data = gameDetails.getData();
        if (data == null) {
            mostrarMensaje("Los datos del juego son nulos para el ID: " + id);
            return;
        }

        // Mostrar los detalles del juego en la interfaz de usuario
        runOnUiThread(() -> {
            textViewId.setText("ID: " + id);
            textViewName.setText(data.getName());
            textViewShortDescription.setText(data.getShortDescription() != null ? data.getShortDescription() : "No disponible");
            textViewPcRequirements.setText(data.getPcRequirements() != null ? "Requisitos: \n" + data.getPcRequirements().getMinimum() + "\n" +
                    data.getPcRequirements().getRecommended() : "No disponible");
            if (data.isIsFree()) {
                textViewPrice.setText("Precio: GRATIS");
            } else {
                textViewPrice.setText("Precio: " + (data.getPriceOverview() != null ? data.getPriceOverview().getFinalFormatted() : "No disponible"));
                int precioFinalInt = data.getPriceOverview().getFinalPrice();
                double precioFinalDouble = precioFinalInt / 100.0;
                double precioFinal = precioFinalDouble * dolarVenta;
                DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
                String precioFinalFormateado = decimalFormat.format(precioFinal);
                textView_priceARS.setText("Precio Posta: " + precioFinalFormateado + " ARS");
            }
            cargarImagenJuego(data.getHeaderImage());
        });
    }




    private void mostrarMensaje(String mensaje){
        Log.d("MostrarJuegoSteam", mensaje);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void cargarImagenJuego(String url){
        Log.d("MostrarJuegoSteam", "Cargando imagen del juego desde: " + url);
        Glide.with(MostrarJuegoSteam.this)
                .load(url)
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
    }

    private void fetchDataFromAPI() {
        dolarAPIManager.getDolarData(new DolarAPIManager.DolarDataListener() {
            @Override
            public void onDolarDataReceived(DolarApiResponse dolarData) {
                dolarCompra = dolarData.getCompra();
                dolarVenta = dolarData.getVenta();
            }

            @Override
            public void onDolarDataError(String errorMessage) {
                mostrarMensaje(errorMessage);
            }
        });
    }
}