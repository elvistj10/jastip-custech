package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A FollowingList.
 */
@Entity
@Table(name = "following_list")
@Document(indexName = "followinglist")
public class FollowingList extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "following_profile_id", nullable = false)
    private Long followingProfileId;

    @Column(name = "following_date")
    private Instant followingDate;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowingProfileId() {
        return followingProfileId;
    }

    public FollowingList followingProfileId(Long followingProfileId) {
        this.followingProfileId = followingProfileId;
        return this;
    }

    public void setFollowingProfileId(Long followingProfileId) {
        this.followingProfileId = followingProfileId;
    }

    public Instant getFollowingDate() {
        return followingDate;
    }

    public FollowingList followingDate(Instant followingDate) {
        this.followingDate = followingDate;
        return this;
    }

    public void setFollowingDate(Instant followingDate) {
        this.followingDate = followingDate;
    }

    public Boolean isStatus() {
        return status;
    }

    public FollowingList status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public FollowingList profile(Profile profile) {
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
        FollowingList followingList = (FollowingList) o;
        if (followingList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followingList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowingList{" +
            "id=" + getId() +
            ", followingProfileId=" + getFollowingProfileId() +
            ", followingDate='" + getFollowingDate() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
