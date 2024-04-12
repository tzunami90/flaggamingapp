public class Response{

	@SerializedName("applist")
	private Applist applist;

	public void setApplist(Applist applist){
		this.applist = applist;
	}

	public Applist getApplist(){
		return applist;
	}
}
