package com.example.disasternews.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.activeandroid.util.Log;
import com.example.disasternews.EndlessScrollListener;
import com.example.disasternews.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RelatedTweetsFragment extends TweetsListFragment{
	
	private String disasterName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disasterName = getArguments().getString("disasterName");

//		if (screenName != null && !Tweet.getTweetsForScreenName(screenName).isEmpty()) {
//			Toast.makeText(getActivity(), "LOADING FROM DATABASE", Toast.LENGTH_LONG).show();
//			addAll(Tweet.getTweetsForScreenName(screenName));
//		}
		
	}
	@Override
	public void populateTimeline(String searchQuery) {
		long maxId = 0;
		if (!aTweets.isEmpty()) {
			maxId = aTweets.getItem(aTweets.getCount() - 1).getUid() - 1;
		}
		client.getSearchEvents(disasterName, maxId, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject jsonObj) {
				try {
					Log.d(jsonObj.toString());
					JSONArray statuses = jsonObj.getJSONArray("statuses");
					addAll(Tweet.fromJsonArray(statuses));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
			
			@Override
            protected void handleFailureMessage(Throwable e, String s) {
                Log.d("ERROR", e.toString() );
                Log.d("ERROR", s);
            }
		});
	}

	@Override
	public EndlessScrollListener populateEndlessScrollListener(
			final String searchQuery) {
		return new EndlessScrollListener() {
			@Override
			public void onLoadMore(int totalItemsCount) {
				populateTimeline(searchQuery);
			}
		};
	}

	public static RelatedTweetsFragment newInstance(String disasterName) {
		RelatedTweetsFragment fragmentRelatedTweets = new RelatedTweetsFragment();
		Bundle args = new Bundle();
		args.putString("disasterName", disasterName);
		fragmentRelatedTweets.setArguments(args);
		return fragmentRelatedTweets;
	}

}
