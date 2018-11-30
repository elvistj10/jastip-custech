package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OfferingDeliveryInfo.
 */
@Entity
@Table(name = "offering_delivery_info")
@Document(indexName = "offeringdeliveryinfo")
public class OfferingDeliveryInfo extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

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

    public String getRecipientName() {
        return recipientName;
    }

    public OfferingDeliveryInfo recipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public OfferingDeliveryInfo phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OfferingDeliveryInfo deliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Offering getOffering() {
        return offering;
    }

    public OfferingDeliveryInfo offering(Offering offering) {
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
        OfferingDeliveryInfo offeringDeliveryInfo = (OfferingDeliveryInfo) o;
        if (offeringDeliveryInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offeringDeliveryInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferingDeliveryInfo{" +
            "id=" + getId() +
            ", recipientName='" + getRecipientName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", deliveryAddress='" + getDeliveryAddress() + "'" +
            "}";
    }
}
