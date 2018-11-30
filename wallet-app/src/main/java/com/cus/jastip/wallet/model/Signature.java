package com.cus.jastip.wallet.model;

public class Signature {

	private String hmac;
	private String timestamp;

	public Signature() {
		super();
	}

	public Signature(String hmac, String timestamp) {
		super();
		this.hmac = hmac;
		this.timestamp = timestamp;
	}

	public String getHmac() {
		return hmac;
	}

	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
