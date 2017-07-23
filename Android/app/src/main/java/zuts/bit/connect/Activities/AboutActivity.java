package zuts.bit.connect.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import zuts.bit.connect.BuildConfig;
import zuts.bit.connect.R;

public class AboutActivity extends AppCompatActivity {

    TextView versioncode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        versioncode= (TextView) findViewById(R.id.about_versioncode);
        versioncode.setText("Version : "+BuildConfig.VERSION_NAME);


    }
}
