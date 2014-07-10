package com.example.disasternews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

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
		cbSetting.setText(setting);
		cbSetting.setTag(setting);
		
		cbSetting.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				if (cb.isChecked()) {
					settingsSelected.add(cb.getText().toString());
				} else {
					settingsSelected.remove(cb.getText().toString());
				}
				Log.d("Debug", settingsSelected.toString());
			}
		});
		return view;
	}
	
	public List<String> getCountriesSelected() {
		return settingsSelected;
	}
}
