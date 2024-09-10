package com.beone.flagggaming.epicapi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EpicGamesApiAdapter {

    private static final String BASE_URL = "https://graphql.epicgames.com/";

    // Crear una instancia de Retrofit
    private static Retrofit retrofit;

    public static EpicGamesApiService getApiService() {
        if (retrofit == null) {
            // Interceptor para logs
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(EpicGamesApiService.class);
    }
}
