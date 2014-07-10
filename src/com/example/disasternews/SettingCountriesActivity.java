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
		getMenuInflater().inflate(R.menu.next, menu);
		return true;
	}

	private void populateCountrySetting() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fragmentCountrySetting = CountrySettingFragment.newInstance();
		ft.replace(R.id.flCountries, fragmentCountrySetting);
		ft.commit();
	}
	
	public void onNext(MenuItem mi) {
		Toast.makeText(getApplicationContext(), fragmentCountrySetting.getAdapter().getCountriesSelected().toString(), Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, SettingDisasterTypesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("countries", (ArrayList<String>) fragmentCountrySetting.getAdapter().getCountriesSelected());
		i.putExtras(bundle);
		startActivity(i);
	}
}
