package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class PriceOverview{

	@SerializedName("final_formatted")
	private String finalFormatted;

	@SerializedName("initial")
	private int initial;

	@SerializedName("final")
	private int jsonMemberFinal;

	@SerializedName("currency")
	private String currency;

	@SerializedName("initial_formatted")
	private String initialFormatted;

	@SerializedName("discount_percent")
	private int discountPercent;

	public String getFinalFormatted(){
		return finalFormatted;
	}

	public int getInitial(){
		return initial;
	}

	public int getJsonMemberFinal(){
		return jsonMemberFinal;
	}

	public String getCurrency(){
		return currency;
	}

	public String getInitialFormatted(){
		return initialFormatted;
	}

	public int getDiscountPercent(){
		return discountPercent;
	}
}