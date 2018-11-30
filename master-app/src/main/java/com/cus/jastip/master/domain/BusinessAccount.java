package com.cus.jastip.master.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BusinessAccount.
 */
@Entity
@Table(name = "business_account")
@Document(indexName = "businessaccount")
public class BusinessAccount extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "coorperate_id", nullable = false)
    private String coorperateId;

    @NotNull
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoorperateId() {
        return coorperateId;
    }

    public BusinessAccount coorperateId(String coorperateId) {
        this.coorperateId = coorperateId;
        return this;
    }

    public void setCoorperateId(String coorperateId) {
        this.coorperateId = coorperateId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BusinessAccount accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public BusinessAccount bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
        BusinessAccount businessAccount = (BusinessAccount) o;
        if (businessAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), businessAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusinessAccount{" +
            "id=" + getId() +
            ", coorperateId='" + getCoorperateId() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", bankName='" + getBankName() + "'" +
            "}";
    }
}
