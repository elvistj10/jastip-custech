package com.cus.jastip.profile.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A BlockList.
 */
@Entity
@Table(name = "block_list")
@Document(indexName = "blocklist")
public class BlockList extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "blocked_profile_id", nullable = false)
	private Long blockedProfileId;

	@NotNull
	@Column(name = "block_date", nullable = false)
	private Instant blockDate;

	@Column(name = "jhi_block")
	private Boolean block;

	@ManyToOne
	private Profile profile;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBlockedProfileId() {
		return blockedProfileId;
	}

	public BlockList blockedProfileId(Long blockedProfileId) {
		this.blockedProfileId = blockedProfileId;
		return this;
	}

	public void setBlockedProfileId(Long blockedProfileId) {
		this.blockedProfileId = blockedProfileId;
	}

	public Instant getBlockDate() {
		return blockDate;
	}

	public BlockList blockDate(Instant blockDate) {
		this.blockDate = blockDate;
		return this;
	}

	public void setBlockDate(Instant blockDate) {
		this.blockDate = blockDate;
	}

	public Boolean isBlock() {
		return block;
	}

	public BlockList block(Boolean block) {
		this.block = block;
		return this;
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}

	public Profile getProfile() {
		return profile;
	}

	public BlockList profile(Profile profile) {
		this.profile = profile;
		return this;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlockList blockList = (BlockList) o;
		if (blockList.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), blockList.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "BlockList{" + "id=" + getId() + ", blockedProfileId=" + getBlockedProfileId() + ", blockDate='"
				+ getBlockDate() + "'" + ", block='" + isBlock() + "'" + "}";
	}
}
