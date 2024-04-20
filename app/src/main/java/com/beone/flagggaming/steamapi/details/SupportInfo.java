package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class SupportInfo{

	@SerializedName("url")
	private String url;

	@SerializedName("email")
	private String email;

	public String getUrl(){
		return url;
	}

	public String getEmail(){
		return email;
	}
}