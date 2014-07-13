package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsArrayAdapter extends ArrayAdapter<String> {
	
	private List<String> settingsSelected;
	
	public SettingsArrayAdapter(Context context, List<String> settings) {
		super(context, 0, settings);
		settingsSelected = new ArrayList<String>();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String setting = getItem(position);
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
		tvSetting.setText(setting);
		return view;
	}
	
	public List<String> getCountriesSelected() {
		return settingsSelected;
	}
}
