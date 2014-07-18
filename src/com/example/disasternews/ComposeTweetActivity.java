package com.example.disasternews;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ComposeTweetActivity extends Activity {
	
	private EditText etTweet;
	private Button btnTweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		etTweet        = (EditText) findViewById(R.id.etTweet);
		btnTweet  	   = (Button) findViewById(R.id.btnTweet);
		etTweet.setText(getIntent().getStringExtra("title")
				+ getIntent().getStringExtra("url"));
	}
	
	public void onTweet(View v) {
		Intent data = new Intent();
		Log.d("debug", "tweet: " + etTweet.getText().toString());
		data.putExtra("tweet", etTweet.getText().toString());
		setResult(RESULT_OK, data);
		finish();
	}
	
}
