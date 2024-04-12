package com.beone.flagggaming.steamapi;

import com.google.gson.annotations.SerializedName;

public class AppsItem{

	@SerializedName("appid")
	private int appid;

	@SerializedName("name")
	private String name;

	public void setAppid(int appid){
		this.appid = appid;
	}

	public int getAppid(){
		return appid;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}