package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data;

public class PaperListItem {
	private int id;
	private String uname, year, exam, url, sub;

	public PaperListItem() {
	}

	public PaperListItem(int id, String year, String exam, String url, String sub, String uname) {
		super();
		this.id=id;
		this.year=year;
		this.exam=exam;
		this.url=url;
		this.sub=sub;
		this.uname=uname;
	}

	public void setId(int id){
		this.id=id;
	}

	public int getId(){
		return this.id;
	}

	public void setExam(String name){
		this.exam=name;
	}

	public String getExam(){
		return this.exam;
	}

	public void setYear(String code){
		this.year=code;
	}

	public String getYear(){
		return this.year;
	}
	public void setUrl(String code){
		this.url=code;
	}

	public String getUrl(){
		return this.url;
	}
	public void setSub(String code){
		this.sub=code;
	}

	public String getSub(){
		return this.sub;
	}

	public void setUName(String uname){
		this.uname=uname;
	}

	public String getUName(){
		return this.uname;
	}

}
