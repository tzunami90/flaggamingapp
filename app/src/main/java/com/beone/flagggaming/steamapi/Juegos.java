package com.beone.flagggaming.steamapi;

import java.util.List;
import com.google.gson.annotations.SerializedName;
public class Juegos {

    @SerializedName("appid")
    private int appId;
    @SerializedName("name")
    private String appName;

    public int getAppId() {
        return appId;
    }


    public String getAppName() {
        return appName;
    }

}
