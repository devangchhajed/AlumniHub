package zuts.bit.connect.Activities.HomeActivity.FragmentFour;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.util.HashMap;

import zuts.bit.connect.Activities.AboutActivity;
import zuts.bit.connect.Activities.EditProfilePic;
import zuts.bit.connect.Activities.ProfileActivity;
import zuts.bit.connect.Activities.ProfileTimelineActivity;
import zuts.bit.connect.BuildConfig;
import zuts.bit.connect.R;
import zuts.bit.connect.helper.SessionManager;


public class FourFragment extends Fragment{

    SessionManager sessionManager;
    HashMap<String,String> userdetails;
    TextView username;

    ImageView userImg;
    Intent intent;
    public  static final int imageupdatesuccess  = 1 ;

    Context context;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressLint("ValidFragment")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_four, container, false);

        sessionManager=new SessionManager(getActivity().getApplicationContext());
        userdetails=sessionManager.getUserDetails();
        userImg=(ImageView) view.findViewById(R.id.fragment_four_user_img);
        username= (TextView) view.findViewById(R.id.fragment_four_username);

        username.setText(userdetails.get(sessionManager.KEY_NAME));

        Glide.with(getActivity().getApplicationContext()).load(userdetails.get(sessionManager.KEY_PHOTO_URL))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(userImg);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(getActivity(), EditProfilePic.class);
                getActivity().startActivityForResult(intent,imageupdatesuccess);

            }
        });


        final ListView listView = (ListView) view.findViewById(R.id.fragment_four_listview);

        LayoutInflater listinflater=getActivity().getLayoutInflater();
        ViewGroup footer= (ViewGroup) listinflater.inflate(R.layout.footer_setting,listView,false);
        TextView version=(TextView)footer.findViewById(R.id.setting_footer_version);
        version.setText("Version : "+ BuildConfig.VERSION_NAME);

        listView.addFooterView(footer,null,false);

        Integer imageDisp[] = {
                R.drawable.ic_profile_picture,
                R.drawable.ic_sms_settings,
                R.drawable.ic_help_settings
        };

        String[] values = new String[] {
                "Profile",
                "Feedback",
                "About"
        };

        SettingsList adapter = new SettingsList(getActivity(), values, imageDisp);
        listView.setAdapter(adapter);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        intent= new Intent(getActivity(), ProfileTimelineActivity.class);
                        getActivity().startActivity(intent);
                        break;
                    case 1:
                        intent= new Intent(getActivity(), FeedbackActivity.class);
                        getActivity().startActivity(intent);

                        break;
                    case 2:
                        intent= new Intent(getActivity(), AboutActivity.class);
                        getActivity().startActivity(intent);
                        break;

                }
            }
        });






        // Inflate the layout for this fragment
        return view;
    }


    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==imageupdatesuccess)
        {
            Glide.with(getActivity().getApplicationContext()).load(userdetails.get(sessionManager.KEY_PHOTO_URL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(userImg);
        }
    }

}
