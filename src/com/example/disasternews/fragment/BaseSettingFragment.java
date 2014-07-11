
package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.disasternews.R;
import com.example.disasternews.SettingsArrayAdapter;

public class BaseSettingFragment extends Fragment{
	protected SettingsArrayAdapter adapter;
	private ListView lvCountries;
	private List<String> settingsSelected;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings_list, container, false);
		lvCountries = (ListView) v.findViewById(R.id.lvSettings);
		lvCountries.setAdapter(adapter);
		
		settingsSelected = new ArrayList<>();
		lvCountries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox) view.findViewById(R.id.cbSetting);
				TextView tv = (TextView) view.findViewById(R.id.tvSetting);
				if (cb.isChecked()) {
					// unchecking box
					cb.setChecked(false);
					settingsSelected.remove(tv.getText().toString());
				} else {
					// checking box
					cb.setChecked(true);
					settingsSelected.add(tv.getText().toString());
				}
				Log.d("Debug", settingsSelected.toString());
			}
		});
		return v;
	}

	public List<String> getSettings() {
		return settingsSelected;
	}
}
