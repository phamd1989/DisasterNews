package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.disasternews.fragment.BaseTimelineFragment;
import com.example.disasternews.fragment.DisasterTimelineFragment;
import com.example.disasternews.fragment.MyCollectionsTimelineFragment;
import com.example.disasternews.fragment.BaseTimelineFragment.BecameVisible;
import com.example.disasternews.models.Disaster;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.disasternews.R;

public class DisasterTimelineActivity extends FragmentActivity {

    private static final int REQUEST_CODE = 10;
    private final String DISASTER_TAB_TAG = "disaster";
    
    DisasterTimelineFragment disasterTimelineFragment = null;
    MyCollectionsTimelineFragment myCollectionsTimelineFragment = null;
    ReliefWebClient client;
    
    ArrayList<String> countries;
    ArrayList<String> types;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disaster_timeline);
		
		client = ReliefWebClient.getInstance();
        
        // Get the intent data
        countries = getIntent().getStringArrayListExtra("countries");
        
        
        // setupTabs();
        // IMPORTANT - create tabs after setting countries and types
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        final ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
        
        // To be executed when we are switching between pages
        vpPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int page) {
                BaseTimelineFragment fragment = (BaseTimelineFragment) adapter.instantiateItem(vpPager, page);
                if ( fragment != null && fragment instanceof MyCollectionsTimelineFragment ) {
                    MyCollectionsTimelineFragment my = (MyCollectionsTimelineFragment) fragment;
                    my.fragmentBecameVisible();
                }
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
	}
	

    /**
     * 
     */
	/*
    private void setupTabs() {
        // get action bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // tell action bar we want tabs
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab1 = actionBar
            .newTab()
            .setText("Home")
            .setIcon(R.drawable.ic_launcher)
            .setTag("DisasterTimelineFragment")
            .setTabListener(
                new FragmentTabListener<DisasterTimelineFragment>(R.id.flContainer, this, DISASTER_TAB_TAG,
                        DisasterTimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);
    }
    */
    
	public class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            if ( position == 0 ) {
                return "Disaster News";
            }
            else {
                return "My Collection";
            }
        }

        /**
         * This code is called only once for initialization.
         * 
         */
        @Override
        public Fragment getItem(int position) {
            Fragment result = null;
            
            if (position == 0) {
                if ( disasterTimelineFragment == null ) {
                    disasterTimelineFragment = DisasterTimelineFragment.newInstance();
                    disasterTimelineFragment.setCountries(countries);
                    disasterTimelineFragment.setTypes(types);
                }
                result = disasterTimelineFragment;

            }
            else {
                if ( myCollectionsTimelineFragment == null ) {
                    myCollectionsTimelineFragment = MyCollectionsTimelineFragment.newInstance();
                }
                
                result = myCollectionsTimelineFragment;
            }
            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }
        
    }
    
}
