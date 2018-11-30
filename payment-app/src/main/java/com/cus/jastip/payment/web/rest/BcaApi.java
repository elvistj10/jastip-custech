package com.cus.jastip.payment.web.rest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cus.jastip.payment.model.CorporateStatementsDataModel;
import com.cus.jastip.payment.model.CorporateStatementsModel;
import com.cus.jastip.payment.model.Signature;
import com.cus.jastip.payment.model.SignatureHeader;
import com.cus.jastip.payment.model.Token;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/bca")
public class BcaApi {
	private static final String bcaKey = "3a3add4e-3df7-494f-9ed1-d92b831cbeb3";
	private static final String bcaSecret = "5c5d3242-559a-409f-bec7-aae07ef1d22d";
	private static final String sendbox = "https://sandbox.bca.co.id";
	private static final String client_id = "45108128-f5d1-4e71-a2b2-58d63891cd1a";
	private static final String secret_key = "038887c4-f660-4ff5-88cd-d4520f7caed8";
	private static final String bankingV3Crops = "/banking/v3";

	@PostMapping("/token")
	public Token postToken() {
		try {
			String outh = client_id + ":" + secret_key;
			String encode = Base64.getEncoder().encodeToString(outh.getBytes());
			HttpResponse response = Request.Post("https://sandbox.bca.co.id/api/oauth/token")
					.addHeader("Content-Type", "application/x-www-form-urlencoded")
					.addHeader("Authorization", "Basic " + encode)
					.bodyForm(Form.form().add("grant_type", "client_credentials").build()).execute().returnResponse();
			Token token = new Gson().fromJson(EntityUtils.toString(response.getEntity()), Token.class);
			return token;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Token();
	}

	// POST /utilities/signature

	@PostMapping("/signature")
	public Signature getSignature(SignatureHeader signHeader) {
		String timestamp = getDateNow();

		try {
			HttpResponse response = null;
			if (signHeader.getHttpMethod().equals("GET")) {
				response = Request.Post("https://sandbox.bca.co.id/utilities/signature")
						.addHeader("Timestamp", timestamp).addHeader("URI", signHeader.getUrl())
						.addHeader("AccessToken", signHeader.getAccessToken()).addHeader("APISecret", bcaSecret)
						.addHeader("HTTPMethod", signHeader.getHttpMethod()).execute().returnResponse();
			} else {
				response = Request.Post("https://sandbox.bca.co.id/utilities/signature")
						.addHeader("Timestamp", timestamp).addHeader("URI", signHeader.getUrl())
						.addHeader("AccessToken", signHeader.getAccessToken()).addHeader("APISecret", bcaSecret)
						.addHeader("HTTPMethod", signHeader.getHttpMethod())
						.addHeader("Content-Type", "application/json")
						.bodyString(signHeader.getJsonStr(), ContentType.APPLICATION_JSON).execute().returnResponse();
			}

			StringTokenizer st = new StringTokenizer(EntityUtils.toString(response.getEntity()));
			String hmac = "";
			int i = 0;
			int count = st.countTokens();
			while (st.hasMoreElements()) {
				st.nextElement();
				if (i == count - 2) {
					hmac = (String) st.nextElement();
					break;
				}
				i++;
			}
			Signature signature = new Signature(hmac, timestamp);
			return signature;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Signature();

	}


	@GetMapping("/v3/corporates/statements")
	public List<CorporateStatementsDataModel> getBankCorpsV3statements() throws ParseException, IOException {
		String CorporateID = "BCAAPI2016";
		String AccountNumbers = "0201245680";
		String corpsv3 = bankingV3Crops + "/corporates/" + CorporateID + "/accounts/" + AccountNumbers
				+ "/statements?StartDate=2016-09-01&EndDate=2016-09-01";
		String url = sendbox + corpsv3;
		Token token = postToken();
		if (token != null) {
			SignatureHeader signHeader = new SignatureHeader(corpsv3, token.getAccess_token(), "GET", null);
			Signature signature = getSignature(signHeader);
			if (signature != null) {
				HttpResponse response = Request.Get(url).addHeader("X-BCA-KEY", bcaKey)
						.addHeader("Authorization", "Bearer " + token.getAccess_token())
						.addHeader("X-BCA-Timestamp", signature.getTimestamp())
						.addHeader("X-BCA-Signature", signature.getHmac()).execute().returnResponse();
				String stringEntity = convertStringLowrcase(EntityUtils.toString(response.getEntity()));

				stringEntity = stringEntity.substring(0, stringEntity.length() - 1) + "}";
				CorporateStatementsModel result = new Gson().fromJson(stringEntity, CorporateStatementsModel.class);
				List<CorporateStatementsDataModel> list = new ArrayList<>();
				for (CorporateStatementsDataModel r : result.getData()) {
					list.add(r);
				}

				for (int i = 0; i < (list.size() / 2); i++) {
					List<CorporateStatementsDataModel> ls = new ArrayList<>();
					ls.add(list.get(i * 2));
					ls.add(list.get((i * 2) + 1));
				}

				return list;
			}

		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public String convertStringLowrcase(String jsonconvert) {
		// ngebuat jadi uppercase depannya
		@SuppressWarnings("rawtypes")
		List<String> list = new ArrayList();
		for (int i = 0; i < jsonconvert.length(); i++) {
			if (i > jsonconvert.length() - 2) {
				list.add("\"");
				break;
			} else {
				String a = jsonconvert.substring(i, i + 1);
				if (a.equals("\"")) {
					list.add(a);
					i++;
					a = jsonconvert.substring(i, i + 1);
					list.add(a.toLowerCase());
				} else {
					list.add(a);
				}
			}

		}
		String jsonstr = "";
		for (String ls : list) {
			jsonstr = jsonstr + ls;
		}
		return jsonstr;
	}

	public String getDateNow() {
		DateFormat dateFormatYear = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormatHour = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String timestamp = dateFormatYear.format(date).toString() + "T" + dateFormatHour.format(date).toString()
				+ ".000+07:00";
		return timestamp;
	}

}
