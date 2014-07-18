/**
 * 
 */
package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

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
//        Toast.makeText(getActivity(), "Creating MyCollection view", Toast.LENGTH_LONG).show();
        populateTimeline( false );
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
    public void onRefreshFragment() {
//    	Toast.makeText(getActivity(), "calling fragmentBecameVisible inside MyCollection", Toast.LENGTH_LONG).show();
        populateTimeline( false );
    }
    
}
