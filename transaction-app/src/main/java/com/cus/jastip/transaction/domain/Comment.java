package com.cus.jastip.transaction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Document(indexName = "comment")
public class Comment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @NotNull
    @Column(name = "jhi_comment", nullable = false)
    private String comment;

    @Column(name = "comment_date_time")
    private Instant commentDateTime;

    @ManyToOne
    private Posting posting;

    @OneToMany(mappedBy = "comment")
    @JsonIgnore
    private Set<SubComment> subComments = new HashSet<>();

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

    public Comment profileId(Long profileId) {
        this.profileId = profileId;
        return this;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getComment() {
        return comment;
    }

    public Comment comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCommentDateTime() {
        return commentDateTime;
    }

    public Comment commentDateTime(Instant commentDateTime) {
        this.commentDateTime = commentDateTime;
        return this;
    }

    public void setCommentDateTime(Instant commentDateTime) {
        this.commentDateTime = commentDateTime;
    }

    public Posting getPosting() {
        return posting;
    }

    public Comment posting(Posting posting) {
        this.posting = posting;
        return this;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }

    public Set<SubComment> getSubComments() {
        return subComments;
    }

    public Comment subComments(Set<SubComment> subComments) {
        this.subComments = subComments;
        return this;
    }

    public Comment addSubComment(SubComment subComment) {
        this.subComments.add(subComment);
        subComment.setComment(this);
        return this;
    }

    public Comment removeSubComment(SubComment subComment) {
        this.subComments.remove(subComment);
        subComment.setComment(null);
        return this;
    }

    public void setSubComments(Set<SubComment> subComments) {
        this.subComments = subComments;
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
        Comment comment = (Comment) o;
        if (comment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", profileId=" + getProfileId() +
            ", comment='" + getComment() + "'" +
            ", commentDateTime='" + getCommentDateTime() + "'" +
            "}";
    }
}
