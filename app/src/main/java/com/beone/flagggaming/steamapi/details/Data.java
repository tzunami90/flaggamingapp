package com.beone.flagggaming.steamapi.details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import javax.crypto.Mac;

public class Data{

	@SerializedName("header_image")
	private String headerImage;

	@SerializedName("short_description")
	private String shortDescription;

	@SerializedName("supported_languages")
	private String supportedLanguages;

	@SerializedName("capsule_imagev5")
	private String capsuleImagev5;

	@SerializedName("legal_notice")
	private String legalNotice;

	@SerializedName("achievements")
	private Achievements achievements;

	@SerializedName("developers")
	private List<String> developers;

	@SerializedName("content_descriptors")
	private ContentDescriptors contentDescriptors;

	@SerializedName("steam_appid")
	private int steamAppid;

	@SerializedName("pc_requirements")
	private PcRequirements pcRequirements;

	@SerializedName("price_overview")
	private PriceOverview priceOverview;

	@SerializedName("type")
	private String type;

	@SerializedName("controller_support")
	private String controllerSupport;

	@SerializedName("about_the_game")
	private String aboutTheGame;

	@SerializedName("recommendations")
	private Recommendations recommendations;

	@SerializedName("background_raw")
	private String backgroundRaw;

	@SerializedName("screenshots")
	private List<ScreenshotsItem> screenshots;

	@SerializedName("linux_requirements")
	private LinuxRequirements linuxRequirements;

	@SerializedName("platforms")
	private Platforms platforms;

	@SerializedName("movies")
	private List<MoviesItem> movies;

	@SerializedName("mac_requirements")
	private MacRequirements macRequirements;

	@SerializedName("genres")
	private List<GenresItem> genres;

	@SerializedName("ratings")
	private Ratings ratings;

	@SerializedName("publishers")
	private List<String> publishers;

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	@SerializedName("website")
	private String website;

	@SerializedName("packages")
	private List<Integer> packages;

	@SerializedName("required_age")
	private int requiredAge;

	@SerializedName("detailed_description")
	private String detailedDescription;

	@SerializedName("capsule_image")
	private String capsuleImage;

	@SerializedName("support_info")
	private SupportInfo supportInfo;

	@SerializedName("ext_user_account_notice")
	private String extUserAccountNotice;

	@SerializedName("package_groups")
	private List<PackageGroupsItem> packageGroups;

	@SerializedName("release_date")
	private ReleaseDate releaseDate;

	@SerializedName("background")
	private String background;

	@SerializedName("is_free")
	private boolean isFree;

	@SerializedName("name")
	private String name;

	@SerializedName("drm_notice")
	private String drmNotice;

	public String getHeaderImage(){
		return headerImage;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public String getSupportedLanguages(){
		return supportedLanguages;
	}

	public String getCapsuleImagev5(){
		return capsuleImagev5;
	}

	public String getLegalNotice(){
		return legalNotice;
	}

	public Achievements getAchievements(){
		return achievements;
	}

	public List<String> getDevelopers(){
		return developers;
	}

	public ContentDescriptors getContentDescriptors(){
		return contentDescriptors;
	}

	public int getSteamAppid(){
		return steamAppid;
	}

	public PcRequirements getPcRequirements(){
		return pcRequirements;
	}

	public PriceOverview getPriceOverview(){
		return priceOverview;
	}

	public String getType(){
		return type;
	}

	public String getControllerSupport(){
		return controllerSupport;
	}

	public String getAboutTheGame(){
		return aboutTheGame;
	}

	public Recommendations getRecommendations(){
		return recommendations;
	}

	public String getBackgroundRaw(){
		return backgroundRaw;
	}

	public List<ScreenshotsItem> getScreenshots(){
		return screenshots;
	}

	public LinuxRequirements getLinuxRequirements(){
		return linuxRequirements;
	}

	public Platforms getPlatforms(){
		return platforms;
	}

	public List<MoviesItem> getMovies(){
		return movies;
	}

	public MacRequirements getMacRequirements(){
		return macRequirements;
	}

	public List<GenresItem> getGenres(){
		return genres;
	}

	public Ratings getRatings(){
		return ratings;
	}

	public List<String> getPublishers(){
		return publishers;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	public String getWebsite(){
		return website;
	}

	public List<Integer> getPackages(){
		return packages;
	}

	public int getRequiredAge(){
		return requiredAge;
	}

	public String getDetailedDescription(){
		return detailedDescription;
	}

	public String getCapsuleImage(){
		return capsuleImage;
	}

	public SupportInfo getSupportInfo(){
		return supportInfo;
	}

	public String getExtUserAccountNotice(){
		return extUserAccountNotice;
	}

	public List<PackageGroupsItem> getPackageGroups(){
		return packageGroups;
	}

	public ReleaseDate getReleaseDate(){
		return releaseDate;
	}

	public String getBackground(){
		return background;
	}

	public boolean isIsFree(){
		return isFree;
	}

	public String getName(){
		return name;
	}

	public String getDrmNotice(){
		return drmNotice;
	}
}