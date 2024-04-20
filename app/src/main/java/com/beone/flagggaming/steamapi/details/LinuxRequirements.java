package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class LinuxRequirements {

	@SerializedName("minimum")
	private String minimum;

	@SerializedName("recommended")
	private String recommended;

	public String getMinimum(){
		return minimum;
	}

	public String getRecommended(){
		return recommended;
	}
}