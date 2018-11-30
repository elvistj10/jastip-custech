package com.cus.jastip.profile.messaging.model;

import com.cus.jastip.profile.domain.enumeration.PostingType;

public class TransactionCountMessageModel {
	private Long profileId;
	private String entity;
	private PostingType type;

	public TransactionCountMessageModel() {
		super();
	}

	public TransactionCountMessageModel(Long profileId, String entity, PostingType type) {
		super();
		this.profileId = profileId;
		this.entity = entity;
		this.type = type;
	}

	public PostingType getType() {
		return type;
	}

	public void setType(PostingType type) {
		this.type = type;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
