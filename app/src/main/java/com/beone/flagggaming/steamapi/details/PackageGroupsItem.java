package com.beone.flagggaming.steamapi.details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PackageGroupsItem{

	@SerializedName("display_type")
	private int displayType;

	@SerializedName("subs")
	private List<SubsItem> subs;

	@SerializedName("save_text")
	private String saveText;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("is_recurring_subscription")
	private String isRecurringSubscription;

	@SerializedName("title")
	private String title;

	@SerializedName("selection_text")
	private String selectionText;

	public int getDisplayType(){
		return displayType;
	}

	public List<SubsItem> getSubs(){
		return subs;
	}

	public String getSaveText(){
		return saveText;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public String getIsRecurringSubscription(){
		return isRecurringSubscription;
	}

	public String getTitle(){
		return title;
	}

	public String getSelectionText(){
		return selectionText;
	}
}