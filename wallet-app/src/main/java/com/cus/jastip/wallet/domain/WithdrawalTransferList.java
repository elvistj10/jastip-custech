package com.cus.jastip.wallet.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WithdrawalTransferList.
 */
@Entity
@Table(name = "withdrawal_transfer_list")
@Document(indexName = "withdrawaltransferlist")
public class WithdrawalTransferList extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "withdrawal_id")
    private Long withdrawalId;

    @Column(name = "nominal")
    private Double nominal;

    @Column(name = "dest_bank_name")
    private String destBankName;

    @Column(name = "dest_bank_account")
    private String destBankAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public WithdrawalTransferList withdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
        return this;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public Double getNominal() {
        return nominal;
    }

    public WithdrawalTransferList nominal(Double nominal) {
        this.nominal = nominal;
        return this;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public String getDestBankName() {
        return destBankName;
    }

    public WithdrawalTransferList destBankName(String destBankName) {
        this.destBankName = destBankName;
        return this;
    }

    public void setDestBankName(String destBankName) {
        this.destBankName = destBankName;
    }

    public String getDestBankAccount() {
        return destBankAccount;
    }

    public WithdrawalTransferList destBankAccount(String destBankAccount) {
        this.destBankAccount = destBankAccount;
        return this;
    }

    public void setDestBankAccount(String destBankAccount) {
        this.destBankAccount = destBankAccount;
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
        WithdrawalTransferList withdrawalTransferList = (WithdrawalTransferList) o;
        if (withdrawalTransferList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), withdrawalTransferList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WithdrawalTransferList{" +
            "id=" + getId() +
            ", withdrawalId=" + getWithdrawalId() +
            ", nominal=" + getNominal() +
            ", destBankName='" + getDestBankName() + "'" +
            ", destBankAccount='" + getDestBankAccount() + "'" +
            "}";
    }
}
