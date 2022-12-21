package com.rimzhim.ModelClasses.model;

import java.util.ArrayList;
import java.util.List;

public class UserProfileResponse{
	private boolean result;
	private Videos videos;
	private UserProfile user_profile;
	private String message;
	private List<TagsItem> tags;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Videos getVideos() {
		return videos;
	}

	public void setVideos(Videos videos) {
		this.videos = videos;
	}

	public UserProfile getUser_profile() {
		return user_profile;
	}

	public void setUser_profile(UserProfile user_profile) {
		this.user_profile = user_profile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<TagsItem> getTags() {
		return tags;
	}

	public void setTags(List<TagsItem> tags) {
		this.tags = tags;
	}
}