package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import com.example.disasternews.models.Country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsArrayAdapter extends ArrayAdapter<Country> {
	
	private List<Country> settingsSelected;
	
	public SettingsArrayAdapter(Context context, List<Country> settings) {
		super(context, 0, settings);
		settingsSelected = new ArrayList<Country>();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Country c = getItem(position);
		View view;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			view = inflator.inflate(R.layout.settings_item, parent, false);
		} else {
			view = convertView;
		}
		
		// find the views in the xml layout
		CheckBox cbSetting = (CheckBox) view.findViewById(R.id.cbSetting);
		TextView tvSetting = (TextView) view.findViewById(R.id.tvSetting);
		tvSetting.setText(c.getCountryName());
		cbSetting.setChecked(c.isChecked());
		return view;
	}
	
	public List<Country> getCountriesSelected() {
		return settingsSelected;
	}
}
