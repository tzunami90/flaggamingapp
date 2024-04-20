package com.beone.flagggaming.steamapi;
import com.google.gson.annotations.SerializedName;
import android.util.Log;
public class AppDetails {

    @SerializedName("name")
    private String name;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("pc_requirements")
    private Requirements pcRequirements;

    @SerializedName("mac_requirements")
    private Requirements macRequirements;

    @SerializedName("linux_requirements")
    private Requirements linuxRequirements;

    @SerializedName("header_image")
    private String headerImage;

    @SerializedName("price_overview")
    private PriceOverview priceOverview;

    // Getters
    public String getName() {
        Log.d("AppDetails", "getName() called. Name: " + name);
        return name;
    }

    public String getShortDescription() {
        Log.d("AppDetails", "getShortDescription() called. Short description: " + shortDescription);
        return shortDescription;
    }

    public Requirements getPcRequirements() {
        Log.d("AppDetails", "getPcRequirements() called. PC requirements: " + pcRequirements);
        return pcRequirements;
    }

    public Requirements getMacRequirements() {
        Log.d("AppDetails", "getMacRequirements() called. Mac requirements: " + macRequirements);
        return macRequirements;
    }

    public Requirements getLinuxRequirements() {
        Log.d("AppDetails", "getLinuxRequirements() called. Linux requirements: " + linuxRequirements);
        return linuxRequirements;
    }

    public String getHeaderImage() {
        Log.d("AppDetails", "getHeaderImage() called. Header image URL: " + headerImage);
        return headerImage;
    }

    public PriceOverview getPriceOverview() {
        Log.d("AppDetails", "getPriceOverview() called. Price overview: " + priceOverview);
        return priceOverview;
    }
}

class Requirements {

    @SerializedName("minimum")
    private String minimum;

    @SerializedName("recommended")
    private String recommended;

    // Getters
    public String getMinimum() {
        return minimum;
    }

    public String getRecommended() {
        return recommended;
    }
}

class PriceOverview {

    @SerializedName("final")
    private double finalPrice;

    @SerializedName("final_formatted")
    private String finalFormatted;

    // Getters
    public double getFinalPrice() {
        return finalPrice;
    }

    public String getFinalFormatted() {
        return finalFormatted;
    }
}