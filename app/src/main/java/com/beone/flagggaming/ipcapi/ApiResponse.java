package com.beone.flagggaming.ipcapi;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("data")
    private List<List<Object>> data;

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }
}