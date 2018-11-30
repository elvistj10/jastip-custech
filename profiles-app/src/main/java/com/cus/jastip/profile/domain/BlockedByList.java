package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A BlockedByList.
 */
@Entity
@Table(name = "blocked_by_list")
@Document(indexName = "blockedbylist")
public class BlockedByList extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bloker_profile_id", nullable = false)
	private Long blokerProfileId;

    @NotNull
    @Column(name = "block_date", nullable = false)
    private Instant blockDate;

    @Column(name = "blocked")
    private Boolean blocked;

    @ManyToOne
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlokerProfileId() {
        return blokerProfileId;
    }

    public BlockedByList blokerProfileId(Long blokerProfileId) {
        this.blokerProfileId = blokerProfileId;
        return this;
    }

    public void setBlokerProfileId(Long blokerProfileId) {
        this.blokerProfileId = blokerProfileId;
    }

    public Instant getBlockDate() {
        return blockDate;
    }

    public BlockedByList blockDate(Instant blockDate) {
        this.blockDate = blockDate;
        return this;
    }

    public void setBlockDate(Instant blockDate) {
        this.blockDate = blockDate;
    }

    public Boolean isBlocked() {
        return blocked;
    }

    public BlockedByList blocked(Boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Profile getProfile() {
        return profile;
    }

    public BlockedByList profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockedByList blockedByList = (BlockedByList) o;
        if (blockedByList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blockedByList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlockedByList{" +
            "id=" + getId() +
            ", blokerProfileId=" + getBlokerProfileId() +
            ", blockDate='" + getBlockDate() + "'" +
            ", blocked='" + isBlocked() + "'" +
            "}";
    }
}
