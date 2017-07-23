package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.SubjectMatter;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data.ListItem;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.data.PaperListItem;
import zuts.bit.connect.Activities.ImageViewActivity;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.SessionManager;


public class PaperListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<PaperListItem> listItems;
	SessionManager sessionManager;

	public static final String[] colorArray=new String[]{"#3498db","#e74c3c","#9b59b6","#e67e22"};

	public static int counter=0;
	int work;


	public PaperListAdapter(Activity activity, List<PaperListItem> listItems, int work) {
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
		final PaperListItem item = listItems.get(position);


		subjectname.setText(item.getYear()+" - "+item.getExam());
		subjectcode.setText(item.getSub());
		if (counter + 1 <= 3)
			counter+=1;
		else
			counter=0;
		baselayout.setBackgroundColor(Color.parseColor(colorArray[counter]));


		baselayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent i=new Intent(activity,ImageViewActivity.class);
				i.putExtra("imgurl", item.getUrl()+"");
				i.putExtra("status", item.getSub()+" "+item.getExam());
				i.putExtra("username", item.getUName()+"");
				i.putExtra("timestamp", item.getYear());
				activity.startActivity(i);

			}
		});

		return convertView;
	}

}
