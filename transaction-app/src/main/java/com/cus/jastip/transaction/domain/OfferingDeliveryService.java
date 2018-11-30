package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OfferingDeliveryService.
 */
@Entity
@Table(name = "offering_delivery_service")
@Document(indexName = "offeringdeliveryservice")
public class OfferingDeliveryService extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sent_to_country_id", nullable = false)
    private Long sentToCountryId;

    @NotNull
    @Column(name = "sent_to_country_name", nullable = false)
    private String sentToCountryName;

    @NotNull
    @Column(name = "sent_to_city_id", nullable = false)
    private Long sentToCityId;

    @NotNull
    @Column(name = "sent_to_city_name", nullable = false)
    private String sentToCityName;

    @Column(name = "package_size_id")
    private Long packageSizeId;

    @Column(name = "package_size_name")
    private String packageSizeName;

    @Column(name = "delivery_service_id")
    private Long deliveryServiceId;

    @Column(name = "delivery_service_name")
    private String deliveryServiceName;

    @DecimalMin(value = "0")
    @Column(name = "delivery_fee")
    private Double deliveryFee;

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

    public Long getSentToCountryId() {
        return sentToCountryId;
    }

    public OfferingDeliveryService sentToCountryId(Long sentToCountryId) {
        this.sentToCountryId = sentToCountryId;
        return this;
    }

    public void setSentToCountryId(Long sentToCountryId) {
        this.sentToCountryId = sentToCountryId;
    }

    public String getSentToCountryName() {
        return sentToCountryName;
    }

    public OfferingDeliveryService sentToCountryName(String sentToCountryName) {
        this.sentToCountryName = sentToCountryName;
        return this;
    }

    public void setSentToCountryName(String sentToCountryName) {
        this.sentToCountryName = sentToCountryName;
    }

    public Long getSentToCityId() {
        return sentToCityId;
    }

    public OfferingDeliveryService sentToCityId(Long sentToCityId) {
        this.sentToCityId = sentToCityId;
        return this;
    }

    public void setSentToCityId(Long sentToCityId) {
        this.sentToCityId = sentToCityId;
    }

    public String getSentToCityName() {
        return sentToCityName;
    }

    public OfferingDeliveryService sentToCityName(String sentToCityName) {
        this.sentToCityName = sentToCityName;
        return this;
    }

    public void setSentToCityName(String sentToCityName) {
        this.sentToCityName = sentToCityName;
    }

    public Long getPackageSizeId() {
        return packageSizeId;
    }

    public OfferingDeliveryService packageSizeId(Long packageSizeId) {
        this.packageSizeId = packageSizeId;
        return this;
    }

    public void setPackageSizeId(Long packageSizeId) {
        this.packageSizeId = packageSizeId;
    }

    public String getPackageSizeName() {
        return packageSizeName;
    }

    public OfferingDeliveryService packageSizeName(String packageSizeName) {
        this.packageSizeName = packageSizeName;
        return this;
    }

    public void setPackageSizeName(String packageSizeName) {
        this.packageSizeName = packageSizeName;
    }

    public Long getDeliveryServiceId() {
        return deliveryServiceId;
    }

    public OfferingDeliveryService deliveryServiceId(Long deliveryServiceId) {
        this.deliveryServiceId = deliveryServiceId;
        return this;
    }

    public void setDeliveryServiceId(Long deliveryServiceId) {
        this.deliveryServiceId = deliveryServiceId;
    }

    public String getDeliveryServiceName() {
        return deliveryServiceName;
    }

    public OfferingDeliveryService deliveryServiceName(String deliveryServiceName) {
        this.deliveryServiceName = deliveryServiceName;
        return this;
    }

    public void setDeliveryServiceName(String deliveryServiceName) {
        this.deliveryServiceName = deliveryServiceName;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public OfferingDeliveryService deliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
        return this;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Offering getOffering() {
        return offering;
    }

    public OfferingDeliveryService offering(Offering offering) {
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
        OfferingDeliveryService offeringDeliveryService = (OfferingDeliveryService) o;
        if (offeringDeliveryService.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offeringDeliveryService.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferingDeliveryService{" +
            "id=" + getId() +
            ", sentToCountryId=" + getSentToCountryId() +
            ", sentToCountryName='" + getSentToCountryName() + "'" +
            ", sentToCityId=" + getSentToCityId() +
            ", sentToCityName='" + getSentToCityName() + "'" +
            ", packageSizeId=" + getPackageSizeId() +
            ", packageSizeName='" + getPackageSizeName() + "'" +
            ", deliveryServiceId=" + getDeliveryServiceId() +
            ", deliveryServiceName='" + getDeliveryServiceName() + "'" +
            ", deliveryFee=" + getDeliveryFee() +
            "}";
    }
}
