package com.example.disasternews;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;

public class DisasterNewsClient extends OAuthBaseClient{
	
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "Jy5Cw2hBY9G3LseERuUupQiGv";       // Change this
    public static final String REST_CONSUMER_SECRET = "0kMuucuCi9BKsGDgQU3b7FoouSwZHfUikfvy7urcBHQuPCSoLQ"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
	
	public DisasterNewsClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
}
