package com.cus.jastip.payment.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cus.jastip.payment.domain.enumeration.PaymentStatus;

import com.cus.jastip.payment.domain.enumeration.PaymentMethod;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Document(indexName = "payment")
public class Payment extends AbstractAuditingEntity implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "nominal", nullable = false)
    private Double nominal;

    @NotNull
    @Column(name = "payment_date_time", nullable = false)
    private Instant paymentDateTime;

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

    public Payment postingId(Long postingId) {
        this.postingId = postingId;
        return this;
    }

    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public Payment offeringId(Long offeringId) {
        this.offeringId = offeringId;
        return this;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Payment paymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Payment paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getNominal() {
        return nominal;
    }

    public Payment nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Instant getPaymentDateTime() {
        return paymentDateTime;
    }

    public Payment paymentDateTime(Instant paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
        return this;
    }

    public void setPaymentDateTime(Instant paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", postingId=" + getPostingId() +
            ", offeringId=" + getOfferingId() +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", nominal=" + getNominal() +
            ", paymentDateTime='" + getPaymentDateTime() + "'" +
            "}";
    }
}
