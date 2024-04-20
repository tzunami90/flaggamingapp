package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class CategoriesItem{

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	public String getDescription(){
		return description;
	}

	public int getId(){
		return id;
	}
}