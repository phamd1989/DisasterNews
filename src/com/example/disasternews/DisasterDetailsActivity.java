package com.example.disasternews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disasternews.fragment.RelatedTweetsFragment;
import com.example.disasternews.models.Disaster;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DisasterDetailsActivity extends FragmentActivity {
    private static final int REQUEST_CODE = 10;
    private String disasterName;
    private int dId;
    
    private ImageView ivMap;
    private Button btnWebView;
    private TextView tvTimestamp;
    private TextView tvTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
        setContentView(R.layout.activity_disaster_details);
        
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
		btnWebView = (Button) findViewById(R.id.btnWebView);
		tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
    	
    	final Disaster disaster = Disaster.getDisasterInfo(disasterId);
    	ivMap.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(disaster.getImageUrl(), ivMap);
		tvTimestamp.setText(disaster.getDate());
		tvTitle.setText(disaster.getName());
		btnWebView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(disaster.getUrl()));
		        startActivity(i);
			}
		});
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
    
}
