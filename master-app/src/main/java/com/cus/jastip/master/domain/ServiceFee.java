package com.cus.jastip.master.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ServiceFee.
 */
@Entity
@Table(name = "service_fee")
@Document(indexName = "servicefee")
public class ServiceFee extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "percentage_fee")
    private Double percentageFee;

    @Column(name = "fix_nominal_fee")
    private Double fixNominalFee;

    @Column(name = "minimum_nominal_fee")
    private Double minimumNominalFee;

    @Column(name = "minimum_nominal_price")
    private Double minimumNominalPrice;

    @Column(name = "maximum_nominal_price")
    private Double maximumNominalPrice;

    @NotNull
    @Column(name = "start_date_time", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_date_time")
    private Instant endDateTime;

    @Column(name = "active_status")
    private Boolean activeStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentageFee() {
        return percentageFee;
    }

    public ServiceFee percentageFee(Double percentageFee) {
        this.percentageFee = percentageFee;
        return this;
    }

    public void setPercentageFee(Double percentageFee) {
        this.percentageFee = percentageFee;
    }

    public Double getFixNominalFee() {
        return fixNominalFee;
    }

    public ServiceFee fixNominalFee(Double fixNominalFee) {
        this.fixNominalFee = fixNominalFee;
        return this;
    }

    public void setFixNominalFee(Double fixNominalFee) {
        this.fixNominalFee = fixNominalFee;
    }

    public Double getMinimumNominalFee() {
        return minimumNominalFee;
    }

    public ServiceFee minimumNominalFee(Double minimumNominalFee) {
        this.minimumNominalFee = minimumNominalFee;
        return this;
    }

    public void setMinimumNominalFee(Double minimumNominalFee) {
        this.minimumNominalFee = minimumNominalFee;
    }

    public Double getMinimumNominalPrice() {
        return minimumNominalPrice;
    }

    public ServiceFee minimumNominalPrice(Double minimumNominalPrice) {
        this.minimumNominalPrice = minimumNominalPrice;
        return this;
    }

    public void setMinimumNominalPrice(Double minimumNominalPrice) {
        this.minimumNominalPrice = minimumNominalPrice;
    }

    public Double getMaximumNominalPrice() {
        return maximumNominalPrice;
    }

    public ServiceFee maximumNominalPrice(Double maximumNominalPrice) {
        this.maximumNominalPrice = maximumNominalPrice;
        return this;
    }

    public void setMaximumNominalPrice(Double maximumNominalPrice) {
        this.maximumNominalPrice = maximumNominalPrice;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public ServiceFee startDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public ServiceFee endDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Boolean isActiveStatus() {
        return activeStatus;
    }

    public ServiceFee activeStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
        return this;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
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
        ServiceFee serviceFee = (ServiceFee) o;
        if (serviceFee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceFee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceFee{" +
            "id=" + getId() +
            ", percentageFee=" + getPercentageFee() +
            ", fixNominalFee=" + getFixNominalFee() +
            ", minimumNominalFee=" + getMinimumNominalFee() +
            ", minimumNominalPrice=" + getMinimumNominalPrice() +
            ", maximumNominalPrice=" + getMaximumNominalPrice() +
            ", startDateTime='" + getStartDateTime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            ", activeStatus='" + isActiveStatus() + "'" +
            "}";
    }
}
