package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class ReleaseDate{

	@SerializedName("coming_soon")
	private boolean comingSoon;

	@SerializedName("date")
	private String date;

	public boolean isComingSoon(){
		return comingSoon;
	}

	public String getDate(){
		return date;
	}
}