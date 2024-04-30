package com.beone.flagggaming.steamapi.details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import javax.crypto.Mac;

public class Data{
	@SerializedName("steam_appid")
	private int steamAppId;
	@SerializedName("header_image")
	private String headerImage;

	@SerializedName("short_description")
	private String shortDescription;

	@SerializedName("pc_requirements")
	private PcRequirements pcRequirements;

	@SerializedName("price_overview")
	private PriceOverview priceOverview;

	@SerializedName("type")
	private String type;

	@SerializedName("is_free")
	private boolean isFree;

	@SerializedName("name")
	private String name;

	public String getHeaderImage(){
		return headerImage;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public PcRequirements getPcRequirements(){
		return pcRequirements;
	}

	public PriceOverview getPriceOverview(){
		return priceOverview;
	}

	public String getType(){
		return type;
	}
	public boolean isIsFree(){
		return isFree;
	}

	public String getName(){
		return name;
	}
	public int getSteamAppId() {
		return steamAppId;
	}

}