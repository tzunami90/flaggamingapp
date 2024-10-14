package com.beone.flagggaming.steamapi;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SteamDetailApiService {
    @GET("appdetails")
    Call<Map<String, AppDetailsResponse.GameDetails>> getAppDetails(@Query("appids") String appids, @Query("cc") String cc);
}