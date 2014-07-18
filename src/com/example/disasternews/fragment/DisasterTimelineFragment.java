package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer.FrameCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.disasternews.DisasterDetailsActivity;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.fragment.BaseTimelineFragment.PopulateTimeline;
import com.example.disasternews.fragment.BaseTimelineFragment.DisasterEndlessScrollListener;
import com.example.disasternews.models.Disaster;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.disasternews.fragment.BaseTimelineFragment.BecameVisible;

public class DisasterTimelineFragment extends BaseTimelineFragment 
    implements PopulateTimeline, DisasterEndlessScrollListener, BecameVisible{

 
    public DisasterTimelineFragment() {
        // EXPLANATION
        // This fragment defines the populate() method which is called from
        // listener
        // However, the since this fragment is a child class and an implementation
        // we set the endless scroll listener in this child class because it has the 
        // definition for the actions needed by endless scrolling.
        this.disasterEndlessScrollListener = this;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateTimeline( true );
        
        if ( Disaster.getOrderedDisasters( countries ).size() == 0) {
        	Toast.makeText(getActivity(), "Calling the API", Toast.LENGTH_LONG).show();
        	populateTimeline( true );
        } else {
        	Log.d("Debug", "Loading from database inside DisasterTimelineFragment");
        	Toast.makeText(getActivity(), "Loading from database", Toast.LENGTH_LONG).show();
        	onRefreshFragment();
        }
    }
    
    
    /**
     * 
     */
    @Override
    public void populateTimeline( boolean newSettings ) {
//    	Log.d("Debug", "inside populateTimeline making API calls");
//    	Toast.makeText(getActivity(), "inside populateTimeline making API calls", Toast.LENGTH_LONG).show();
        client.getDisasters( newSettings, countries, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
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
                    client.incOffset(idList.size());
                } catch( JSONException e ) {
                    e.printStackTrace();
                    return;
                }
               
                client.getDisasterMaps(idList, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        try {
                            JSONArray dataArray = json.getJSONArray("data");
                            if ( dataArray.length() > 0 ) {
                                try {
                                    JSONObject item = dataArray.getJSONObject(0);
                                    String href = item.getString("href");
                                    
                                    // Log.d("DEBUG", "Getting href: " + href);
                                    
                                    client.getDisasterDetails(href, new JsonHttpResponseHandler() {
                                        public void onSuccess(JSONObject jsonDetail) {
                                            Integer id = 0;
                                            try {
                                                JSONArray detailsArray = jsonDetail.getJSONArray("data");
                                                if ( detailsArray.length() > 0 ) {
                                                    JSONObject fieldMapFirst = (JSONObject) detailsArray.get(0);
                                                    // Log.d("DEBUG", "fieldMapFirst: " + fieldMapFirst.toString());
                                                    
                                                    JSONObject fieldMapSecond = fieldMapFirst.getJSONObject("fields");
                                                    id = fieldMapSecond.getInt("id");
                                                    // Log.d("DEBUG", "fieldMapSecond: " + fieldMapSecond.toString() );
                                                    
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
                                                    
                                                    String originalName = disasterMap.getString("name");
                                                    String[] originalNameArray = originalName.split("-");
                                                    
                                                    String body = "";
                                                    if ( fieldMapSecond.has("body") ) {
                                                        body = fieldMapSecond.getString("body");
                                                    }
                                                    
                                                    Disaster disaster = null;
                                                    if (Disaster.getDisasterInfo(id) != null) {
                                                    	disaster = Disaster.getDisasterInfo(id);
                                                    } else {
                                                    	disaster = new Disaster( id,
                                                                fieldMapSecond.getString("url"),
                                                                originalNameArray[0],
                                                                body,
                                                                dateObject.getString("created"),
                                                                countryMap.getString("name"),
                                                                disasterTypeMap.getString("name"),
                                                                previewMap.getString("url-large"),
                                                                fieldMapSecond.getString("title")
                                                                );
                                                        disaster.save();
                                                    }
                                                    aDisasters.add(disaster);
                                                    
                                                    /*
                                                    for ( int i=0; i < countryArray.length(); i++ ) {
                                                        JSONObject tempCountryMap = (JSONObject) countryArray.get(i);
                                                        String countryName = tempCountryMap.getString("name");
                                                        
                                                        if ( countries.contains(countryName) ) {
                                                            Log.d("DEBUG", "adding: " + countryName );
                                                            
                                                            Disaster disaster = new Disaster( id,
                                                                    fieldMapSecond.getString("url"),
                                                                    originalNameArray[0],
                                                                    body,
                                                                    dateObject.getString("created"),
                                                                    countryName,
                                                                    disasterTypeMap.getString("name"),
                                                                    previewMap.getString("url-large"),
                                                                    fieldMapSecond.getString("title")
                                                                    );
                                                            disaster.save();
                                                        }
                                                    }
                                                    */
                                                                     
                                                    aDisasters.clear();
                                                    List<Disaster> orderedDisasters = Disaster.getOrderedDisasters( countries );
                                                    Log.d("DEBUG", "orderedDisasters size: " + orderedDisasters.size() );
                                                    addAll( new ArrayList<Disaster>(orderedDisasters) );
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
     * Method to create a new instance of this fragment
     * 
     * @return
     */
    public static DisasterTimelineFragment newInstance() {
        return new DisasterTimelineFragment();
    }


    /**
     * Implementing the interface defined in BaseTimelineFragment::DisasterEndlessScrollListener
     */
    @Override
    public void onLoadMore(int totalItemsCount) {
    	populateTimeline( false );
    }
    
    
    /**
     * Interface method to be called the the page is selected.
     * Pager in DisasterTimelineActivity shall call this.
     */
    @Override
    public void onRefreshFragment() {
//    	Toast.makeText(getActivity(), "calling fragmentBecameVisible inside DisasterTimeline", Toast.LENGTH_LONG).show();
        aDisasters.clear();
        List<Disaster> orderedDisasters = Disaster.getOrderedDisasters( countries );
        addAll( new ArrayList<Disaster>(orderedDisasters) );
//        Log.d("Debug", aDisasters.toString());
//        aDisasters.notifyDataSetChanged();
    }
}
