package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class PriceOverview{

	@SerializedName("final")
	private int finalPrice;
	@SerializedName("final_formatted")
	private String finalFormatted;

	public String getFinalFormatted(){
		return finalFormatted;
	}

	public int getFinalPrice(){ return  finalPrice; }
}