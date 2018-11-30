package com.cus.jastip.wallet.model;

public class Corporates {

	private String CorporateID;
	private String SourceAccountNumber;
	private String TransactionID;
	private String TransactionDate;
	private String ReferenceID;
	private String CurrencyCode;
	private String Amount;
	private String BeneficiaryAccountNumber;
	private String Remark1;
	private String Remark2;

	public Corporates() {
		super();
	}

	public Corporates(String corporateID, String sourceAccountNumber, String transactionID, String transactionDate,
			String referenceID, String currencyCode, String amount, String beneficiaryAccountNumber, String remark1,
			String remark2) {
		super();
		CorporateID = corporateID;
		SourceAccountNumber = sourceAccountNumber;
		TransactionID = transactionID;
		TransactionDate = transactionDate;
		ReferenceID = referenceID;
		CurrencyCode = currencyCode;
		Amount = amount;
		BeneficiaryAccountNumber = beneficiaryAccountNumber;
		Remark1 = remark1;
		Remark2 = remark2;
	}

	public String getCorporateID() {
		return CorporateID;
	}

	public void setCorporateID(String corporateID) {
		CorporateID = corporateID;
	}

	public String getSourceAccountNumber() {
		return SourceAccountNumber;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		SourceAccountNumber = sourceAccountNumber;
	}

	public String getTransactionID() {
		return TransactionID;
	}

	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}

	public String getTransactionDate() {
		return TransactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		TransactionDate = transactionDate;
	}

	public String getReferenceID() {
		return ReferenceID;
	}

	public void setReferenceID(String referenceID) {
		ReferenceID = referenceID;
	}

	public String getCurrencyCode() {
		return CurrencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getBeneficiaryAccountNumber() {
		return BeneficiaryAccountNumber;
	}

	public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
		BeneficiaryAccountNumber = beneficiaryAccountNumber;
	}

	public String getRemark1() {
		return Remark1;
	}

	public void setRemark1(String remark1) {
		Remark1 = remark1;
	}

	public String getRemark2() {
		return Remark2;
	}

	public void setRemark2(String remark2) {
		Remark2 = remark2;
	}

}
