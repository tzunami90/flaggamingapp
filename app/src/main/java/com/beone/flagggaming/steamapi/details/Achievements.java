package com.beone.flagggaming.steamapi.details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Achievements{

	@SerializedName("total")
	private int total;

	@SerializedName("highlighted")
	private List<HighlightedItem> highlighted;

	public int getTotal(){
		return total;
	}

	public List<HighlightedItem> getHighlighted(){
		return highlighted;
	}
}