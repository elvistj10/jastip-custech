package com.cus.jastip.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.cus.jastip.wallet.domain.enumeration.WalletStatus;

/**
 * A Wallet.
 */
@Entity
@Table(name = "wallet")
@Document(indexName = "wallet")
public class Wallet extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "balance")
    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WalletStatus status;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    private Set<WalletTransaction> walletTransactions = new HashSet<>();

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

    public Wallet ownerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getBalance() {
        return balance;
    }

    public Wallet balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public Wallet status(WalletStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public Set<WalletTransaction> getWalletTransactions() {
        return walletTransactions;
    }

    public Wallet walletTransactions(Set<WalletTransaction> walletTransactions) {
        this.walletTransactions = walletTransactions;
        return this;
    }

    public Wallet addWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.add(walletTransaction);
        walletTransaction.setWallet(this);
        return this;
    }

    public Wallet removeWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransactions.remove(walletTransaction);
        walletTransaction.setWallet(null);
        return this;
    }

    public void setWalletTransactions(Set<WalletTransaction> walletTransactions) {
        this.walletTransactions = walletTransactions;
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
        Wallet wallet = (Wallet) o;
        if (wallet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wallet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", ownerId=" + getOwnerId() +
            ", balance=" + getBalance() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
