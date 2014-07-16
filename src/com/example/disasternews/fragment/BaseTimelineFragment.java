/**
 * 
 */
package com.example.disasternews.fragment;

import java.util.ArrayList;

import com.example.disasternews.DisasterArrayAdapter;
import com.example.disasternews.DisasterDetailsActivity;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.ReliefWebClient;
import com.example.disasternews.models.Disaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 *
 */
public abstract class BaseTimelineFragment extends Fragment {

    protected ArrayList<Disaster> disasters;
    protected DisasterArrayAdapter aDisasters;
    protected ListView lvDisasters;
    protected DisasterEndlessScrollListener disasterEndlessScrollListener;
    protected ReliefWebClient client = null;
    
    public static BaseTimelineFragment btf = null;
    protected ArrayList<String> countries;
    protected ArrayList<String> types;
    
    
    /**
     * 
     */
    public BaseTimelineFragment() {
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
    }
    

    
}
