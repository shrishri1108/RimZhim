package com.rimzhim.ModelClasses.FollowerFollowingResponseModel;

public class DataItem{
	private String image;
	private String name;
	private String user_name;
	private int id;
	private int is_following;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIs_following() {
		return is_following;
	}

	public void setIs_following(int is_following) {
		this.is_following = is_following;
	}
}
