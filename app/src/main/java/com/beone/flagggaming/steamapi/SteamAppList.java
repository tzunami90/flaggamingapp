package com.beone.flagggaming.steamapi;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SteamAppList {
    @SerializedName("applist")
    private AppList applist;

    public AppList  getAppList() {
        return applist;
    }

    public static class AppList {
        @SerializedName("apps")
        private List<Juegos> apps;

        public List<Juegos> getApps() {
            return apps;
        }
    }
}
