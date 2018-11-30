package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Document(indexName = "feedback")
public class Feedback extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "feedbacker_id", nullable = false)
    private Long feedbackerId;

    @NotNull
    @Column(name = "posting_id", nullable = false)
    private Long postingId;

    @NotNull
    @Column(name = "offering_id", nullable = false)
    private Long offeringId;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "feedback_date_time")
    private Instant feedbackDateTime;

    @OneToOne
    @JoinColumn(unique = true)
    private FeedbackResponse response;

    @ManyToOne
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackerId() {
        return feedbackerId;
    }

    public Feedback feedbackerId(Long feedbackerId) {
        this.feedbackerId = feedbackerId;
        return this;
    }

    public void setFeedbackerId(Long feedbackerId) {
        this.feedbackerId = feedbackerId;
    }

    public Long getPostingId() {
        return postingId;
    }

    public Feedback postingId(Long postingId) {
        this.postingId = postingId;
        return this;
    }

    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public Feedback offeringId(Long offeringId) {
        this.offeringId = offeringId;
        return this;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public Integer getRating() {
        return rating;
    }

    public Feedback rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public Feedback message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getFeedbackDateTime() {
        return feedbackDateTime;
    }

    public Feedback feedbackDateTime(Instant feedbackDateTime) {
        this.feedbackDateTime = feedbackDateTime;
        return this;
    }

    public void setFeedbackDateTime(Instant feedbackDateTime) {
        this.feedbackDateTime = feedbackDateTime;
    }

    public FeedbackResponse getResponse() {
        return response;
    }

    public Feedback response(FeedbackResponse feedbackResponse) {
        this.response = feedbackResponse;
        return this;
    }

    public void setResponse(FeedbackResponse feedbackResponse) {
        this.response = feedbackResponse;
    }

    public Profile getProfile() {
        return profile;
    }

    public Feedback profile(Profile profile) {
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
        Feedback feedback = (Feedback) o;
        if (feedback.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feedback.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", feedbackerId=" + getFeedbackerId() +
            ", postingId=" + getPostingId() +
            ", offeringId=" + getOfferingId() +
            ", rating=" + getRating() +
            ", message='" + getMessage() + "'" +
            ", feedbackDateTime='" + getFeedbackDateTime() + "'" +
            "}";
    }
}
