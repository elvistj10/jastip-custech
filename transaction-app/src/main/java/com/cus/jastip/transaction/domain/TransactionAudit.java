package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TransactionAudit.
 */
@Entity
@Table(name = "transaction_audit")
@Document(indexName = "transactionaudit")
public class TransactionAudit extends PersistentAuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
*/
    @NotNull
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

   /* // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;*/
   /* }*/

    public String getEntityName() {
        return entityName;
    }

    public TransactionAudit entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public TransactionAudit entityId(Long entityId) {
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
        TransactionAudit transactionAudit = (TransactionAudit) o;
        if (transactionAudit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionAudit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionAudit{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", entityId=" + getEntityId() +
            "}";
    }
}
