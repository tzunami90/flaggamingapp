package com.beone.flagggaming.steamapi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Applist{

	@SerializedName("apps")
	private List<AppsItem> apps;

	public void setApps(List<AppsItem> apps){
		this.apps = apps;
	}

	public List<AppsItem> getApps(){
		return apps;
	}
}