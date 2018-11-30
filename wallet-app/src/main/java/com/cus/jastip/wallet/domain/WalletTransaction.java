package com.cus.jastip.wallet.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cus.jastip.wallet.domain.enumeration.WalletTransactionType;

/**
 * A WalletTransaction.
 */
@Entity
@Table(name = "wallet_transaction")
@Document(indexName = "wallettransaction")
public class WalletTransaction extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date_time")
    private Instant transactionDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private WalletTransactionType transactionType;

    @Column(name = "nominal")
    private Double nominal;

    @Column(name = "posting_id")
    private Long postingId;

    @Column(name = "offering_id")
    private Long offeringId;

    @Column(name = "withdrawal_id")
    private Long withdrawalId;

    @ManyToOne
    private Wallet wallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTransactionDateTime() {
        return transactionDateTime;
    }

    public WalletTransaction transactionDateTime(Instant transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
        return this;
    }

    public void setTransactionDateTime(Instant transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public WalletTransactionType getTransactionType() {
        return transactionType;
    }

    public WalletTransaction transactionType(WalletTransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(WalletTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getNominal() {
        return nominal;
    }

    public WalletTransaction nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Long getPostingId() {
        return postingId;
    }

    public WalletTransaction postingId(Long postingId) {
        this.postingId = postingId;
        return this;
    }

    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public WalletTransaction offeringId(Long offeringId) {
        this.offeringId = offeringId;
        return this;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public WalletTransaction withdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
        return this;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public WalletTransaction wallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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
        WalletTransaction walletTransaction = (WalletTransaction) o;
        if (walletTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), walletTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WalletTransaction{" +
            "id=" + getId() +
            ", transactionDateTime='" + getTransactionDateTime() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", nominal=" + getNominal() +
            ", postingId=" + getPostingId() +
            ", offeringId=" + getOfferingId() +
            ", withdrawalId=" + getWithdrawalId() +
            "}";
    }
}
