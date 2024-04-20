package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class HighlightedItem{

	@SerializedName("path")
	private String path;

	@SerializedName("name")
	private String name;

	public String getPath(){
		return path;
	}

	public String getName(){
		return name;
	}
}