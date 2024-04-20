package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Usk{

	@SerializedName("descriptors")
	private String descriptors;

	@SerializedName("use_age_gate")
	private String useAgeGate;

	@SerializedName("rating")
	private String rating;

	public String getDescriptors(){
		return descriptors;
	}

	public String getUseAgeGate(){
		return useAgeGate;
	}

	public String getRating(){
		return rating;
	}
}