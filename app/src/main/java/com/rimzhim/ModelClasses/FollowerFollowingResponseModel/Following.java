package com.rimzhim.ModelClasses.FollowerFollowingResponseModel;

import java.util.List;

public class Following{
	private boolean result;
	private int count;
	private List<FollowListItem> follow_list;
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FollowListItem> getFollow_list() {
		return follow_list;
	}

	public void setFollow_list(List<FollowListItem> follow_list) {
		this.follow_list = follow_list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}