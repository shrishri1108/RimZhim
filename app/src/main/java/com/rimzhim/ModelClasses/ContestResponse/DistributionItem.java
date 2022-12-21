package com.rimzhim.ModelClasses.ContestResponse;

public class DistributionItem{
	private int amount;
	private int size;
	private String updated_at;
	private String percentage;
	private String created_at;
	private int id;
	private int lower_rank;
	private int upper_rank;
	private int contest_id;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLower_rank() {
		return lower_rank;
	}

	public void setLower_rank(int lower_rank) {
		this.lower_rank = lower_rank;
	}

	public int getUpper_rank() {
		return upper_rank;
	}

	public void setUpper_rank(int upper_rank) {
		this.upper_rank = upper_rank;
	}

	public int getContest_id() {
		return contest_id;
	}

	public void setContest_id(int contest_id) {
		this.contest_id = contest_id;
	}
}
