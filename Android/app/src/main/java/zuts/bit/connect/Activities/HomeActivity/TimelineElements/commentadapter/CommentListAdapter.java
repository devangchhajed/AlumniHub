package zuts.bit.connect.Activities.HomeActivity.TimelineElements.commentadapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuts.bit.connect.Activities.Browser.BrowserActivity;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.FeedImageView.FeedImageView;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem;
import zuts.bit.connect.Activities.HomeActivity.TimelineElements.DetailActivity;
import zuts.bit.connect.Activities.HomeActivity.TimelineElements.commentitem.CommentItem;
import zuts.bit.connect.Activities.ImageViewActivity;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

/**
 * Created by Devang on 2/1/2017.
 */

public class CommentListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<CommentItem> feedItems;
    private int postid;
    private Boolean opentimeline;
    ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();
    SessionManager sessionManager;

    public CommentListAdapter(Activity activity, List<CommentItem> feedItems, int postid){
        this.activity = activity;
        this.feedItems = feedItems;
        this.postid=postid;
        imageLoader = VolleyController.getInstance().getImageLoader();
        sessionManager=new SessionManager(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.comment_item, null);

        if (imageLoader == null)
            imageLoader = VolleyController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.commentuser);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.commenttime);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.commentbody);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.commentprofilePic);
        RelativeLayout baselayout= (RelativeLayout) convertView.findViewById(R.id.commentitemlayout);
        final CommentItem item = feedItems.get(position);



        name.setText(item.getUsername());


        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimestamp())*1000L,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

            statusMsg.setText(item.getComment());




        if (Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID)) == item.getUserid()) {

            baselayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    alertDialog.setTitle("Delete?");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want delete this?");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            String tag_string_req = "deletepost";
                            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UNCOMMENT_POST_FEED_URL, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e("Feed","Delete : "+response);

                                    try {

                                        JSONObject jObj = new JSONObject(response);
                                        boolean success = jObj.getBoolean("success");
                                        if (success) {
                                            // User successfully stored in MySQL
                                            // Now store the user in sqlite
                                            String ssuccess = jObj.getString("success");
                                            String smessage = jObj.getString("message");

                                            // Inserting row in users table
                                            //db.addUser(name, email, uid, created_at);

                                            LayoutInflater inflator= activity.getLayoutInflater();
                                            View layout=inflator.inflate(R.layout.custom_noification, null);
                                            ((TextView)layout.findViewById(R.id.custom_noti_text)).setText("Comment Deleted");
                                            Toast toast= new Toast(activity.getApplicationContext());
                                            toast.setView(layout);
                                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
                                            toast.setDuration(Toast.LENGTH_LONG);
                                            toast.show();

                                            feedItems.remove(position);
                                            notifyDataSetChanged();

                                        } else {
                                            // Error occurred in registration. Get the error
                                            // message
                                            String errorMsg = jObj.getString("message");
                                            Toast.makeText(activity.getApplicationContext(),
                                                    errorMsg, Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(activity.getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity.getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // Posting params to register url
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("commentid", String.valueOf(item.getId()));
                                    params.put("postid", String.valueOf(postid));
                                    return params;
                                }
                            };

                            // Adding request to request queue
                            VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);
                            Log.e("delete","uid : "+item.getId());
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                    return true;
                }
            });
        }


        // user profile pic
        profilePic.setImageUrl(item.getUserimg(), imageLoader);
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProfileTimelineActivity.class);
                    i.putExtra("userid", item.getUserid());
                    activity.startActivity(i);
                }
            });

        return convertView;
    }
}
