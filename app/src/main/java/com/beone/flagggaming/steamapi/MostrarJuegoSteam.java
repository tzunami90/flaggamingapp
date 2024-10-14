package com.beone.flagggaming.steamapi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

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
import com.beone.flagggaming.ipcapi.ApiResponse;
import com.beone.flagggaming.ipcapi.ApiService;
import com.beone.flagggaming.ipcapi.IPCApiManager;
import com.beone.flagggaming.steamapi.details.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.beone.flagggaming.R;

import java.text.DecimalFormat;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MostrarJuegoSteam extends AppCompatActivity {

    private ImageView imageViewCapsule;
    private TextView textViewName;
    private TextView textViewShortDescription;
    private TextView textViewId;
    private TextView textViewPcRequirements;
    private TextView textViewPrice;
    private TextView textView_priceARS;
    private TextView textView_price3m;
    private DolarAPIManager dolarAPIManager;
    double dolarCompra, dolarVenta , inflacion = 0.0;
    boolean isDolarDataFetched = false;
    boolean isInflationDataFetched = false;
    double precioFinalDouble;
    double precioEnARS;


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
         textView_price3m = findViewById(R.id.textView_price3m);

        // Obtener el ID del juego seleccionado del intent
        String id = getIntent().getStringExtra("id");

        Log.d("ID del juego", "ID: " + id);
        // Llama al método para obtener los detalles del juego
        obtenerDetallesJuego(id);

        //Configurar la API de Dolar TC
        dolarAPIManager = new DolarAPIManager();

        fetchDataFromAPI();

        // Configurar la API de inflación
        configurarIPCApi();

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
                textView_priceARS.setText("");
                textView_price3m.setText("");
            } else {
                if (data.getPriceOverview() != null) {
                textViewPrice.setText("Precio: " + (data.getPriceOverview() != null ? data.getPriceOverview().getFinalFormatted() : "No disponible"));
                int precioFinalInt = data.getPriceOverview().getFinalPrice();
                precioFinalDouble = precioFinalInt / 100.0;

                // Recalcular el precio si ya se obtuvieron los datos necesarios
                if (isDolarDataFetched || isInflationDataFetched) {
                    recalcularPrecios();
                }
                } else {
                    textViewPrice.setText("Precio: No disponible");
                    textView_priceARS.setText("");
                    textView_price3m.setText("");
                }
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
                isDolarDataFetched = true;
                Log.d("Dolar Data", "Compra: " + dolarCompra + " Venta: " + dolarVenta);

                recalcularPrecios();
            }

            @Override
            public void onDolarDataError(String errorMessage) {
                mostrarMensaje(errorMessage);
                // Permitir que la aplicación siga funcionando sin los datos de dolar
                isDolarDataFetched = false;
                recalcularPrecios();
            }
        });
    }

    private void configurarIPCApi() {
        Retrofit retrofit = IPCApiManager.getClient("https://apis.datos.gob.ar/series/api/");
        ApiService apiService = retrofit.create(ApiService.class);

        String ids = "148.3_INIVELNAL_DICI_M_26";
        String format = "json";
        int maxResults = 5000; // Número máximo de resultados para obtener todos los datos desde 2016

        String url = "https://apis.datos.gob.ar/series/api/series?ids=" + ids + "&limit=" + maxResults + "&format=" + format;
        Log.d("URL Construida", url);

        Call<ApiResponse> call = apiService.getSeriesData(ids, String.valueOf(maxResults), format);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("Datos API", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<List<Object>> data = response.body().getData();
                    Log.d("Datos API", "Total de datos recibidos: " + (data != null ? data.size() : 0));
                    if (data != null && data.size() >= 2) {
                        // Obtener los últimos dos valores
                        List<Object> lastObservation = data.get(data.size() - 1);
                        List<Object> secondLastObservation = data.get(data.size() - 2);

                        String lastDate = (String) lastObservation.get(0);
                        double lastValue = ((Number) lastObservation.get(1)).doubleValue();
                        String secondLastDate = (String) secondLastObservation.get(0);
                        double secondLastValue = ((Number) secondLastObservation.get(1)).doubleValue();

                        Log.d("Datos API", "Última observación - Fecha: " + lastDate + ", Valor: " + lastValue);
                        Log.d("Datos API", "Penúltima observación - Fecha: " + secondLastDate + ", Valor: " + secondLastValue);

                        inflacion = ((lastValue - secondLastValue) / secondLastValue) * 100;
                        Log.d("Inflacion", inflacion + "%");
                        isInflationDataFetched = true;
                    } else {
                        mostrarMensaje("No hay suficientes datos de inflación para calcular.");
                        isInflationDataFetched = false;
                    }

                    recalcularPrecios(); // Asegurar que se recalculan los precios después de procesar los datos de inflación
                } else {
                    mostrarMensaje("Error en la respuesta de la API: " + response.code());
                    isInflationDataFetched = false;
                    recalcularPrecios();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Datos API", "Error al obtener datos de IPC: " + t.getMessage());
                mostrarMensaje("Error al obtener datos de IPC: " + t.getMessage());
                isInflationDataFetched = false;
                recalcularPrecios(); // Asegurar que se recalculan los precios incluso si la API de inflación falla
            }
        });
    }

    private void recalcularPrecios() {
        runOnUiThread(() -> {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            // Mostrar precios en ARS y precio proyectado
            if (isDolarDataFetched) {
                precioEnARS = precioFinalDouble + (precioFinalDouble * dolarVenta);
                Log.d("Dolar API", "Precio  ARS: " + decimalFormat.format(precioEnARS));
                textView_priceARS.setText("Precio en ARS: $" + decimalFormat.format(precioEnARS));
            } else {
                textView_priceARS.setText("Precio en ARS: No disponible");
            }

            if (isInflationDataFetched && isDolarDataFetched) {
                double precioSufrir = precioEnARS + (precioEnARS * (inflacion / 100));
                Log.d("IPC API", "Precio  Futuro: " + decimalFormat.format(precioSufrir));
                textView_price3m.setText("Precio proyectado prox. mes: $" + decimalFormat.format(precioSufrir));
            } else {
                textView_price3m.setText("Precio proyectado prox. mes: No disponible");
            }
        });
    }


}