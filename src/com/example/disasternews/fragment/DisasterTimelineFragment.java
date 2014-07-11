package com.example.disasternews.fragment;

import android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DisasterTimelineFragment extends Fragment {

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
}
