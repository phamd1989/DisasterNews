package com.example.disasternews.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tweets")
public class Tweet extends Model{
	
	@Column(name = "body")
	private String body;
	@Column(name = "uid", unique = true)
	private long uid;
	@Column(name = "createdAt")
	private String createdAt;
	@Column(name = "user")
	private User user;
	@Column(name = "userScreenName")
	private String userScreenName;
	@Column(name = "contentImgUrl")
	private String contentImgUrl;
	
	public Tweet() {
		super();
	}
	
	public Tweet(JSONObject obj) {
		super();
		try {
			 this.body           = obj.getString("text");
			 this.uid            = obj.getLong("id");
			 this.createdAt      = obj.getString("created_at");
			 this.user           = User.fetchUser(obj.getJSONObject("user"));
			 this.userScreenName = this.user.getScreenName();
			 JSONObject temp     = obj.getJSONObject("extended_entities");
			 if (temp != null) {
//				 Log.d("debug", "temp.toString(): " + temp.toString());
				 this.contentImgUrl = temp.getJSONArray("media").getJSONObject(0).getString("media_url") + ":medium";
//				 Log.d("debug", "contentImgUrl: " + contentImgUrl);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getContentImgUrl() {
		return contentImgUrl;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static ArrayList<Tweet> fromJsonArray(JSONArray json) {
		Log.d("debug", "JSONResponse: " + json.toString());
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		for (int i = 0; i < json.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = json.getJSONObject(i);
				long tweetUid = tweetJson.getLong("id");
				if (getTweet(tweetUid) == null) {
					Tweet tweet = new Tweet(tweetJson);
					if (tweet != null) {
						tweet.save();
						tweets.add(tweet);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tweets;
	}
	
	
	public static Tweet getTweet(JSONObject jsonObj) {
		return null;
	}
	
	public static List<Tweet> recentTweets() {
		return new Select().from(Tweet.class).execute(); //.orderBy("createdAt DESC").execute();
	}
	
	@Override
	public String toString() {
		return Long.toString(this.uid);
	}
	
	private static Tweet getTweet(long uid) {
		return (Tweet) new Select().from(Tweet.class).where("uid = ?", uid).executeSingle();
	}

	public static List<Tweet> getTweetsForScreenName(String screenName) {
		return new Select().from(Tweet.class).where("userScreenName = ?", screenName).execute();
	}
}
