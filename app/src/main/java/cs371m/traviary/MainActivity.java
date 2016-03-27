package cs371m.traviary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    protected ViewPagerAdapter vp_adapter;
    SlidingTabLayout tabs;
    ViewPager vp_pager;
    private CharSequence Titles[]={"Home","United States","World","Challenges"};
    int Numboftabs = 4;


    /* Did the user sign out?*/
    private boolean log_off;

    private HashMap<String,Boolean> countries;
    private HashMap<String,Boolean> states;
    private HashMap<String,Boolean> challenges;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Attaching the layout to the toolbar object */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        vp_adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs, this);
        vp_pager = (ViewPager) findViewById(R.id.pager);
        vp_pager.setAdapter(vp_adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        // To make the Tabs Fixed set this true
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.logoPink);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(vp_pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        // TODO Auto-generated method stub

                        getSupportActionBar().setTitle(Titles[position]);

                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPageScrollStateChanged(int pos) {
                        // TODO Auto-generated method stub

                    }
                });

        log_off = true;
        if (log_off) {
            Intent login_intent = new Intent(this,LoginActivity.class);
            startActivity(login_intent);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeTitle(String s) {
        getSupportActionBar().setTitle(s);
    }
}