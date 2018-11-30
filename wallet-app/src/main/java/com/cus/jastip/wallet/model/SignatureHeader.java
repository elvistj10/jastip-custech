package com.cus.jastip.wallet.model;

public class SignatureHeader {
	private String url;
	private String accessToken;
	private String httpMethod;
	private String jsonStr;

	public SignatureHeader() {
		super();
	}

	public SignatureHeader(String url, String accessToken, String httpMethod, String jsonStr) {
		super();
		this.url = url;
		this.accessToken = accessToken;
		this.httpMethod = httpMethod;
		this.jsonStr = jsonStr;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

}
