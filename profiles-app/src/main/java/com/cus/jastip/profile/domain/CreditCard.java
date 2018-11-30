package com.cus.jastip.profile.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A CreditCard.
 */
@Entity
@Table(name = "credit_card")
@Document(indexName = "creditcard")
public class CreditCard extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cc_number", nullable = false)
    private String ccNumber;

    @NotNull
    @Column(name = "cvc", nullable = false)
    private String cvc;

    @NotNull
    @Column(name = "expire_mon", nullable = false)
    private Instant expireMon;

    @NotNull
    @Column(name = "expire_year", nullable = false)
    private Instant expireYear;

    @Column(name = "default_credit_card")
    private Boolean defaultCreditCard;

    @ManyToOne
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public CreditCard ccNumber(String ccNumber) {
        this.ccNumber = ccNumber;
        return this;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public CreditCard cvc(String cvc) {
        this.cvc = cvc;
        return this;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public Instant getExpireMon() {
        return expireMon;
    }

    public CreditCard expireMon(Instant expireMon) {
        this.expireMon = expireMon;
        return this;
    }

    public void setExpireMon(Instant expireMon) {
        this.expireMon = expireMon;
    }

    public Instant getExpireYear() {
        return expireYear;
    }

    public CreditCard expireYear(Instant expireYear) {
        this.expireYear = expireYear;
        return this;
    }

    public void setExpireYear(Instant expireYear) {
        this.expireYear = expireYear;
    }

    public Boolean isDefaultCreditCard() {
        return defaultCreditCard;
    }

    public CreditCard defaultCreditCard(Boolean defaultCreditCard) {
        this.defaultCreditCard = defaultCreditCard;
        return this;
    }

    public void setDefaultCreditCard(Boolean defaultCreditCard) {
        this.defaultCreditCard = defaultCreditCard;
    }

    public Profile getProfile() {
        return profile;
    }

    public CreditCard profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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
        CreditCard creditCard = (CreditCard) o;
        if (creditCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creditCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CreditCard{" +
            "id=" + getId() +
            ", ccNumber='" + getCcNumber() + "'" +
            ", cvc='" + getCvc() + "'" +
            ", expireMon='" + getExpireMon() + "'" +
            ", expireYear='" + getExpireYear() + "'" +
            ", defaultCreditCard='" + isDefaultCreditCard() + "'" +
            "}";
    }
}
