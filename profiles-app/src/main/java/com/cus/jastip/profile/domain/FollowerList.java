package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A FollowerList.
 */
@Entity
@Table(name = "follower_list")
@Document(indexName = "followerlist")
public class FollowerList extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "follower_profile_id", nullable = false)
    private Long followerProfileId;

    @Column(name = "followed_date")
    private Instant followedDate;

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

    public Long getFollowerProfileId() {
        return followerProfileId;
    }

    public FollowerList followerProfileId(Long followerProfileId) {
        this.followerProfileId = followerProfileId;
        return this;
    }

    public void setFollowerProfileId(Long followerProfileId) {
        this.followerProfileId = followerProfileId;
    }

    public Instant getFollowedDate() {
        return followedDate;
    }

    public FollowerList followedDate(Instant followedDate) {
        this.followedDate = followedDate;
        return this;
    }

    public void setFollowedDate(Instant followedDate) {
        this.followedDate = followedDate;
    }

    public Boolean isStatus() {
        return status;
    }

    public FollowerList status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public FollowerList profile(Profile profile) {
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
        FollowerList followerList = (FollowerList) o;
        if (followerList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followerList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowerList{" +
            "id=" + getId() +
            ", followerProfileId=" + getFollowerProfileId() +
            ", followedDate='" + getFollowedDate() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
