package zuts.bit.connect.Activities.HomeActivity.FragmentThree.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import zuts.bit.connect.Activities.HomeActivity.FragmentThree.data.FeedItem;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;

public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	ImageLoader imageLoader = VolleyController.getInstance().getImageLoader();
	SessionManager sessionManager;

	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
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
			convertView = inflater.inflate(R.layout.notification_feed_item, null);

		if (imageLoader == null)
			imageLoader = VolleyController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.body);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.nimageview);

		final FeedItem item = feedItems.get(position);


		name.setText(item.getBody());
//		name.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//                Intent i = new Intent();
//                Bundle b = new Bundle();
//                b.putParcelable("FeedObj", (Parcelable) item);
//                i.putExtras(b);
//                i.setClass(activity, DetailActivity.class);
//
//                                activity.startActivity(i);
//            }
//		});

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong(item.getTime()) * 1000L,
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);


		profilePic.setImageUrl(item.getUserImage(), imageLoader);
		profilePic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, ProfileTimelineActivity.class);
				i.putExtra("userid", item.getUserId());
				activity.startActivity(i);
			}
		});

		return convertView;
	}
}
