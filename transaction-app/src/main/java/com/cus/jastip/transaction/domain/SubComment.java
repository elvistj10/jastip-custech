package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A SubComment.
 */
@Entity
@Table(name = "sub_comment")
@Document(indexName = "subcomment")
public class SubComment extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @NotNull
    @Column(name = "sub_comment", nullable = false)
    private String subComment;

    @Column(name = "sub_comment_date_time")
    private Instant subCommentDateTime;

    @ManyToOne
    private Comment comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public SubComment profileId(Long profileId) {
        this.profileId = profileId;
        return this;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getSubComment() {
        return subComment;
    }

    public SubComment subComment(String subComment) {
        this.subComment = subComment;
        return this;
    }

    public void setSubComment(String subComment) {
        this.subComment = subComment;
    }

    public Instant getSubCommentDateTime() {
        return subCommentDateTime;
    }

    public SubComment subCommentDateTime(Instant subCommentDateTime) {
        this.subCommentDateTime = subCommentDateTime;
        return this;
    }

    public void setSubCommentDateTime(Instant subCommentDateTime) {
        this.subCommentDateTime = subCommentDateTime;
    }

    public Comment getComment() {
        return comment;
    }

    public SubComment comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
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
        SubComment subComment = (SubComment) o;
        if (subComment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subComment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubComment{" +
            "id=" + getId() +
            ", profileId=" + getProfileId() +
            ", subComment='" + getSubComment() + "'" +
            ", subCommentDateTime='" + getSubCommentDateTime() + "'" +
            "}";
    }
}
