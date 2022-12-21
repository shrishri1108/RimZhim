package com.rimzhim.ModelClasses.LeaderBordModel;

public class LeaderboardItem{
	private String video_link;
	private String user_image;
	private int user_id;
	private String user_name;
	private String is_winning_zone;
	private int rank;
	private String votes;

	public String getVideo_link() {
		return video_link;
	}

	public void setVideo_link(String video_link) {
		this.video_link = video_link;
	}

	public String getUser_image() {
		return user_image;
	}

	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getIs_winning_zone() {
		return is_winning_zone;
	}

	public void setIs_winning_zone(String is_winning_zone) {
		this.is_winning_zone = is_winning_zone;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getVotes() {
		return votes;
	}

	public void setVotes(String votes) {
		this.votes = votes;
	}
}
