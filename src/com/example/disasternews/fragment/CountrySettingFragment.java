package com.example.disasternews.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import com.example.disasternews.SettingsArrayAdapter;

public class CountrySettingFragment extends BaseSettingFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<String> countries = new ArrayList<>();
		countries.add("Select All");
		countries.add("USA");
		countries.add("Vietnam");
		countries.add("Malaysia");
		countries.add("UK");
		countries.add("Philippines");
		countries.add("South Sudan");
		adapter = new SettingsArrayAdapter(getActivity(), countries);
	}

	public static CountrySettingFragment newInstance() {
		return new CountrySettingFragment();
	}
}
