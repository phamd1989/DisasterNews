package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.disasternews.fragment.DisasterTypeSettingFragment;


public class SettingDisasterTypesActivity extends FragmentActivity {
	
	DisasterTypeSettingFragment fragmentDisasterTypesSetting;
	List<String> countries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_disaster_types);
		countries = getIntent().getExtras().getStringArrayList("countries");
		populateDisasterTypesSetting();
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.finish, menu);
		return true;
	}

	private void populateDisasterTypesSetting() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		fragmentDisasterTypesSetting = DisasterTypeSettingFragment.newInstance();
		ft.replace(R.id.flTypes, fragmentDisasterTypesSetting);
		ft.commit();
	}
	
	public void onFinish(MenuItem mi) {
		Intent i = new Intent(this, DisasterTimelineActivity.class);
		List<String> result = new ArrayList<>();
		result.addAll(countries);
		// add space character to separate countries from disaster types
		result.add(" ");
		List<String> types = fragmentDisasterTypesSetting.getAdapter().getCountriesSelected();
		result.addAll(types);
		Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
		
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("countries_and_types", (ArrayList<String>) result);
		i.putExtras(bundle);
		startActivity(i);
	}
}
