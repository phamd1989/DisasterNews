
package com.example.disasternews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.disasternews.R;
import com.example.disasternews.SettingsArrayAdapter;

public class BaseSettingFragment extends Fragment{
	protected SettingsArrayAdapter adapter;
	private ListView lvCountries;
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings_list, container, false);
		lvCountries = (ListView) v.findViewById(R.id.lvSettings);
		lvCountries.setAdapter(adapter);
		return v;
	}


	public SettingsArrayAdapter getAdapter() {
		return adapter;
	}
}
