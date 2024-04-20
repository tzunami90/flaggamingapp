package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class ScreenshotsItem{

	@SerializedName("path_full")
	private String pathFull;

	@SerializedName("path_thumbnail")
	private String pathThumbnail;

	@SerializedName("id")
	private int id;

	public String getPathFull(){
		return pathFull;
	}

	public String getPathThumbnail(){
		return pathThumbnail;
	}

	public int getId(){
		return id;
	}
}