package com.beone.flagggaming.steamapi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SteamDetailApiService {
    @GET("appdetails")
    Call<AppDetailsResponse> getAppDetails(@Query("appids") String id, @Query("cc") String cc);
}
