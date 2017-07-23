package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.SubjectMatter;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data.ListItem;
import zuts.bit.connect.Activities.ImageViewActivity;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.AppConfig;
import zuts.bit.connect.helper.SessionManager;
import zuts.bit.connect.helper.VolleyController;


public class ListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ListItem> listItems;
	SessionManager sessionManager;

	public static final String[] colorArray=new String[]{"#3498db","#e74c3c","#9b59b6","#e67e22"};

	public static int counter=0;
	int work;


	public ListAdapter(Activity activity, List<ListItem> listItems, int work) {
		this.activity = activity;
		this.listItems = listItems;
		this.work=work;
		sessionManager=new SessionManager(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int location) {
		return listItems.get(location);
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
			convertView = inflater.inflate(R.layout.subject_list, null);

		TextView subjectname = (TextView) convertView.findViewById(R.id.subjectname);
		TextView subjectcode = (TextView) convertView.findViewById(R.id.subjectcode);
		RelativeLayout baselayout=(RelativeLayout) convertView.findViewById(R.id.subjectlistlayout);
		final ListItem item = listItems.get(position);


		subjectname.setText(item.getSubjectname());
		subjectcode.setText(item.getSubjectcode());
		if (counter + 1 <= 3)
			counter+=1;
		else
			counter=0;
		baselayout.setBackgroundColor(Color.parseColor(colorArray[counter]));


		baselayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(work==0)
				{
					Intent i = new Intent(activity, SubjectMatter.class);
					i.putExtra("subjectid",item.getId());
					i.putExtra("subjectcode",item.getSubjectcode());
					i.putExtra("subjectname",item.getSubjectname());
					activity.startActivity(i);

				}

				if(work==1){
					Intent i = new Intent(activity, ImageViewActivity.class);
					i.putExtra("subjectid",item.getId());
					i.putExtra("subjectcode",item.getSubjectcode());
					i.putExtra("subjectname",item.getSubjectname());
					activity.startActivity(i);

				}
			}
		});

		return convertView;
	}

}
