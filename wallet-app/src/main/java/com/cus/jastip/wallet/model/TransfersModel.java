package com.cus.jastip.wallet.model;

public class TransfersModel {
	private String transactionID;
	private String transactionDate;
	private String referenceID;
	private String status;

	public TransfersModel() {
		super();
	}

	public TransfersModel(String transactionID, String transactionDate, String referenceID, String status) {
		super();
		this.transactionID = transactionID;
		this.transactionDate = transactionDate;
		this.referenceID = referenceID;
		this.status = status;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getReferenceID() {
		return referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
