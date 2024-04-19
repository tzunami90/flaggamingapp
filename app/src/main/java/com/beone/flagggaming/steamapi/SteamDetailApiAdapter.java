package com.beone.flagggaming.steamapi;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SteamDetailApiAdapter {
    private static SteamDetailApiService API_SERVICE;
    private static final String BASE_URL = "https://store.steampowered.com/";

    public static SteamDetailApiService getApiService(){
        // Creamos un interceptor y le indicamos el log level a usar
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- set log level
                    .build();

            API_SERVICE = retrofit.create(SteamDetailApiService.class);

            // Agregar mensaje de log para verificar la conexión
            Log.d("SteamDetailApiAdapter", "Solicitud a la API de Steam realizada correctamente");

        }

        return API_SERVICE;

    }
}