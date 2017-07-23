package zuts.bit.connect.Activities.HomeActivity.TimelineElements.commentitem;

/**
 * Created by Devang on 2/1/2017.
 */

public class CommentItem {
    int id, userid;
    String userimg, comment, timestamp, username;

    public int getId() {
        return id;
    }

    public int getUserid() {
        return userid;
    }

    public String getComment() {
        return comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
