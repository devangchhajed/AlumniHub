package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data;

public class ListItem {
	private int id;
	private String subjectcode, subjectname;

	public ListItem() {
	}

	public ListItem(int id, String subjectcode, String subjectname) {
		super();
		this.id=id;
		this.subjectcode=subjectcode;
		this.subjectname=subjectname;
	}

	public void setId(int id){
		this.id=id;
	}

	public int getId(){
		return this.id;
	}

	public void setSubjectName(String name){
		this.subjectname=name;
	}

	public String getSubjectname(){
		return this.subjectname;
	}

	public void setSubjectcode(String code){
		this.subjectcode=code;
	}

	public String getSubjectcode(){
		return this.subjectcode;
	}

}
