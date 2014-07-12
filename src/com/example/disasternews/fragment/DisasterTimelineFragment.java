package com.example.disasternews.fragment;

import java.util.ArrayList;

import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.disasternews.DisasterArrayAdapter;
import com.example.disasternews.ReliefWebClient;
import com.example.disasternews.models.Disaster;

public class DisasterTimelineFragment extends Fragment {

    protected ArrayList<Disaster> disasters;
    protected DisasterArrayAdapter aDisasters;
    protected ListView lvDisasters;
    protected DisasterEndlessScrollListener disasterEndlessScrollListener;
    

    
    public interface DisasterTimelineFragmentInterface {
        
    }
    
    public interface DisasterEndlessScrollListener {
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        populateTimeline();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        
        // TODO
        // set listview
        // set listview adapater
        
        View view = inflater.inflate(R.layout.activity_list_item, container, false);
        return view;
    }
    
    
    /**
     * 
     */
    public void populateTimeline() {
    }
    
    
}
