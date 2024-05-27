package com.beone.flagggaming.steamapi;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
import com.google.gson.Gson;
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

        dolarAPIManager = new DolarAPIManager();

        // Obtener el mes actual y el mes anterior en el formato requerido (YYYY-MM-DD)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Fecha del primer día del mes actual
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String currentDate = sdf.format(calendar.getTime());

        // Fecha del primer día del mes anterior
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);  // Asegurar que es el primer día del mes
        String previousMonthDate = sdf.format(calendar.getTime());

        // Fecha del primer día del mes anterior al mes anterior
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);  // Asegurar que es el primer día del mes
        String secondPreviousMonthDate = sdf.format(calendar.getTime());

        Log.d("Fechas", "Current Date: " + currentDate);
        Log.d("Fechas", "Previous Month Date: " + previousMonthDate);
        Log.d("Fechas", "Second Previous Month Date: " + secondPreviousMonthDate);


        // Ajuste de fechas para la solicitud de API
        calendar.add(Calendar.MONTH, 2); // Volver al mes actual después de restar dos meses
        String endDate = sdf.format(calendar.getTime());

// Configurar Retrofit y hacer la llamada a la API
        Retrofit retrofit = IPCApiManager.getClient("https://apis.datos.gob.ar/series/api/");
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ApiResponse> call = apiService.getSeriesData("148.3_INIVELNAL_DICI_M_26", secondPreviousMonthDate, "json");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<List<Object>> data = response.body().getData();
                    Log.d("Datos API", "Total de datos recibidos: " + data.size());
                    for (List<Object> observation : data) {
                        String date = (String) observation.get(0);
                        Double value = ((Number) observation.get(1)).doubleValue();
                        Log.d("Datos API", "Fecha: " + date + ", Valor: " + value);
                    }
                    processObservations(data, previousMonthDate, secondPreviousMonthDate);
                } else {
                    mostrarMensaje("Error en la respuesta de la API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mostrarMensaje("Error al obtener datos de IPC: " + t.getMessage());
            }
        });

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
                textView_priceARS.setText("");
                textView_price3m.setText("");
            } else {
                if (data.getPriceOverview() != null) {
                textViewPrice.setText("Precio: " + (data.getPriceOverview() != null ? data.getPriceOverview().getFinalFormatted() : "No disponible"));
                int precioFinalInt = data.getPriceOverview().getFinalPrice();
                precioFinalDouble = precioFinalInt / 100.0;

                // Recalcular el precio si ya se obtuvieron los datos necesarios
                if (isDolarDataFetched && isInflationDataFetched) {
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

                // Recalcular el precio si ya se obtuvieron los datos de inflación
                if (isInflationDataFetched) {
                    recalcularPrecios();
                }
            }

            @Override
            public void onDolarDataError(String errorMessage) {
                mostrarMensaje(errorMessage);
            }
        });
    }

    private void processObservations(List<List<Object>> data, String previousMonthDate, String secondPreviousMonthDate) {
        Double previousMonthValue = null;
        Double secondPreviousMonthValue = null;

        Log.d("Fechas", "Previous Month Date: " + previousMonthDate + ", Second Previous Month Date: " + secondPreviousMonthDate);

        for (List<Object> observation : data) {
            String date = (String) observation.get(0);
            Double value = ((Number) observation.get(1)).doubleValue();

            Log.d("Datos IPC", "Fecha: " + date + ", Valor: " + value);

            // Comparar fechas con los valores requeridos
            if (date.equals(previousMonthDate)) {
                previousMonthValue = value;
                Log.d("Datos IPC", "Valor del mes anterior encontrado: " + value);
            } else if (date.equals(secondPreviousMonthDate)) {
                secondPreviousMonthValue = value;
                Log.d("Datos IPC", "Valor del segundo mes anterior encontrado: " + value);
            }
        }

        // Verificar si ambos valores han sido encontrados
        if (previousMonthValue != null && secondPreviousMonthValue != null) {
            // Calcular la inflación
            inflacion = ((previousMonthValue / secondPreviousMonthValue) - 1) * 100;
            isInflationDataFetched = true;
            Log.d("Inflación Calculada", "Inflación: " + inflacion + "%");
            // Recalcular el precio si ya se obtuvieron los datos del dólar
            if (isDolarDataFetched) {
                recalcularPrecios();
            }
        } else {
            mostrarMensaje("No se encontraron datos suficientes para calcular la inflación.");
        }
    }

    private void recalcularPrecios() {
        runOnUiThread(() -> {
            // No realizar cálculos si el juego es gratuito
            if (precioFinalDouble == 0) {
                return;
            }

            double precioFinal = precioFinalDouble * dolarVenta;
            DecimalFormat decimalFormat = new DecimalFormat("#,###,##0.00");
            String precioFinalFormateado = decimalFormat.format(precioFinal);
            textView_priceARS.setText("Precio Posta: " + precioFinalFormateado + " ARS");

            double precioSufrir = precioFinal * (1 + (inflacion / 100));
            String precioPareFormateado = decimalFormat.format(precioSufrir);
            textView_price3m.setText("Precio Pare de Sufrir (próximo mes): " + precioPareFormateado + " ARS");
        });
    }

}