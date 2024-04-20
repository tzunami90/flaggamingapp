package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Esrb{

	@SerializedName("display_online_notice")
	private String displayOnlineNotice;

	@SerializedName("rating")
	private String rating;

	@SerializedName("interactive_elements")
	private String interactiveElements;

	public String getDisplayOnlineNotice(){
		return displayOnlineNotice;
	}

	public String getRating(){
		return rating;
	}

	public String getInteractiveElements(){
		return interactiveElements;
	}
}