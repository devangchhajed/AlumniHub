package zuts.bit.connect;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ForceUpdate extends AppCompatActivity {

    TextView textview;
    Button button;
    int version;
    String features;
    Boolean forceupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_update);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            try {
                JSONObject jsonObject = new JSONObject(extras.getString("response"));

                version = jsonObject.getInt("version");
                features = jsonObject.getString("newfeatures");
                forceupdate = jsonObject.getBoolean("forceupdate");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textview = (TextView) findViewById(R.id.updateinfo);

            textview.setText("Version " + version + " has been released of this app. Please update this app to continue using it.");

            button = (Button) findViewById(R.id.updatebutton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=zuts.bit.connect"));
                    startActivity(intent);
                }
            });
        }
    }
}
