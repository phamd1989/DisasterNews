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

import com.example.disasternews.fragment.DisasterTimelineFragment;
import com.example.disasternews.fragment.DisasterTimelineFragment.DisasterTimelineFragmentInterface;
import com.example.disasternews.models.Disaster;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.disasternews.R;

public class DisasterTimelineActivity extends FragmentActivity implements DisasterTimelineFragmentInterface{

    private static final int REQUEST_CODE = 10;
    private final String DISASTER_TAB_TAG = "disaster";
    
    DisasterTimelineFragment disasterTimelineFragment = null;
    ReliefWebClient client;
    
    ArrayList<String> countries;
    ArrayList<String> types;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disaster_timeline);
		
		client = ReliefWebClient.getInstance();
        
        // Get the intent data
<<<<<<< HEAD
        ArrayList<String> countriesAndTypes = getIntent().getStringArrayListExtra("countries_and_types");
        countries = new ArrayList<String>();
        types = new ArrayList<String>();
=======
        ArrayList<String> countriesAndTypes = getIntent().getStringArrayListExtra("countries");
        ArrayList<String> countries = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
>>>>>>> 2f8df5f0e519dd8f5848595631158802e9b0ad4e
        
        boolean fillCountry = true;
        for ( String str : countriesAndTypes ) {
            if ( str.equalsIgnoreCase(" ") ) {
                fillCountry = false;
                continue;
            }
            
            if ( fillCountry ) {
                Log.d("DEBUG", "adding " + str + " to countries");
                countries.add(str);
            }
            else {
                Log.d("DEBUG", "adding " + str + " to types");
                types.add(str);
            }
        }
        
        // setupTabs();
        // IMPORTANT - create tabs after setting countries and types
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
                return "My Collection";
            }
        }

        @Override
        public Fragment getItem(int position) {
            Fragment result = null;
            
            
            if (position == 0) {
                
                if ( disasterTimelineFragment == null ) {
                    disasterTimelineFragment = DisasterTimelineFragment.newInstance();
                    disasterTimelineFragment.setCountries(countries);
                    disasterTimelineFragment.setTypes(types);
                    /*
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    disasterTimelineFragment = DisasterTimelineFragment.newInstance();
                    ft.replace(R.id.vpPager, disasterTimelineFragment);
                    ft.commit();
                    */
                }
                result = disasterTimelineFragment;

            }
            else {
                // TODO
                // MY collections
                //if ( disasterTimelineFragment == null ) {
                    disasterTimelineFragment = DisasterTimelineFragment.newInstance();
                //}
                result = disasterTimelineFragment;
            }
            return result;
        }

        @Override
        public int getCount() {
            return 1;
        }
        
    }
    
}
