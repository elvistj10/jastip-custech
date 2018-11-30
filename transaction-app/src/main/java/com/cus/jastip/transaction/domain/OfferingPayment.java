package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cus.jastip.transaction.domain.enumeration.PaymentMethod;

/**
 * A OfferingPayment.
 */
@Entity
@Table(name = "offering_payment")
@Document(indexName = "offeringpayment")
public class OfferingPayment extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "final_price_item")
    private Double finalPriceItem;

    @DecimalMin(value = "0")
    @Column(name = "final_service_fee")
    private Double finalServiceFee;

    @DecimalMin(value = "0")
    @Column(name = "final_jastip_fee")
    private Double finalJastipFee;

    @DecimalMin(value = "0")
    @Column(name = "unique_identifier")
    private Double uniqueIdentifier;

    @Min(value = 0)
    @Column(name = "final_total_fee")
    private Integer finalTotalFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_confirm_date_time")
    private Instant paymentConfirmDateTime;

    @OneToOne
    @JoinColumn(unique = true)
    private Offering offering;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFinalPriceItem() {
        return finalPriceItem;
    }

    public OfferingPayment finalPriceItem(Double finalPriceItem) {
        this.finalPriceItem = finalPriceItem;
        return this;
    }

    public void setFinalPriceItem(Double finalPriceItem) {
        this.finalPriceItem = finalPriceItem;
    }

    public Double getFinalServiceFee() {
        return finalServiceFee;
    }

    public OfferingPayment finalServiceFee(Double finalServiceFee) {
        this.finalServiceFee = finalServiceFee;
        return this;
    }

    public void setFinalServiceFee(Double finalServiceFee) {
        this.finalServiceFee = finalServiceFee;
    }

    public Double getFinalJastipFee() {
        return finalJastipFee;
    }

    public OfferingPayment finalJastipFee(Double finalJastipFee) {
        this.finalJastipFee = finalJastipFee;
        return this;
    }

    public void setFinalJastipFee(Double finalJastipFee) {
        this.finalJastipFee = finalJastipFee;
    }

    public Double getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public OfferingPayment uniqueIdentifier(Double uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
        return this;
    }

    public void setUniqueIdentifier(Double uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Integer getFinalTotalFee() {
        return finalTotalFee;
    }

    public OfferingPayment finalTotalFee(Integer finalTotalFee) {
        this.finalTotalFee = finalTotalFee;
        return this;
    }

    public void setFinalTotalFee(Integer finalTotalFee) {
        this.finalTotalFee = finalTotalFee;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public OfferingPayment paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getPaymentConfirmDateTime() {
        return paymentConfirmDateTime;
    }

    public OfferingPayment paymentConfirmDateTime(Instant paymentConfirmDateTime) {
        this.paymentConfirmDateTime = paymentConfirmDateTime;
        return this;
    }

    public void setPaymentConfirmDateTime(Instant paymentConfirmDateTime) {
        this.paymentConfirmDateTime = paymentConfirmDateTime;
    }

    public Offering getOffering() {
        return offering;
    }

    public OfferingPayment offering(Offering offering) {
        this.offering = offering;
        return this;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
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
        OfferingPayment offeringPayment = (OfferingPayment) o;
        if (offeringPayment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offeringPayment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferingPayment{" +
            "id=" + getId() +
            ", finalPriceItem=" + getFinalPriceItem() +
            ", finalServiceFee=" + getFinalServiceFee() +
            ", finalJastipFee=" + getFinalJastipFee() +
            ", uniqueIdentifier=" + getUniqueIdentifier() +
            ", finalTotalFee=" + getFinalTotalFee() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentConfirmDateTime='" + getPaymentConfirmDateTime() + "'" +
            "}";
    }
}
