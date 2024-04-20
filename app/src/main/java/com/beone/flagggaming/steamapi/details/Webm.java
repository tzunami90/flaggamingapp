package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Webm{

	@SerializedName("max")
	private String max;

	@SerializedName("480")
	private String jsonMember480;

	public String getMax(){
		return max;
	}

	public String getJsonMember480(){
		return jsonMember480;
	}
}