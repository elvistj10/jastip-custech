package com.cus.jastip.payment.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A PaymentTransferHistory.
 */
@Entity
@Table(name = "payment_transfer_history")
@Document(indexName = "paymenttransferhistory")
public class PaymentTransferHistory extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "payment_confirm_date_time", nullable = false)
    private Instant paymentConfirmDateTime;

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

    public PaymentTransferHistory postingId(Long postingId) {
        this.postingId = postingId;
        return this;
    }

    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public PaymentTransferHistory offeringId(Long offeringId) {
        this.offeringId = offeringId;
        return this;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public Double getNominal() {
        return nominal;
    }

    public PaymentTransferHistory nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Instant getPaymentConfirmDateTime() {
        return paymentConfirmDateTime;
    }

    public PaymentTransferHistory paymentConfirmDateTime(Instant paymentConfirmDateTime) {
        this.paymentConfirmDateTime = paymentConfirmDateTime;
        return this;
    }

    public void setPaymentConfirmDateTime(Instant paymentConfirmDateTime) {
        this.paymentConfirmDateTime = paymentConfirmDateTime;
    }

    public Instant getCheckDateTime() {
        return checkDateTime;
    }

    public PaymentTransferHistory checkDateTime(Instant checkDateTime) {
        this.checkDateTime = checkDateTime;
        return this;
    }

    public void setCheckDateTime(Instant checkDateTime) {
        this.checkDateTime = checkDateTime;
    }

    public Instant getExpiredDateTime() {
        return expiredDateTime;
    }

    public PaymentTransferHistory expiredDateTime(Instant expiredDateTime) {
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
        PaymentTransferHistory paymentTransferHistory = (PaymentTransferHistory) o;
        if (paymentTransferHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentTransferHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentTransferHistory{" +
            "id=" + getId() +
            ", postingId=" + getPostingId() +
            ", offeringId=" + getOfferingId() +
            ", nominal=" + getNominal() +
            ", paymentConfirmDateTime='" + getPaymentConfirmDateTime() + "'" +
            ", checkDateTime='" + getCheckDateTime() + "'" +
            ", expiredDateTime='" + getExpiredDateTime() + "'" +
            "}";
    }
}
