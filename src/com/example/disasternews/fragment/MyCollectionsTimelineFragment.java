/**
 * 
 */
package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.fragment.BaseTimelineFragment.BecameVisible;
import com.example.disasternews.fragment.BaseTimelineFragment.DisasterEndlessScrollListener;
import com.example.disasternews.fragment.BaseTimelineFragment.PopulateTimeline;
import com.example.disasternews.models.Disaster;

/**
 *
 */
public class MyCollectionsTimelineFragment extends BaseTimelineFragment 
    implements PopulateTimeline, DisasterEndlessScrollListener, BecameVisible{

    /**
     * 
     */
    public MyCollectionsTimelineFragment() {
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
        
        populateTimeline( false );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        
        View v = inflater.inflate(R.layout.fragment_mycollections_list, container, false);
        
        // Assign our view references
        lvDisasters = (ListView) v.findViewById( R.id.lvMyCollection );
        lvDisasters.setAdapter(aDisasters);
        
        lvDisasters.setOnScrollListener( new EndlessScrollListener() {
            // Triggered only when new data needs to be appended to the list
            // Add whatever code is needed to append new items to your AdapterView
            @Override
            public void onLoadMore(int totalItemsCount) {
                disasterEndlessScrollListener.onLoadMore(totalItemsCount);
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
     * Method to create a new instance of this fragment
     * 
     * @return
     */
    public static MyCollectionsTimelineFragment newInstance() {
        return new MyCollectionsTimelineFragment();
    }

    
    /**
     * Populate the timeline with favorite disasters
     */
    @Override
    public void populateTimeline( boolean newSettings ) {
        aDisasters.clear();
        List<Disaster> favoriteDisasters = Disaster.getFavoriteDisasters();
        addAll( new ArrayList<Disaster>(favoriteDisasters) );
    }
    
    /**
     * Interface method to be called to populate the timeline.
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
    public void fragmentBecameVisible() {
        populateTimeline( false );
    }
    
}
