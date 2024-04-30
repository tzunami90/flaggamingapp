package com.beone.flagggaming.steamapi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SteamDetailApiAdapter {
    private static SteamDetailApiService API_SERVICE;
    private static final String BASE_URL = "https://store.steampowered.com/api/";

    public static SteamDetailApiService getApiService(){
        // Creamos un interceptor para el logging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Creamos un cliente OkHttpClient con el interceptor
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            API_SERVICE = retrofit.create(SteamDetailApiService.class);
        }

        return API_SERVICE;
    }
}