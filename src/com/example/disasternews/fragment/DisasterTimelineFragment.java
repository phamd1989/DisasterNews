package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.disasternews.DisasterArrayAdapter;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.ReliefWebClient;
import com.example.disasternews.models.Disaster;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DisasterTimelineFragment extends Fragment {

    protected ArrayList<Disaster> disasters;
    protected DisasterArrayAdapter aDisasters;
    protected ListView lvDisasters;
    protected DisasterEndlessScrollListener disasterEndlessScrollListener;
    protected ReliefWebClient client;
    
    public static DisasterTimelineFragment dtf = null;
    protected ArrayList<String> countries;
    protected ArrayList<String> types;
    
    public interface DisasterTimelineFragmentInterface {
        
    }
    
    /**
     * Activity must define this interface.
     * In this Fragment, we're calling the method from this interface.
     * 
     * The TimelineActivity will implement this interface and allows the fragment
     * to communicate events to the Activity.
     *
     */
    public interface DisasterEndlessScrollListener {
        public void onLoadMore( int totalItemsCount );
    }
    
    /**
     * @return the countries
     */
    public ArrayList<String> getCountries() {
        return countries;
    }

    /**
     * @param countries the countries to set
     */
    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }

    /**
     * @return the types
     */
    public ArrayList<String> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        disasters = new ArrayList<Disaster>();
        aDisasters = new DisasterArrayAdapter(getActivity(), disasters);
        
        client = ReliefWebClient.getInstance();
        Log.d("DEBUG", "Disaster Timeline Fragment created here!!!!!!!!!!!!!!");
        populateTimeline();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        
        View v = inflater.inflate(R.layout.fragment_disasters_list, container, false);
        
        // Assign our view references
        lvDisasters = (ListView) v.findViewById( R.id.lvDisasters );
        lvDisasters.setAdapter(aDisasters);
        
        lvDisasters.setOnScrollListener( new EndlessScrollListener() {
            // Triggered only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView
            @Override
            public void onLoadMore(int totalItemsCount) {
                // populateTimeline();
                
                // disasterEndlessScrollListener.onLoadMore(totalItemsCount);
            }
        });
        return v;
    }
    
    
    /**
     * Delegate adding to the internal adapter
     * 
     * @param disasters
     */
    public void addAll( ArrayList<Disaster> disasters ) {
        aDisasters.addAll(disasters);
    }
    
    
    /**
     * 
     */
    public void populateTimeline() {
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
                                                    aDisasters.add(disaster);
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

    public static DisasterTimelineFragment newInstance() {
        dtf = new DisasterTimelineFragment();
        
        return dtf;
    }
    
    
}
