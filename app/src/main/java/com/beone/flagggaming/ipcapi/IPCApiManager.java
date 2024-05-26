package com.beone.flagggaming.ipcapi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class IPCApiManager {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
