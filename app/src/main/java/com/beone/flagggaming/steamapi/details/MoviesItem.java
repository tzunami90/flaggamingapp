package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class MoviesItem{

	@SerializedName("mp4")
	private Mp4 mp4;

	@SerializedName("highlight")
	private boolean highlight;

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("webm")
	private Webm webm;

	public Mp4 getMp4(){
		return mp4;
	}

	public boolean isHighlight(){
		return highlight;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public Webm getWebm(){
		return webm;
	}
}