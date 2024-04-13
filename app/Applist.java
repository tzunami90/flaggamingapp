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
