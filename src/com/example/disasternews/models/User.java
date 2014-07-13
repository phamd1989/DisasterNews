package com.example.disasternews.models;

import java.util.List;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "users")
public class User extends Model{
	
	@Column(name = "name")
	private String name;
	@Column(name = "userId")
	private long userId;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	@Column(name = "tweetsCount")
	private int tweetsCount;
	@Column(name = "followersCount")
	private int followersCount;
	@Column(name = "followingCount")
	private int followingCount;
	
	public User() {
		super();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public User(JSONObject obj) {
		super();
		try {
			 this.name            = obj.getString("name");
			 this.userId          = obj.getLong("id");
			 this.screenName      = obj.getString("screen_name");
			 this.profileImageUrl = obj.getString("profile_image_url");
			 this.tweetsCount     = obj.getInt("statuses_count");
			 this.followingCount  = obj.getInt("friends_count");
			 this.followersCount  = obj.getInt("followers_count");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getTweetsCount() {
		return tweetsCount;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFollowingCount() {
		return followingCount;
	}

	public static User fetchUser(JSONObject obj) {
		try {
			 long userId = obj.getLong("id");
			 if (getUser(userId) == null) {
				 User user = new User(obj);
				 user.save();
//				 Log.d("debug", user.toString());
				 return user;
			 } else {
				 return getUser(userId);
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static User getUser(long userId) {
		return (User) new Select().from(User.class).where("userId = ?", userId).executeSingle();
	}
	
	public static User getUserFromScreenName(String screen_name) {
		return (User) new Select().from(User.class).where("screenName = ?", screen_name).executeSingle();
	}
	
	public String getName() {
		return name;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	
	public static List<User> recentUsers() {
		return new Select().from(User.class).execute();
	}

}
