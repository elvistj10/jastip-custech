package com.cus.jastip.payment.model;

public class CorporateStatementsDataModel {
	private String transactionDate;
	private String branchCode;
	private String transactionType;
	private Double transactionAmount;
	private String transactionName;
	private String trailer;

	public CorporateStatementsDataModel() {
		super();
	}

	public CorporateStatementsDataModel(String transactionDate, String branchCode, String transactionType,
			Double transactionAmount, String transactionName, String trailer) {
		super();
		this.transactionDate = transactionDate;
		this.branchCode = branchCode;
		this.transactionType = transactionType;
		this.transactionAmount = transactionAmount;
		this.transactionName = transactionName;
		this.trailer = trailer;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

}
