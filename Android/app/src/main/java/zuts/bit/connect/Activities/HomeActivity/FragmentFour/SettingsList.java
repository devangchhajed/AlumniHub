package zuts.bit.connect.Activities.HomeActivity.FragmentFour;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import zuts.bit.connect.R;

/**
 * Created by Devang on 10/7/2015.
 */
public class SettingsList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public SettingsList(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.settings_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.settings_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.setting_name);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.setting_thumbnail);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
