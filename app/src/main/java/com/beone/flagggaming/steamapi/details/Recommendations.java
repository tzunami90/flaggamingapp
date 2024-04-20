package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Recommendations{

	@SerializedName("total")
	private int total;

	public int getTotal(){
		return total;
	}
}