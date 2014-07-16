package com.example.disasternews;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DisasterNewsClient extends OAuthBaseClient{
	
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "pkGeBJe31xxp05pW6wvcN0NQF";       // Change this
    public static final String REST_CONSUMER_SECRET = "NSdPuVFH4e7zJRoKcnqrUIb0KOdIAzu4bSeSEPv4VArktnRxEi"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
	
	public DisasterNewsClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getSearchEvents(String searchQuery, long maxTweetId,
			AsyncHttpResponseHandler handler) {
		
		String apiUrl = getApiUrl("search/tweets.json"); 
		RequestParams params = new RequestParams();
//		if (searchQuery != null) {
//			params.put("q", Uri.encode(searchQuery));
//		}
		if (maxTweetId != 0) {
			params.put("max_id", Long.toString(maxTweetId));
		}
//		Log.d("debug", "searchQuery inside getUserTimeline: " + searchQuery);
//		Log.d("debug", "maxTweetId inside getUserTimeline: " + Long.toString(maxTweetId));
//		Log.d("debug", params.toString());
//		if (searchQuery == null && maxTweetId == 0) {
//			params = null;
//		}
		params.put("q", searchQuery);
		Log.d("debug", params.toString());
		client.get(apiUrl, params, handler);
	}
	
}
