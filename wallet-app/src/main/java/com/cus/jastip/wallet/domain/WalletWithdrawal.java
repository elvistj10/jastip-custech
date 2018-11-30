package com.cus.jastip.wallet.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cus.jastip.wallet.domain.enumeration.WithdrawalStatus;

/**
 * A WalletWithdrawal.
 */
@Entity
@Table(name = "wallet_withdrawal")
@Document(indexName = "walletwithdrawal")
public class WalletWithdrawal extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "request_date_time")
    private Instant requestDateTime;

    @Column(name = "nominal")
    private Double nominal;

    @Column(name = "dest_bank_name")
    private String destBankName;

    @Column(name = "dest_bank_account")
    private String destBankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WithdrawalStatus status;

    @Column(name = "completed_date_time")
    private Instant completedDateTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public WalletWithdrawal ownerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Instant getRequestDateTime() {
        return requestDateTime;
    }

    public WalletWithdrawal requestDateTime(Instant requestDateTime) {
        this.requestDateTime = requestDateTime;
        return this;
    }

    public void setRequestDateTime(Instant requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public Double getNominal() {
        return nominal;
    }

    public WalletWithdrawal nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public String getDestBankName() {
        return destBankName;
    }

    public WalletWithdrawal destBankName(String destBankName) {
        this.destBankName = destBankName;
        return this;
    }

    public void setDestBankName(String destBankName) {
        this.destBankName = destBankName;
    }

    public String getDestBankAccount() {
        return destBankAccount;
    }

    public WalletWithdrawal destBankAccount(String destBankAccount) {
        this.destBankAccount = destBankAccount;
        return this;
    }

    public void setDestBankAccount(String destBankAccount) {
        this.destBankAccount = destBankAccount;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public WalletWithdrawal status(WithdrawalStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public Instant getCompletedDateTime() {
        return completedDateTime;
    }

    public WalletWithdrawal completedDateTime(Instant completedDateTime) {
        this.completedDateTime = completedDateTime;
        return this;
    }

    public void setCompletedDateTime(Instant completedDateTime) {
        this.completedDateTime = completedDateTime;
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
        WalletWithdrawal walletWithdrawal = (WalletWithdrawal) o;
        if (walletWithdrawal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), walletWithdrawal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WalletWithdrawal{" +
            "id=" + getId() +
            ", ownerId=" + getOwnerId() +
            ", requestDateTime='" + getRequestDateTime() + "'" +
            ", nominal=" + getNominal() +
            ", destBankName='" + getDestBankName() + "'" +
            ", destBankAccount='" + getDestBankAccount() + "'" +
            ", status='" + getStatus() + "'" +
            ", completedDateTime='" + getCompletedDateTime() + "'" +
            "}";
    }
}
