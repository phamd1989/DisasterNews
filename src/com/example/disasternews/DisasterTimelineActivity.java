package com.example.disasternews;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.disasternews.fragment.DisasterTimelineFragment;
import com.example.disasternews.listener.FragmentTabListener;

public class DisasterTimelineActivity extends FragmentActivity {

    private static final int REQUEST_CODE = 10;
    private final String DISASTER_TAB_TAG = "disaster";
    
    DisasterTimelineFragment disasterTimelineFragment;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disaster_timeline);
		
		// setupTabs();
		
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
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
                return "Favorites";
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DisasterTimelineFragment();
            }
            
            return new DisasterTimelineFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
        
    }
    
}
