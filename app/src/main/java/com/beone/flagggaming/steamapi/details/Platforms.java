package com.beone.flagggaming.steamapi.details;

import com.google.gson.annotations.SerializedName;

public class Platforms{

	@SerializedName("linux")
	private boolean linux;

	@SerializedName("windows")
	private boolean windows;

	@SerializedName("mac")
	private boolean mac;

	public boolean isLinux(){
		return linux;
	}

	public boolean isWindows(){
		return windows;
	}

	public boolean isMac(){
		return mac;
	}
}