package com.cus.jastip.uaa.messaging.model;

import com.cus.jastip.uaa.domain.enumeration.ProfileStatus;

public class ProfileMessageModel {

	private Long userId;
	private String login;
	private String firstname;
	private ProfileStatus status;

	public ProfileMessageModel() {
		super();
	}

	public ProfileMessageModel(Long userId, String login, String firstname, ProfileStatus status) {
		super();
		this.userId = userId;
		this.login = login;
		this.firstname = firstname;
		this.status = status;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public ProfileStatus getStatus() {
		return status;
	}

	public void setStatus(ProfileStatus status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
