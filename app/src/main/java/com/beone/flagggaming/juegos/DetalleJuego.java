package com.beone.flagggaming.juegos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.beone.flagggaming.R;
import com.beone.flagggaming.db.DBHelper;
import com.beone.flagggaming.dolarapi.DolarAPIManager;
import com.beone.flagggaming.dolarapi.DolarApiResponse;
import com.beone.flagggaming.epicapi.EpicGamesApiAdapter;
import com.beone.flagggaming.epicapi.EpicGamesApiService;
import com.beone.flagggaming.epicapi.GraphQLRequest;
import com.beone.flagggaming.epicapi.GraphQLResponse;
import com.beone.flagggaming.ipcapi.ApiResponse;
import com.beone.flagggaming.ipcapi.ApiService;
import com.beone.flagggaming.ipcapi.IPCApiManager;
import com.beone.flagggaming.steamapi.AppDetailsResponse;
import com.beone.flagggaming.steamapi.SteamDetailApiAdapter;
import com.beone.flagggaming.steamapi.SteamDetailApiService;
import com.beone.flagggaming.steamapi.details.Data;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DetalleJuego extends Fragment {

    private String idFlagg;
    private TextView nombreTextView;
    private TextView descripcionTextView;
    private ImageView imagenImageView;
    private ImageView icSteam, icEpic;
    private TextView precioSteam, precioEpic, precioPareSteam, precioPareEpic, precioPostaSteam, precioPostaEpic, estudioTextView, requisitosTextView;
    private DolarAPIManager dolarAPIManager;
    double dolarCompra, dolarVenta , inflacion = 0.0;
    boolean isDolarDataFetched = false;
    boolean isInflationDataFetched = false;
    double precioFinalDouble;
    double precioEnARS;
    private ProgressBar progressBar;


    public DetalleJuego() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idFlagg = getArguments().getString("idFlagg");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_juego, container, false);

        nombreTextView = view.findViewById(R.id.nombreTextView);
        descripcionTextView = view.findViewById(R.id.descripcionTextView);
        imagenImageView = view.findViewById(R.id.imagenImageView);
        icSteam = view.findViewById(R.id.icSteam);
        icEpic = view.findViewById(R.id.icEpic);
        precioEpic = view.findViewById(R.id.precioEpic);
        precioSteam = view.findViewById(R.id.precioSteam);
        precioPareSteam = view.findViewById(R.id.precioPareSteam);
        precioPareEpic = view.findViewById(R.id.precioPareEpic);
        precioPostaSteam = view.findViewById(R.id.precioPostaSteam);
        precioPostaEpic = view.findViewById(R.id.precioPostaEpic);
        estudioTextView = view.findViewById(R.id.estudioTextView);
        requisitosTextView = view.findViewById(R.id.requisitosTextView);

        progressBar = view.findViewById(R.id.progressBar); // Inicializar el ProgressBar

        // Mostrar ProgressBar antes de comenzar la carga
        progressBar.setVisibility(View.VISIBLE);

        cargarDetallesJuego();

        // Configurar la API de Dolar TC
        dolarAPIManager = new DolarAPIManager();
        fetchDataFromAPI();

        // Configurar la API de inflación
        configurarIPCApi();


        return view;
    }
    private void cargarDetallesJuego() {
        if (idFlagg != null) {
            new Thread(() -> {
                Juego juego = DBHelper.getJuegoByIdFlagg(getContext(), idFlagg);
                if (juego != null) {
                    obtenerPrecio(juego);
                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Juego no encontrado", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Ocultar cuando haya un error
                    });
                }
            }).start();
        }
    }

    private void obtenerPrecio(Juego juego) {
        // Obtener el servicio de la API de Steam
        SteamDetailApiService apiService = SteamDetailApiAdapter.getApiService();

        // Obtener el servicio de la API de Epic
        EpicGamesApiService epicService = EpicGamesApiAdapter.getApiService();

        // Realizar la solicitud de detalles del juego para Steam
        Call<Map<String, AppDetailsResponse.GameDetails>> call = apiService.getAppDetails(juego.getIdJuegoTienda(), "ar");

        call.enqueue(new Callback<Map<String, AppDetailsResponse.GameDetails>>() {
            @Override
            public void onResponse(Call<Map<String, AppDetailsResponse.GameDetails>> call, Response<Map<String, AppDetailsResponse.GameDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, AppDetailsResponse.GameDetails> appDetailsResponse = response.body();
                    Log.d("Response Body", response.body().toString());

                    // Obtener los detalles del juego y mostrarlos en la interfaz de usuario
                    mostrarDetallesJuego(appDetailsResponse, juego);
                } else {
                    // Mostrar mensaje de error si la solicitud no fue exitosa
                    mostrarMensaje("La solicitud no fue exitosa. Código de respuesta: " + response.code());
                }
                progressBar.setVisibility(View.GONE);  // Ocultar cuando se terminen de cargar los datos
            }

            @Override
            public void onFailure(Call<Map<String, AppDetailsResponse.GameDetails>> call, Throwable t) {
                // Manejar el fallo de la solicitud
                Log.e("Response", "Error de red", t);
                mostrarMensaje("Error de red: " + t.getMessage());
                progressBar.setVisibility(View.GONE); // Ocultar cuando haya un fallo
            }
        });

        // Realizar la solicitud de precio para Epic Games
        String query = "{\n" +
                "  \"query\": \"{ Catalog { searchStore(query: \\\"" + juego.getNombre() + "\\\", locale: \\\"en-US\\\", country: \\\"AR\\\", storeType: \\\"EPIC_GAMES_STORE\\\") { elements { title price { totalPrice { discountPrice } } } } } }\"\n" +
                "}";

        GraphQLRequest request = new GraphQLRequest(query);

        Call<GraphQLResponse> epicCall = epicService.searchStoreQuery(request);

        epicCall.enqueue(new Callback<GraphQLResponse>() {
            @Override
            public void onResponse(Call<GraphQLResponse> call, Response<GraphQLResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GraphQLResponse.Element> elements = response.body().getData().getCatalog().getSearchStore().getElements();
                    if (!elements.isEmpty()) {
                        // Obtener el precio de descuento como string
                        String discountPriceStr = elements.get(0).getPrice().getTotalPrice().getFmtPrice().getDiscountPrice();
                        Log.d("Epic API Response", response.body().toString());
                        Log.d("Precio en dólares Epic", discountPriceStr);
                        if (discountPriceStr != null) {
                            try {
                                // Eliminar el símbolo de dólar y convertir a centavos
                                String priceWithoutDollar = discountPriceStr.replace("$", "").trim();
                                double priceInDollars = Double.parseDouble(priceWithoutDollar);
                                int priceInCents = (int) Math.round(priceInDollars * 100);

                                // Llamar a mostrarDetallesEpic con el precio en centavos
                                mostrarDetallesEpic(priceInCents, juego);

                            } catch (NumberFormatException e) {
                                // Manejar el caso de que el precio no sea un número válido
                                e.printStackTrace();
                                mostrarMensaje("Error formateando el precio de Epic Games");
                            }
                        } else {
                            // Manejar el caso donde el precio sea null
                            precioEpic.setText("Precio: No disponible");
                            precioPostaEpic.setText("");
                            precioPareEpic.setText("");
                        }
                    } else {
                        // No hay elementos en la lista
                        precioEpic.setText("Precio: No disponible");
                        precioPostaEpic.setText("");
                        precioPareEpic.setText("");
                    }
                } else {
                    mostrarMensaje("Error obteniendo precio de Epic Games");
                }
            }

            @Override
            public void onFailure(Call<GraphQLResponse> call, Throwable t) {
                mostrarMensaje("Error de red en Epic Games: " + t.getMessage());
            }
        });
    }

    private void mostrarDetallesJuego(Map<String, AppDetailsResponse.GameDetails> appDetailsResponse, Juego juego) {
        if (appDetailsResponse == null) {
            mostrarMensaje("La respuesta del servidor es nula. No se pudieron obtener los detalles del juego.");
            return;
        }

        // Obtener los detalles específicos del juego
        AppDetailsResponse.GameDetails gameDetails = appDetailsResponse.get(juego.getIdJuegoTienda());
        if (gameDetails == null || !gameDetails.isSuccess()) {
            mostrarMensaje("La solicitud para el juego con ID: " + juego.getIdJuegoTienda() + " no fue exitosa. Los detalles del juego no están disponibles.");
            return;
        }
        Log.d("gameDetails", gameDetails.toString());

        // Obtener los datos del juego
        Data data = gameDetails.getData();
        if (data == null) {
            mostrarMensaje("Los datos del juego son nulos para el ID: " + juego.getIdJuegoTienda());
            return;
        }

        // Mostrar los detalles del juego en la interfaz de usuario
        getActivity().runOnUiThread(() -> {
            nombreTextView.setText(juego.getNombre());
            descripcionTextView.setText(juego.getDescripcionCorta());
            Glide.with(getContext()).load(juego.getImagen()).into(imagenImageView);
            estudioTextView.setText("Estudio: " + juego.getEstudio());
            requisitosTextView.setText("Requisitos: " + juego.getRequisitos());

            // Procesar el precio de Steam
            if (data.isIsFree()) {
                precioSteam.setText("Precio: GRATIS");
                precioPostaSteam.setText("");
                precioPareSteam.setText("");
            } else {
                if (data.getPriceOverview() != null) {
                    juego.setPrecioSteam(data.getPriceOverview().getFinalFormatted());
                    precioSteam.setText("Precio: " + juego.getPrecioSteam());

                    int precioFinalInt = data.getPriceOverview().getFinalPrice();
                    precioFinalDouble = precioFinalInt / 100.0;

                    // Recalcular el precio si ya se obtuvieron los datos necesarios
                    if (isDolarDataFetched || isInflationDataFetched) {
                        recalcularPrecios(juego);
                    }
                } else {
                    precioSteam.setText("Precio: No disponible");
                    precioPostaSteam.setText("");
                    precioPareSteam.setText("");
                }
            }
        });
    }

    private void mostrarDetallesEpic(double epicDiscountPrice, Juego juego) {
        getActivity().runOnUiThread(() -> {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            precioEpic.setText("Precio: $" + decimalFormat.format(epicDiscountPrice));
            precioFinalDouble = epicDiscountPrice;

            if (isDolarDataFetched || isInflationDataFetched) {
                recalcularPreciosEpic(juego);
            }
        });
    }


    private void fetchDataFromAPI() {
        dolarAPIManager.getDolarData(new DolarAPIManager.DolarDataListener() {
            @Override
            public void onDolarDataReceived(DolarApiResponse dolarData) {
                dolarCompra = dolarData.getCompra();
                dolarVenta = dolarData.getVenta();
                isDolarDataFetched = true;
                Log.d("Dolar Data", "Compra: " + dolarCompra + " Venta: " + dolarVenta);

                recalcularPrecios(null); // Recalcular precios con los datos de dolar
            }

            @Override
            public void onDolarDataError(String errorMessage) {
                mostrarMensaje(errorMessage);
                isDolarDataFetched = false;
                recalcularPrecios(null); // Permitir que la aplicación siga funcionando sin los datos de dolar
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

                    recalcularPrecios(null); // Asegurar que se recalculan los precios después de procesar los datos de inflación
                } else {
                    mostrarMensaje("Error en la respuesta de la API: " + response.code());
                    isInflationDataFetched = false;
                    recalcularPrecios(null); // Asegurar que se recalculan los precios incluso si la API de inflación falla
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Datos API", "Error al obtener datos de IPC: " + t.getMessage());
                mostrarMensaje("Error al obtener datos de IPC: " + t.getMessage());
                isInflationDataFetched = false;
                recalcularPrecios(null); // Asegurar que se recalculan los precios incluso si la API de inflación falla
            }
        });
    }

    private void recalcularPrecios(Juego juego) {
        getActivity().runOnUiThread(() -> {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            // Mostrar precios en ARS y precio proyectado
            if (isDolarDataFetched) {
                precioEnARS = precioFinalDouble * dolarVenta;
                Log.d("Dolar API", "Precio ARS: " + decimalFormat.format(precioEnARS));
                precioPostaSteam.setText("Precio Posta (ARS): $" + decimalFormat.format(precioEnARS));
                if (juego != null) {
                    juego.setPrecioPostaSteam("Precio Posta ARS: $" + decimalFormat.format(precioEnARS));
                }
            } else {
                precioPostaSteam.setText("Precio Posta (ARS): No disponible");
            }

            if (isInflationDataFetched && isDolarDataFetched) {
                double precioSufrir = precioEnARS + (precioEnARS * (inflacion / 100));
                Log.d("IPC API", "Precio Pare de Sufrir: " + decimalFormat.format(precioSufrir));
                precioPareSteam.setText("Precio Pare de Sufrir (Prox mes): $" + decimalFormat.format(precioSufrir));
                if (juego != null) {
                    juego.setPrecioPareSteam("Precio Pare de Sufrir prox. mes: $" + decimalFormat.format(precioSufrir));
                }
            } else {
                precioPareSteam.setText("Precio Pare de Sufrir (Prox mes): No disponible");
            }
        });
    }
    private void recalcularPreciosEpic(Juego juego) {
        getActivity().runOnUiThread(() -> {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            if (isDolarDataFetched) {
                precioEnARS = precioFinalDouble * dolarVenta;
                precioPostaEpic.setText("Precio Posta (ARS): $" + decimalFormat.format(precioEnARS));
                juego.setPrecioPostaEpic("Precio Posta ARS: $" + decimalFormat.format(precioEnARS));
            } else {
                precioPostaEpic.setText("Precio Posta (ARS): No disponible");
            }

            if (isInflationDataFetched && isDolarDataFetched) {
                double precioSufrir = precioEnARS + (precioEnARS * (inflacion / 100));
                precioPareEpic.setText("Precio Pare de Sufrir (Prox mes): $" + decimalFormat.format(precioSufrir));
                juego.setPrecioPareEpic("Precio Pare de Sufrir prox. mes: $" + decimalFormat.format(precioSufrir));
            } else {
                precioPareEpic.setText("Precio Pare de Sufrir (Prox mes): No disponible");
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Log.d("MostrarJuegoSteam", mensaje);
        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}