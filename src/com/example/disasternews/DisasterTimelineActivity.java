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
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.disasternews.fragment.DisasterTimelineFragment;
import com.example.disasternews.fragment.DisasterTimelineFragment.DisasterTimelineFragmentInterface;
import com.example.disasternews.models.Disaster;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DisasterTimelineActivity extends FragmentActivity implements DisasterTimelineFragmentInterface{

    private static final int REQUEST_CODE = 10;
    private final String DISASTER_TAB_TAG = "disaster";
    
    DisasterTimelineFragment disasterTimelineFragment;
    ReliefWebClient client;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disaster_timeline);
		
		client = new ReliefWebClient();
		
		// setupTabs();
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
        
        // Get the intent data
        ArrayList<String> countriesAndTypes = getIntent().getStringArrayListExtra("countries_and_types");
        ArrayList<String> countries = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        
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
        
        client.getDisasters( countries, types, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                Log.d("DEBUG", json.toString());
                
                // create list of IDs
                List<String> idList;
                try {
                    idList = new ArrayList<String>();
                    JSONArray dataArray = json.getJSONArray("data");
                    for ( int i=0; i < dataArray.length(); i++ ) {
                        JSONObject item = null;
                        try {
                            item = dataArray.getJSONObject(i);
                            String id = item.getString("id");
                            // Log.d("DEBUG", "Adding id: " + id);
                            idList.add(id);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    
                } catch( JSONException e ) {
                    e.printStackTrace();
                    return;
                }
               
                client.getDisasterMaps(idList, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        Log.d("DEBUG", json.toString());
                        
                        try {
                            JSONArray dataArray = json.getJSONArray("data");
                            if ( dataArray.length() > 0 ) {
                                try {
                                    JSONObject item = dataArray.getJSONObject(0);
                                    String href = item.getString("href");
                                    Log.d("DEBUG", "Getting href: " + href);
                                    client.getDisasterDetails(href, new JsonHttpResponseHandler() {
                                        public void onSuccess(JSONObject jsonDetail) {
                                            Integer id = 0;
                                            try {
                                                JSONArray detailsArray = jsonDetail.getJSONArray("data");
                                                if ( detailsArray.length() > 0 ) {
                                                    JSONObject fieldMapFirst = (JSONObject) detailsArray.get(0);
                                                    Log.d("DEBUG", "fieldMapFirst: " + fieldMapFirst.toString());
                                                    
                                                    JSONObject fieldMapSecond = fieldMapFirst.getJSONObject("fields");
                                                    id = fieldMapSecond.getInt("id");
                                                    Log.d("DEBUG", "fieldMapSecond: " + fieldMapSecond.toString() );
                                                    
                                                    // If contents do not have the "file" field, then it is useless.
                                                    JSONArray fileArray = new JSONArray();
                                                    JSONObject fileMap = new JSONObject();
                                                    JSONObject previewMap = new JSONObject();
                                                    if ( fieldMapSecond.has("file") ) {
                                                        fileArray = fieldMapSecond.getJSONArray("file");
                                                        fileMap = (JSONObject) fileArray.get(0);
                                                        previewMap = fileMap.getJSONObject("preview");
                                                    }
                                                    else {
                                                        Log.d("WARN", "ID: " + id.toString() 
                                                                + " does not have file... SKIPPING. ");
                                                        return;
                                                    }
                                                    
                                                    JSONObject dateObject = fieldMapSecond.getJSONObject("date");
                                                    JSONArray countryArray = fieldMapSecond.getJSONArray("country");
                                                    JSONObject countryMap = (JSONObject) countryArray.get(0);
                                                    JSONArray disasterArray = fieldMapSecond.getJSONArray("disaster");
                                                    JSONObject disasterMap = (JSONObject) disasterArray.get(0);
                                                    JSONArray disasterTypeArray = disasterMap.getJSONArray("type");
                                                    JSONObject disasterTypeMap = (JSONObject) disasterTypeArray.get(0);
                                                    
                                                    String body = "";
                                                    if ( fieldMapSecond.has("body") ) {
                                                        body = fieldMapSecond.getString("body");
                                                    }
                                                    
                                                    Disaster disaster = new Disaster( id,
                                                                                      fieldMapSecond.getString("url"),
                                                                                      disasterMap.getString("name"),
                                                                                      body,
                                                                                      dateObject.getString("created"),
                                                                                      countryMap.getString("name"),
                                                                                      disasterTypeMap.getString("name"),
                                                                                      previewMap.getString("url-large"),
                                                                                      fieldMapSecond.getString("title")
                                                            );
                                                    disaster.save();
                                                    Log.d("DEBUG", "disaster model: " + disaster.toString() );
                                                }
                                                
                                            }
                                            catch ( JSONException e ) {
                                                Log.d("DEBUG", "EXCEPTION id: " + id + " " + e.getMessage() );
                                                return;
                                            }
                                        }
                                        @Override
                                        public void onFailure(Throwable e, String s) {
                                            Log.d("ERROR", e.toString() );
                                            Log.d("ERROR", s);
                                        }
                                        
                                        @Override
                                        protected void handleFailureMessage(Throwable e, String s) {
                                            Log.d("ERROR", e.toString() );
                                            Log.d("ERROR", s);
                                        }
                                    });
                                } catch ( Exception e ) {
                                    e.printStackTrace();
                                }
                            }
                        } catch( JSONException e ) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    
                    @Override
                    public void onFailure(Throwable e, String s) {
                        Log.d("ERROR", e.toString() );
                        Log.d("ERROR", s);
                    }
                    
                    @Override
                    protected void handleFailureMessage(Throwable e, String s) {
                        Log.d("ERROR", e.toString() );
                        Log.d("ERROR", s);
                    }
                });
            }
            
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("ERROR", e.toString() );
                Log.d("ERROR", s);
            }
            
            @Override
            protected void handleFailureMessage(Throwable e, String s) {
                Log.d("ERROR", e.toString() );
                Log.d("ERROR", s);
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

        @Override
        public Fragment getItem(int position) {
            Fragment result = null;
            
            
            if (position == 0) {
                disasterTimelineFragment = new DisasterTimelineFragment();
                result = disasterTimelineFragment;
            }
            else {
                // TODO
                // MY collections
                disasterTimelineFragment = new DisasterTimelineFragment();
                result = disasterTimelineFragment;
            }
            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }
        
    }
    
}
