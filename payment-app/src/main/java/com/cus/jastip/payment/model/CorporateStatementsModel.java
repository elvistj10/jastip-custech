package com.cus.jastip.payment.model;

import java.util.List;

public class CorporateStatementsModel {
	private String startDate;
	private String endDate;
	private String currency;
	private String startBalance;
	private List<CorporateStatementsDataModel> data;

	public CorporateStatementsModel() {
		super();
	}

	public CorporateStatementsModel(String startDate, String endDate, String currency, String startBalance,
			List<CorporateStatementsDataModel> data) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.currency = currency;
		this.startBalance = startBalance;
		this.data = data;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStartBalance() {
		return startBalance;
	}

	public void setStartBalance(String startBalance) {
		this.startBalance = startBalance;
	}

	public List<CorporateStatementsDataModel> getData() {
		return data;
	}

	public void setData(List<CorporateStatementsDataModel> data) {
		this.data = data;
	}

}
