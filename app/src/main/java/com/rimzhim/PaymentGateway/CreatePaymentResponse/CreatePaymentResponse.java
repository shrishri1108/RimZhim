package com.rimzhim.PaymentGateway.CreatePaymentResponse;

public class CreatePaymentResponse{
	private boolean result;
	private PaymentDetails payment_details;
	private String message;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public PaymentDetails getPayment_details() {
		return payment_details;
	}

	public void setPayment_details(PaymentDetails payment_details) {
		this.payment_details = payment_details;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
