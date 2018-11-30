package com.cus.jastip.payment.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A PaymentTransferUnmatched.
 */
@Entity
@Table(name = "payment_transfer_unmatched")
@Document(indexName = "paymenttransferunmatched")
public class PaymentTransferUnmatched extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "posting_id", nullable = false)
    private Long postingId;

    @NotNull
    @Column(name = "offering_id", nullable = false)
    private Long offeringId;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "nominal", nullable = false)
    private Double nominal;

    @NotNull
    @Column(name = "payment_unmatched_date_time", nullable = false)
    private Instant paymentUnmatchedDateTime;

    @NotNull
    @Column(name = "check_date_time", nullable = false)
    private Instant checkDateTime;

    @NotNull
    @Column(name = "expired_date_time", nullable = false)
    private Instant expiredDateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostingId() {
        return postingId;
    }

    public PaymentTransferUnmatched postingId(Long postingId) {
        this.postingId = postingId;
        return this;
    }

    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public PaymentTransferUnmatched offeringId(Long offeringId) {
        this.offeringId = offeringId;
        return this;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public Double getNominal() {
        return nominal;
    }

    public PaymentTransferUnmatched nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Instant getPaymentUnmatchedDateTime() {
        return paymentUnmatchedDateTime;
    }

    public PaymentTransferUnmatched paymentUnmatchedDateTime(Instant paymentUnmatchedDateTime) {
        this.paymentUnmatchedDateTime = paymentUnmatchedDateTime;
        return this;
    }

    public void setPaymentUnmatchedDateTime(Instant paymentUnmatchedDateTime) {
        this.paymentUnmatchedDateTime = paymentUnmatchedDateTime;
    }

    public Instant getCheckDateTime() {
        return checkDateTime;
    }

    public PaymentTransferUnmatched checkDateTime(Instant checkDateTime) {
        this.checkDateTime = checkDateTime;
        return this;
    }

    public void setCheckDateTime(Instant checkDateTime) {
        this.checkDateTime = checkDateTime;
    }

    public Instant getExpiredDateTime() {
        return expiredDateTime;
    }

    public PaymentTransferUnmatched expiredDateTime(Instant expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
        return this;
    }

    public void setExpiredDateTime(Instant expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
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
        PaymentTransferUnmatched paymentTransferUnmatched = (PaymentTransferUnmatched) o;
        if (paymentTransferUnmatched.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentTransferUnmatched.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentTransferUnmatched{" +
            "id=" + getId() +
            ", postingId=" + getPostingId() +
            ", offeringId=" + getOfferingId() +
            ", nominal=" + getNominal() +
            ", paymentUnmatchedDateTime='" + getPaymentUnmatchedDateTime() + "'" +
            ", checkDateTime='" + getCheckDateTime() + "'" +
            ", expiredDateTime='" + getExpiredDateTime() + "'" +
            "}";
    }
}
