package com.beone.flagggaming.dolarapi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DolarAPIManager {
    private static final String BASE_URL = "https://dolarapi.com/";

    private DolarAPIService dolarAPIService;

    public DolarAPIManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dolarAPIService = retrofit.create(DolarAPIService.class);
    }

    public void getDolarData(final DolarDataListener listener) {
        Call<DolarApiResponse> call = dolarAPIService.getDolarData();
        call.enqueue(new Callback<DolarApiResponse>() {
            @Override
            public void onResponse(Call<DolarApiResponse> call, Response<DolarApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onDolarDataReceived(response.body());
                } else {
                    listener.onDolarDataError("Error al obtener los datos del dólar");
                }
            }

            @Override
            public void onFailure(Call<DolarApiResponse> call, Throwable t) {
                listener.onDolarDataError("Error de red: " + t.getMessage());
            }
        });
    }

    public interface DolarDataListener {
        void onDolarDataReceived(DolarApiResponse dolarData);
        void onDolarDataError(String errorMessage);
    }
}
