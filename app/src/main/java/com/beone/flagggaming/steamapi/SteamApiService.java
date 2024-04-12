package com.beone.flagggaming.steamapi;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
public interface SteamApiService {

    @GET("ISteamApps/GetAppList/v2")
   Call<List<Juegos>> getJuego();
}
