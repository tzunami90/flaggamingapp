package com.beone.flagggaming.steamapi;
import com.beone.flagggaming.steamapi.details.Data;
import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class AppDetailsResponse {
    private Map<String, GameDetails> gameDetailsMap;

    public Map<String, GameDetails> getGameDetailsMap() {
        return gameDetailsMap;
    }

    // Clase interna para representar los detalles de cada juego
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
    @Override
    public String toString() {
        return "AppDetailsResponse{" +
                "gameDetailsMap=" + gameDetailsMap +
                '}';
    }
}