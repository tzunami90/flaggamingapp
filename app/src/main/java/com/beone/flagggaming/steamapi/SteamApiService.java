package com.beone.flagggaming.steamapi;

import retrofit2.Call;
import retrofit2.http.GET;
public interface SteamApiService {

    @GET("ISteamApps/GetAppList/v2")
    Call<SteamAppList> getAppList();
}
