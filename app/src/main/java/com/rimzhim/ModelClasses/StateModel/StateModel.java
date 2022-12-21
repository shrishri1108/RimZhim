package com.rimzhim.ModelClasses.StateModel;

import java.util.ArrayList;

public class StateModel{
	private boolean result;
	private String message;
	private ArrayList<ListItem> list;

	public boolean isResult(){
		return result;
	}

	public String getMessage(){
		return message;
	}

	public ArrayList<ListItem> getList(){
		return list;
	}
}