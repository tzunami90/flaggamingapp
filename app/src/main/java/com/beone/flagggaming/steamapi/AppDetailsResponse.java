package com.beone.flagggaming.steamapi;
import com.beone.flagggaming.steamapi.details.Data;
import com.google.gson.annotations.SerializedName;

public class AppDetailsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }
}