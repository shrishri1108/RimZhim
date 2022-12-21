package com.rimzhim.ModelClasses.ContestResponse;

public class ContestsResponseModel{
	private boolean result;
	private ContestList contest_list;
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public ContestList getContest_list() {
		return contest_list;
	}

	public void setContest_list(ContestList contest_list) {
		this.contest_list = contest_list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
