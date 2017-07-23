package zuts.bit.connect.Activities.HomeActivity.FragmentOne.data;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedItem {
	private int id, userid, likes=0, comments=0, userlike;
	private String name, status, image, profilePic, timeStamp, url;

	public FeedItem() {
	}

	public int getUserId() {
		return userid;
	}

	public void setUserId(int id) {
		this.userid = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getUserLike() {
		return userlike;
	}

	public void setUserLike(int likes) {
		this.userlike = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}


	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
