package com.example.disasternews.models;

public class Country {
	private String countryName;
	private boolean isChecked;
	
	public Country(String name, boolean checked) {
		countryName = name;
		isChecked = checked;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	
}
