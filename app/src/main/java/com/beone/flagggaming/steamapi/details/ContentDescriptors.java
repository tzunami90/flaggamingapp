package com.beone.flagggaming.steamapi.details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ContentDescriptors{

	@SerializedName("notes")
	private Object notes;

	@SerializedName("ids")
	private List<Object> ids;

	public Object getNotes(){
		return notes;
	}

	public List<Object> getIds(){
		return ids;
	}
}