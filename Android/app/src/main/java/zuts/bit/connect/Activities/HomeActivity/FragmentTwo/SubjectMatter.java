package zuts.bit.connect.Activities.HomeActivity.FragmentTwo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.subjectmatterfragments.Papers;
import zuts.bit.connect.Activities.HomeActivity.FragmentTwo.subjectmatterfragments.Resources;
import zuts.bit.connect.R;

public class SubjectMatter extends AppCompatActivity {

    int id;
    String name, code;

    TextView scode, sname;
    Button paper, res;
    ImageButton close;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_matter);

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){
            id=bundle.getInt("subjectid");
            name=bundle.getString("subjectname");
            code=bundle.getString("subjectcode");
        }
//
//        scode= (TextView) findViewById(R.id.sm_code);
//        sname= (TextView) findViewById(R.id.sm_name);
//        paper= (Button) findViewById(R.id.sm_papers);
//        res= (Button) findViewById(R.id.sm_resources);
//        close= (ImageButton) findViewById(R.id.sm_close);
//
//        scode.setText(code);
//        sname.setText(name);
//
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        paper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(SubjectMatter.this, PaperResourceActivity.class);
//                i.putExtra("subjectid",id);
//                i.putExtra("subjectcode",code);
//                i.putExtra("subjectname",name);
//                i.putExtra("content","papers");
//
//                startActivity(i);
//
//            }
//        });
//
//        res.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SubjectMatter.this, "Feature coming soon", Toast.LENGTH_SHORT).show();
//            }
//        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Papers(), "Papers");
        adapter.addFragment(new Resources(), "Resources");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}