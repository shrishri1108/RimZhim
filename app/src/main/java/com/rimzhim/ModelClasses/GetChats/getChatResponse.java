package com.rimzhim.ModelClasses.GetChats;

public class getChatResponse{
	private boolean result;
	private Chats chats;
	private String message;

	public boolean isResult(){
		return result;
	}

	public Chats getChats(){
		return chats;
	}

	public String getMessage(){
		return message;
	}
}
