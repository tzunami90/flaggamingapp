package com.beone.flagggaming.ipcapi;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("series")
    Call<ApiResponse> getSeriesData(
            @Query("ids") String ids,
            @Query("limit") String limit,
            @Query("format") String format
    );
}
