package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class GenresItem{

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private String id;

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}
}