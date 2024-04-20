package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Oflc{

	@SerializedName("descriptors")
	private String descriptors;

	@SerializedName("rating")
	private String rating;

	public String getDescriptors(){
		return descriptors;
	}

	public String getRating(){
		return rating;
	}
}