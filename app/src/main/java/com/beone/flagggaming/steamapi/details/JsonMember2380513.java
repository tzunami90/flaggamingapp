package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class JsonMember2380513{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}