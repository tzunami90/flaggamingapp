package com.beone.flagggaming.dolarapi;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DolarAPIService {
    @GET("v1/dolares/tarjeta")
    Call<DolarApiResponse> getDolarData();
}