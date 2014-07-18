package com.example.disasternews;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disasternews.fragment.RelatedTweetsFragment;
import com.example.disasternews.models.Disaster;
import com.example.disasternews.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DisasterDetailsActivity extends FragmentActivity {
    private static final int REQUEST_CODE = 10;
    private String disasterName;
    private int dId;
    
    private ImageView ivMap;
    private ImageButton ibWebView;
    private TextView tvTimestamp;
    private TextView tvTitle;
    private ImageButton ibFavorite;
    private ImageButton ibTweet;
    
    
    DisasterNewsClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
        setContentView(R.layout.activity_disaster_details);
        
        client = DiasterNewsApplication.getRestClient();
        
        // loading progress
        showProgressBar();
        // run background task
        disasterName = getIntent().getStringExtra("disasterName");
        dId = getIntent().getIntExtra("disasterId", 0);
        loadDisasterDetails(dId);
        loadTweetsRelated();
    }
    
    // Should be called manually when an async task has started
    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true); 
    }
    
    // Should be called when an async task has finished
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false); 
    }

    private void loadDisasterDetails(int disasterId) {
		ivMap = (ImageView) findViewById(R.id.ivMap);
		ibWebView = (ImageButton) findViewById(R.id.ibViewOnWeb);
		tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ibFavorite = (ImageButton) findViewById(R.id.ibFavorite);
		ibTweet = (ImageButton) findViewById(R.id.ibTweet);
    	
    	final Disaster disaster = Disaster.getDisasterInfo(disasterId);
    	ivMap.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(disaster.getImageUrl(), ivMap);
		tvTimestamp.setText(disaster.getRelativeTime());
		tvTitle.setText(disaster.getName());
		ibWebView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(disaster.getUrl()));
		        startActivity(i);
			}
		});
		
		// handling click on Favorite button here
		boolean favorite = disaster.getFavorite();
        if ( favorite )
        	ibFavorite.setImageResource(R.drawable.ic_favorite_good_yellow);
        else {
        	ibFavorite.setImageResource(R.drawable.ic_favorite_good_black);
        }
        ibFavorite.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean favoriteChanged = !disaster.getFavorite();
                disaster.setFavorite( favoriteChanged );
                disaster.save();
                if ( favoriteChanged ) {
                	ibFavorite.setImageResource( R.drawable.ic_favorite_good_yellow );
                }
                else {
                	ibFavorite.setImageResource( R.drawable.ic_favorite_good_black );
                }
            }
        });
        
        ibTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(DisasterDetailsActivity.this, ComposeTweetActivity.class);
				i.putExtra("title", tvTitle.getText().toString());
				i.putExtra("url", disaster.getUrl());
				startActivityForResult(i, REQUEST_CODE);
			}
		});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		String tweet = data.getExtras().getString("tweet");
    		Log.d("debug", "tweet in onActivityResult: " + tweet);
    		client.setTweetBody(tweet);
    		postTweetUpdate();
    	}
    }
    
	protected void loadTweetsRelated() {
        Toast.makeText(DisasterDetailsActivity.this, disasterName, Toast.LENGTH_SHORT).show();
        Log.d("debug", "Search for: " + disasterName);
        // query twitter api about related tweets for this disaster name
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RelatedTweetsFragment fragmentRelatedTweets = RelatedTweetsFragment.newInstance(disasterName);
        ft.replace(R.id.flRelatedTweets, fragmentRelatedTweets);
        ft.commit();
    }
	
	private void postTweetUpdate() {
		client.postTweet(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonObj) {
				Toast.makeText(DisasterDetailsActivity.this, "Tweet posted", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}
    
}
