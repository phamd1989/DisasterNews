package com.example.disasternews;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.disasternews.fragment.CountrySettingFragment;

public class SettingCountriesActivity extends FragmentActivity {
	
	CountrySettingFragment fragmentCountrySetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_countries);
		
		populateCountrySetting();
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	
	private void populateCountrySetting() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fragmentCountrySetting = CountrySettingFragment.newInstance();
		ft.replace(R.id.flCountries, fragmentCountrySetting);
		ft.commit();
	}
	
	public void onFinish(MenuItem mi) {
		if (mi.getItemId() == R.id.miFinish) {
			// only move to the next activity if users select check at least on box
			if (fragmentCountrySetting.getSettings().size() == 0) {
				Toast.makeText(getApplicationContext(), "Please check at least one box", Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(this, DisasterTimelineActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("countries", (ArrayList<String>) fragmentCountrySetting.getSettings());
				i.putExtras(bundle);
				startActivity(i);
			}
		}
	}
}
