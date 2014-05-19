package com.facebook.samples.hellofacebook;

import com.facebook.samples.hellofacebook.TabsPagerAdapter;

import com.facebook.samples.hellofacebook.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private static String tag = "MainActivity";
    // Tab titles
    private String[] tabs = { "Profile", "Challenges", "Local", "Competitors", "Settings" };
    private int y1, y2; //used to detect up and down swipes
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.i(tag, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("here1", "1");
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        Log.d("here2", "2");
        //actionBar.setGravity(Gravity.BOTTOM);
        actionBar.setHomeButtonEnabled(false);
        Log.d("here21", "21");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);   
        Log.d("here22", "22");
        actionBar.setDisplayShowHomeEnabled(false);
        Log.d("here23", "23");
        actionBar.setDisplayShowTitleEnabled(false);
        Log.d("here3", "3");
        // Adding Tabs
        for (String tab_name : tabs) {
            /*actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));*/
        	actionBar.addTab(actionBar.newTab().setIcon(getIcon(tab_name)).setTabListener(this));
        }
        Log.d("here4", "4");
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        //if not logged in then promp them to login page
        if(savedInstanceState == null){
        	Log.d("here7", "7");
        	Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        	//i.putExtra("id", position);
        	startActivity(i);
        }
        
        //setting swipe gestures
        
        
    }
 
    private int getIcon(String tab_name) {
    	Log.i(tag, "getIcon");

		if(tab_name.equals("Profile")){
			return R.drawable.id;
		} else if(tab_name.equals("Challenges")){
			return R.drawable.line_globe;
		} else if(tab_name.equals("Local")){
			return R.drawable.connections;
		} else if(tab_name.equals("Competitors")){
			return R.drawable.multi_agents;
		}else if(tab_name.equals("Settings")){
			return R.drawable.configuration;
		}else {
			return R.drawable.star;
		}
	}

	@Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	Log.i(tag, "onTabSelected");
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

}
