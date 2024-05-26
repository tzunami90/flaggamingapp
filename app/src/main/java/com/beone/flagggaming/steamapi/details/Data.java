package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Data {
	@SerializedName("name")
	private String name;

	@SerializedName("short_description")
	private String shortDescription;

	@SerializedName("pc_requirements")
	private PcRequirements pcRequirements;

	@SerializedName("is_free")
	private boolean isFree;

	@SerializedName("price_overview")
	private PriceOverview priceOverview;

	@SerializedName("header_image")
	private String headerImage;

	public String getName() {
		return name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public PcRequirements getPcRequirements() {
		return pcRequirements;
	}

	public boolean isIsFree() {
		return isFree;
	}

	public PriceOverview getPriceOverview() {
		return priceOverview;
	}

	public String getHeaderImage() {
		return headerImage;
	}
}
