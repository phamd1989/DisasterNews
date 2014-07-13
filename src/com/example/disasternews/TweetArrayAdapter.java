package com.example.disasternews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.disasternews.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;


public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		View view;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			view = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			view = convertView;
		}
		
		// find the views within the template
		ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
		ImageView ivContentImage = (ImageView) view.findViewById(R.id.ivContentImage);
		TextView tvUserName   = (TextView) view.findViewById(R.id.tvUserName);
		TextView tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
		TextView tvBody		  = (TextView) view.findViewById(R.id.tvBody);
		TextView tvTimestamp  = (TextView) view.findViewById(R.id.tvTimestamp);
		
		
		ivContentImage.setImageResource(android.R.color.transparent);
		ivProfileImage.setImageResource(android.R.color.transparent);
		ivProfileImage.setTag(tweet.getUser().getScreenName());
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// populate views with tweet data
		Picasso.with(getContext())
			   .load(tweet.getUser().getProfileImageUrl())
			   .into(ivProfileImage);
//		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
//		Log.d("debug", "tweet.getContentImgUrl(): " + tweet.getContentImgUrl());
		if (tweet.getContentImgUrl() != null) {
//			imageLoader.displayImage(tweet.getContentImgUrl(), ivContentImage);
			Picasso.with(getContext())
				   .load(tweet.getContentImgUrl())
				   //.resize(470, 320)
				   .into(ivContentImage);
		}
		tvUserName.setText(tweet.getUser().getName());
		tvScreenName.setText("@" + tweet.getUid());//getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
		
		return view;
	}
	
	
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return relativeDate;
	}
}
