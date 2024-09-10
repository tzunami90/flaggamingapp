package com.beone.flagggaming.epicapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EpicGamesApiService {
    @POST("graphql")
    Call<GraphQLResponse> searchStoreQuery(@Body GraphQLRequest request);
}