package com.rimzhim.PaymentGateway.CreatePaymentResponse;

public class PaymentDetails{
	private String amount;
	private int paymentDone;
	private String updated_at;
	private int user_id;
	private String created_at;
	private int id;
	private String order_id;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(int paymentDone) {
		this.paymentDone = paymentDone;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
}
