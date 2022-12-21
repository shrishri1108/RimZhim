package com.rimzhim.ModelClasses.ChatModel;

public class ChatsInboxesResponse{
	private boolean result;
	private ChatList chat_list;
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public ChatList getChat_list() {
		return chat_list;
	}

	public void setChat_list(ChatList chat_list) {
		this.chat_list = chat_list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
