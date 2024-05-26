package com.beone.flagggaming.steamapi;
import com.beone.flagggaming.steamapi.details.Data;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class AppDetailsResponse {
    @SerializedName("data")
    private Map<String, GameDetails> gameDetailsMap;

    public Map<String, GameDetails> getGameDetailsMap() {
        return gameDetailsMap;
    }

    public static class GameDetails {
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
}
