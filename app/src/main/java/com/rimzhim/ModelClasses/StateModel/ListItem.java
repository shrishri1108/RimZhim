package com.rimzhim.ModelClasses.StateModel;

import java.util.ArrayList;

public class ListItem{
	private ArrayList<CitiesItem> cities;
	private String name;
	private int id;

	public ArrayList<CitiesItem> getCities() {
		return cities;
	}

	public void setCities(ArrayList<CitiesItem> cities) {
		this.cities = cities;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}