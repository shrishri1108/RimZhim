package com.rimzhim.ModelClasses.LeaderBordModel;

import java.util.List;

public class LeaderboardResponse{
	private boolean result;
	private List<LeaderboardItem> leaderboard;
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<LeaderboardItem> getLeaderboard() {
		return leaderboard;
	}

	public void setLeaderboard(List<LeaderboardItem> leaderboard) {
		this.leaderboard = leaderboard;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}