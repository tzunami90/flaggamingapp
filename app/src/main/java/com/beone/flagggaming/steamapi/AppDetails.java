package com.beone.flagggaming.steamapi;
import com.google.gson.annotations.SerializedName;

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
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Requirements getPcRequirements() {
        return pcRequirements;
    }

    public Requirements getMacRequirements() {
        return macRequirements;
    }

    public Requirements getLinuxRequirements() {
        return linuxRequirements;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public PriceOverview getPriceOverview() {
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