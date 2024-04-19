package com.beone.flagggaming.steamapi;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class AppDetailsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private Map<String, AppDetails> data;

    public boolean isSuccess() {
        return success;
    }

    public Map<String, AppDetails> getData() {
        return data;
    }
}