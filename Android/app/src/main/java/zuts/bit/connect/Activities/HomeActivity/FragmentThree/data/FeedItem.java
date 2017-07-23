package zuts.bit.connect.Activities.HomeActivity.FragmentThree.data;

public class FeedItem {
	private int nid, userid, scope;
	private String title, body, branch, semester, image, createdat, userimg, username;

	public FeedItem() {
	}

	public void setNid(int nid){
		this.nid=nid;
	}

	public int getNid(){
		return this.nid;
	}

	public void setUserId(int uid){
		this.userid=uid;
	}

	public int getUserId(){
		return this.userid;
	}
	public void setScope(int scope){
		this.scope=scope;
	}

	public int getScope(){
		return this.scope;
	}

	public void setTitle(String value){
		this.title=value;
	}

	public String getTitle(){
		return this.title;
	}

	public void setBody(String value){
		this.body=value;
	}

	public String getBody(){
		return this.body;
	}

	public void setBranch(String value){
		this.branch=value;
	}

	public String getBranch(){
		return this.branch;
	}

	public void setSemester(String value){
		this.semester=value;
	}

	public String getSemester(){
		return this.semester;
	}

	public void setImage(String value){
		this.image=value;
	}

	public String getImage(){
		return this.image;
	}

    public void setUserImage(String value){
        this.userimg=value;
    }

    public String getUserImage(){
        return this.userimg;
    }

    public void setUserName(String value){
        this.username=value;
    }

    public String getUserName(){
        return this.username;
    }


    public void setTime(String value){
		this.createdat=value;
	}

	public String getTime(){
		return this.createdat;
	}


}
