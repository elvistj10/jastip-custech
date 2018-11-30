package com.cus.jastip.profile.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A FeedbackResponse.
 */
@Entity
@Table(name = "feedback_response")
@Document(indexName = "feedbackresponse")
public class FeedbackResponse extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "response_date_time")
    private Instant responseDateTime;

    @OneToOne(mappedBy = "response")
    @JsonIgnore
    private Feedback feedback;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public FeedbackResponse message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getResponseDateTime() {
        return responseDateTime;
    }

    public FeedbackResponse responseDateTime(Instant responseDateTime) {
        this.responseDateTime = responseDateTime;
        return this;
    }

    public void setResponseDateTime(Instant responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public FeedbackResponse feedback(Feedback feedback) {
        this.feedback = feedback;
        return this;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
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
        FeedbackResponse feedbackResponse = (FeedbackResponse) o;
        if (feedbackResponse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feedbackResponse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeedbackResponse{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", responseDateTime='" + getResponseDateTime() + "'" +
            "}";
    }
}
