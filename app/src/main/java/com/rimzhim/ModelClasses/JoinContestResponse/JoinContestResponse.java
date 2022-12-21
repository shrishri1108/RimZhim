package com.rimzhim.ModelClasses.JoinContestResponse;

import java.util.List;

public class JoinContestResponse{
	private boolean result;
	private List<String> videos;
	private String message;

	public boolean isResult(){
		return result;
	}

	public List<String> getVideos(){
		return videos;
	}

	public String getMessage(){
		return message;
	}
}