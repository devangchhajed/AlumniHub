package zuts.bit.connect.Activities.HomeActivity.FragmentOne.adapter;

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
import zuts.bit.connect.Activities.HomeActivity.TimelineElements.DetailActivity;
import zuts.bit.connect.Activities.HomeActivity.FragmentOne.FeedImageView.FeedImageView;

import zuts.bit.connect.Activities.HomeActivity.FragmentOne.data.FeedItem;
import zuts.bit.connect.Activities.ImageViewActivity;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;


public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private Boolean opentimeline;
    Boolean postlike = false;
    ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();
    SessionManager sessionManager;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems, Boolean bool) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.opentimeline = bool;
        imageLoader = VolleyController.getInstance().getImageLoader();
        sessionManager = new SessionManager(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
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
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = VolleyController.getInstance().getImageLoader();

        final RelativeLayout countlayout = (RelativeLayout) convertView.findViewById(R.id.countlayout);
        final TextView name = (TextView) convertView.findViewById(R.id.name);
        final TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        final TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        final TextView likecount = (TextView) convertView.findViewById(R.id.likecount);
        final TextView commentcount = (TextView) convertView.findViewById(R.id.commentcount);
        final Button like = (Button) convertView.findViewById(R.id.like);
        final Button comment = (Button) convertView.findViewById(R.id.comment);
        final TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        final NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        final FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);
        final LinearLayout base = (LinearLayout) convertView.findViewById(R.id.feed_base);

        final FeedItem item = feedItems.get(position);
        final int flikes = item.getLikes();
        final int fcomments = item.getComments();


        name.setText(item.getName());


        if (flikes > 0 || fcomments > 0) {
            countlayout.setVisibility(View.VISIBLE);
            likecount.setText(flikes + " Likes");
            commentcount.setText(fcomments + " Comments");
        }

        if (item.getUserLike() == 1) {
            postlike = true;
            like.setText("Unlike");
        } else {
            postlike = false;
            like.setText("Like");
        }

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(activity.getApplicationContext(),feedItems.get(position).getId()+"-"+position,Toast.LENGTH_LONG).show();
                Log.e("fla", feedItems.get(position).getId() + "");
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra("feed", feedItems.get(position).getId());
                activity.startActivity(i);
            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countlayout.setVisibility(View.VISIBLE);

                Log.e("FeedListAdapter", like.getText().toString() + " --- " + item.getUserLike());

                if (item.getUserLike() != 1) {

                    item.setLikes(item.getLikes() + 1);
                    likecount.setText(item.getLikes() + " Likes");
                    like.setText("Unlike");
                    like.setEnabled(false);

                    final String tag_string_req = "likepost";
                    StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.LIKE_POST_FEED_URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("Feed", "Delete : " + response);

                            try {

                                JSONObject jObj = new JSONObject(response);
                                boolean success = jObj.getBoolean("success");
                                if (success) {
                                    item.setUserLike(1);
                                    like.setEnabled(true);

                                } else {
                                    // Error occurred in registration. Get the error
                                    // message
                                    Log.e(tag_string_req, "Error");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("postid", String.valueOf(feedItems.get(position).getId()));
                            params.put("userid", String.valueOf(Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID))));
                            return params;
                        }
                    };

                    // Adding request to request queue
                    VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);




                } else {
                    item.setLikes(item.getLikes() - 1);
                    likecount.setText(item.getLikes() + " Likes");

                    like.setText("Like");
                    like.setEnabled(false);

                    final String tag_string_req = "unlikepost";
                    StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.UNLIKE_FEED_URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("Feed", "Delete : " + response);

                            try {

                                JSONObject jObj = new JSONObject(response);
                                boolean success = jObj.getBoolean("success");
                                if (success) {
                                    item.setUserLike(0);
                                    like.setEnabled(true);
                                } else {
                                    // Error occurred in registration. Get the error
                                    // message
                                    Log.e(tag_string_req, "ERROR");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("postid", String.valueOf(feedItems.get(position).getId()));
                            params.put("userid", String.valueOf(Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID))));
                            return params;
                        }
                    };

                    // Adding request to request queue
                    VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);



                }
                Log.e("FeedListAdapter", like.getText().toString() + " - " + item.getUserLike());

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra("feed", feedItems.get(position).getId());
                activity.startActivity(i);
            }
        });

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()) * 1000L,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            final String furl = item.getUrl();
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BrowserActivity.class);
                    intent.putExtra("url", furl);
                    activity.startActivity(intent);
                }
            });
//			url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }


        if (Integer.parseInt(sessionManager.getUserDetails().get(sessionManager.KEY_USERID)) == item.getUserId()) {

            base.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Log.e("Feedlistadapter", "" + sessionManager.getUserDetails().get(sessionManager.KEY_USERID) + item.getUserId());
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                    // Setting Dialog Title
                    alertDialog.setTitle("Delete?");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want delete this?");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String tag_string_req = "deletepost";
                            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.DELETE_POST_FEED_URL, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.e("Feed", "Delete : " + response);

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

                                            LayoutInflater inflator = activity.getLayoutInflater();
                                            View layout = inflator.inflate(R.layout.custom_noification, null);
                                            ((TextView) layout.findViewById(R.id.custom_noti_text)).setText("Post Deleted");
                                            Toast toast = new Toast(activity.getApplicationContext());
                                            toast.setView(layout);
                                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
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
                                        Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // Posting params to register url
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", String.valueOf(feedItems.get(position).getId()));
                                    return params;
                                }
                            };

                            // Adding request to request queue
                            VolleyController.getInstance().addToRequestQueue(strReq, tag_string_req);
                            Log.e("delete", "uid : " + item.getUserId());
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
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);
        if (opentimeline) {
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ProfileTimelineActivity.class);
                    i.putExtra("userid", item.getUserId());
                    activity.startActivity(i);
                }
            });
        }

        // Feed image
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                @Override
                public void onError() {
                }

                @Override
                public void onSuccess() {
                }
            });
            feedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ImageViewActivity.class);
                    i.putExtra("imgurl", item.getImge() + "");
                    i.putExtra("status", item.getStatus() + "");
                    i.putExtra("username", item.getName() + "");
                    i.putExtra("timestamp", DateUtils.getRelativeTimeSpanString(Long.parseLong(item.getTimeStamp()) * 1000L, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
                    activity.startActivity(i);
                }
            });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void likePost(final int postid, final int userid) {

    }

    public void unlikePost(final int postid, final int userid) {

    }

}
