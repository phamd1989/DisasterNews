/**
 * 
 */
package com.example.disasternews.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.example.disasternews.DisasterArrayAdapter;
import com.example.disasternews.DisasterDetailsActivity;
import com.example.disasternews.DisasterNewsClient;
import com.example.disasternews.DiasterNewsApplication;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.ReliefWebClient;
import com.example.disasternews.models.Disaster;

/**
 *
 */
public abstract class BaseTimelineFragment extends Fragment {

    protected ArrayList<Disaster> disasters;
    protected DisasterArrayAdapter aDisasters;
    protected ListView lvDisasters;
    protected DisasterEndlessScrollListener disasterEndlessScrollListener;
    protected ReliefWebClient client = null;
    protected DisasterNewsClient twitterClient = null;
    
    public static BaseTimelineFragment btf = null;
    protected ArrayList<String> countries;
    protected ArrayList<String> types;
    
    
    /**
     * 
     */
    public BaseTimelineFragment() {
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
                disasterEndlessScrollListener.onLoadMore(totalItemsCount);
            }
        });
        
        lvDisasters.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent i = new Intent(getActivity(), DisasterDetailsActivity.class);
                int disasterId = disasters.get(position).getDisasterId();
                String disasterName = disasters.get(position).getName();
                i.putExtra("disasterId", disasterId);
                i.putExtra("disasterName", disasterName);
                startActivity(i);
            }
            
        });
        
        
        lvDisasters.setOnItemLongClickListener( new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // Toast.makeText(getActivity(), disasters.get(position).getName(), Toast.LENGTH_LONG).show();
                
                // Intent i = new Intent(getActivity(), DisasterDetailsActivity.class);
                
                Disaster disaster = disasters.get(position);
                disaster.setFavorite(! disaster.getFavorite() );
                disaster.save();
                
                disasters.remove(position);
                aDisasters.notifyDataSetChanged();
                return false;
            }
        });
        return v;
    }
    
    /**
     * All fragments must implement this timeline method
     *
     */
    public interface PopulateTimeline {
        public void populateTimeline( boolean newSettings );
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
     * This shall be called when the fragment is visible.
     * The activity shall set the pager listeners.
     */
    public interface BecameVisible {
        public void fragmentBecameVisible();
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
        if ( client != null ) {
            client.setOffset( (long) 0 );
        }
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

    /**
     * Delegate adding to the internal adapter
     * 
     * @param disasters
     */
    public void addAll( ArrayList<Disaster> disasters ) {
        aDisasters.addAll(disasters);
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        disasters = new ArrayList<Disaster>();
        aDisasters = new DisasterArrayAdapter(getActivity(), disasters);
        
        client = ReliefWebClient.getInstance();
        twitterClient = DiasterNewsApplication.getRestClient();
    }
    

    
}
