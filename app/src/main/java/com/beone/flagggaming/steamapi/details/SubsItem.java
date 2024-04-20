package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class SubsItem{

	@SerializedName("option_description")
	private String optionDescription;

	@SerializedName("can_get_free_license")
	private String canGetFreeLicense;

	@SerializedName("option_text")
	private String optionText;

	@SerializedName("price_in_cents_with_discount")
	private int priceInCentsWithDiscount;

	@SerializedName("percent_savings")
	private int percentSavings;

	@SerializedName("packageid")
	private int packageid;

	@SerializedName("is_free_license")
	private boolean isFreeLicense;

	@SerializedName("percent_savings_text")
	private String percentSavingsText;

	public String getOptionDescription(){
		return optionDescription;
	}

	public String getCanGetFreeLicense(){
		return canGetFreeLicense;
	}

	public String getOptionText(){
		return optionText;
	}

	public int getPriceInCentsWithDiscount(){
		return priceInCentsWithDiscount;
	}

	public int getPercentSavings(){
		return percentSavings;
	}

	public int getPackageid(){
		return packageid;
	}

	public boolean isIsFreeLicense(){
		return isFreeLicense;
	}

	public String getPercentSavingsText(){
		return percentSavingsText;
	}
}