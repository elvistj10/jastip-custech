package com.cus.jastip.transaction.messaging.model;

import com.cus.jastip.transaction.domain.enumeration.PostingType;

public class TransactionCountModel {
	private Long profileId;
	private String entity;
	private PostingType type;

	public TransactionCountModel() {
		super();
	}

	public TransactionCountModel(Long profileId, String entity, PostingType type) {
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
