package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class SteamGermany{

	@SerializedName("descriptors")
	private String descriptors;

	@SerializedName("rating_generated")
	private String ratingGenerated;

	@SerializedName("use_age_gate")
	private String useAgeGate;

	@SerializedName("rating")
	private String rating;

	@SerializedName("banned")
	private String banned;

	@SerializedName("required_age")
	private String requiredAge;

	public String getDescriptors(){
		return descriptors;
	}

	public String getRatingGenerated(){
		return ratingGenerated;
	}

	public String getUseAgeGate(){
		return useAgeGate;
	}

	public String getRating(){
		return rating;
	}

	public String getBanned(){
		return banned;
	}

	public String getRequiredAge(){
		return requiredAge;
	}
}