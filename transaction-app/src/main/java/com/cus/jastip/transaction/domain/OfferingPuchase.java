package com.cus.jastip.transaction.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OfferingPuchase.
 */
@Entity
@Table(name = "offering_puchase")
@Document(indexName = "offeringpuchase")
public class OfferingPuchase extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchased")
    private Boolean purchased;

    @Lob
    @Column(name = "receipt_img")
    private byte[] receiptImg;

    @Column(name = "receipt_img_content_type")
    private String receiptImgContentType;

    @Column(name = "receipt_img_url")
    private String receiptImgUrl;

    @Column(name = "receipt_img_thumb_url")
    private String receiptImgThumbUrl;

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

    public Boolean isPurchased() {
        return purchased;
    }

    public OfferingPuchase purchased(Boolean purchased) {
        this.purchased = purchased;
        return this;
    }

    public void setPurchased(Boolean purchased) {
        this.purchased = purchased;
    }

    public byte[] getReceiptImg() {
        return receiptImg;
    }

    public OfferingPuchase receiptImg(byte[] receiptImg) {
        this.receiptImg = receiptImg;
        return this;
    }

    public void setReceiptImg(byte[] receiptImg) {
        this.receiptImg = receiptImg;
    }

    public String getReceiptImgContentType() {
        return receiptImgContentType;
    }

    public OfferingPuchase receiptImgContentType(String receiptImgContentType) {
        this.receiptImgContentType = receiptImgContentType;
        return this;
    }

    public void setReceiptImgContentType(String receiptImgContentType) {
        this.receiptImgContentType = receiptImgContentType;
    }

    public String getReceiptImgUrl() {
        return receiptImgUrl;
    }

    public OfferingPuchase receiptImgUrl(String receiptImgUrl) {
        this.receiptImgUrl = receiptImgUrl;
        return this;
    }

    public void setReceiptImgUrl(String receiptImgUrl) {
        this.receiptImgUrl = receiptImgUrl;
    }

    public String getReceiptImgThumbUrl() {
        return receiptImgThumbUrl;
    }

    public OfferingPuchase receiptImgThumbUrl(String receiptImgThumbUrl) {
        this.receiptImgThumbUrl = receiptImgThumbUrl;
        return this;
    }

    public void setReceiptImgThumbUrl(String receiptImgThumbUrl) {
        this.receiptImgThumbUrl = receiptImgThumbUrl;
    }

    public Offering getOffering() {
        return offering;
    }

    public OfferingPuchase offering(Offering offering) {
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
        OfferingPuchase offeringPuchase = (OfferingPuchase) o;
        if (offeringPuchase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offeringPuchase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferingPuchase{" +
            "id=" + getId() +
            ", purchased='" + isPurchased() + "'" +
            ", receiptImg='" + getReceiptImg() + "'" +
            ", receiptImgContentType='" + getReceiptImgContentType() + "'" +
            ", receiptImgUrl='" + getReceiptImgUrl() + "'" +
            ", receiptImgThumbUrl='" + getReceiptImgThumbUrl() + "'" +
            "}";
    }
}
