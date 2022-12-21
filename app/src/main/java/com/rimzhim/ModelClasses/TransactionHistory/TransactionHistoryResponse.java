package com.rimzhim.ModelClasses.TransactionHistory;

public class TransactionHistoryResponse{
	private boolean result;
	private String message;
	private Transactions transactions;

	public boolean isResult(){
		return result;
	}

	public String getMessage(){
		return message;
	}

	public Transactions getTransactions(){
		return transactions;
	}
}
