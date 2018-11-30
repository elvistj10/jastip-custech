package com.cus.jastip.payment.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PaymentAudit.
 */
@Entity
@Table(name = "payment_audit")
@Document(indexName = "paymentaudit")
public class PaymentAudit extends PersistentAuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
   

    public String getEntityName() {
        return entityName;
    }

    public PaymentAudit entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public PaymentAudit entityId(Long entityId) {
        this.entityId = entityId;
        return this;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
        PaymentAudit paymentAudit = (PaymentAudit) o;
        if (paymentAudit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentAudit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentAudit{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", entityId=" + getEntityId() +
            "}";
    }
}
