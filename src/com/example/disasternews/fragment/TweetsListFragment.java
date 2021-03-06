package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.disasternews.DiasterNewsApplication;
import com.example.disasternews.DisasterNewsClient;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.R;
import com.example.disasternews.TweetArrayAdapter;
import com.example.disasternews.models.Tweet;

public abstract class TweetsListFragment extends Fragment {
	protected DisasterNewsClient client;
	protected TweetArrayAdapter aTweets;
	protected ListView lvTweets;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// non-view initialization
		client = DiasterNewsApplication.getRestClient();
		aTweets = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// assign our view preferences, get all views
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		// still don't know why disabling the populateTimeline()
		// works for BasicTwitter, but not here.
		// my theory is that the onScrollListener would make the 1st call
		// to populate the timeline
		populateTimeline(null);
		lvTweets.setOnScrollListener(populateEndlessScrollListener(null));
		
		return v;
	}
	
	public void addAll(List<Tweet> tweets) {
		aTweets.addAll(tweets);
	}
	
	public abstract void populateTimeline(String query);
	public abstract EndlessScrollListener populateEndlessScrollListener(String screen_name);
	
}
