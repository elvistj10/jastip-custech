package com.cus.jastip.transaction.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A OfferingCourier.
 */
@Entity
@Table(name = "offering_courier")
@Document(indexName = "offeringcourier")
public class OfferingCourier extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "courier_receipt_no", nullable = false)
    private String courierReceiptNo;

    @Lob
    @Column(name = "courier_receipt_img")
    private byte[] courierReceiptImg;

    @Column(name = "courier_receipt_img_content_type")
    private String courierReceiptImgContentType;

    @Column(name = "courier_receipt_img_url")
    private String courierReceiptImgUrl;

    @Column(name = "courier_receipt_img_thumb_url")
    private String courierReceiptImgThumbUrl;

    @Column(name = "courier_send_date")
    private Instant courierSendDate;

    @Min(value = 0)
    @Column(name = "courier_delivery_day")
    private Integer courierDeliveryDay;

    @Column(name = "courier_est_delivered_date")
    private Instant courierEstDeliveredDate;

    @Column(name = "confirm_received_date_time")
    private Instant confirmReceivedDateTime;

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

    public String getCourierReceiptNo() {
        return courierReceiptNo;
    }

    public OfferingCourier courierReceiptNo(String courierReceiptNo) {
        this.courierReceiptNo = courierReceiptNo;
        return this;
    }

    public void setCourierReceiptNo(String courierReceiptNo) {
        this.courierReceiptNo = courierReceiptNo;
    }

    public byte[] getCourierReceiptImg() {
        return courierReceiptImg;
    }

    public OfferingCourier courierReceiptImg(byte[] courierReceiptImg) {
        this.courierReceiptImg = courierReceiptImg;
        return this;
    }

    public void setCourierReceiptImg(byte[] courierReceiptImg) {
        this.courierReceiptImg = courierReceiptImg;
    }

    public String getCourierReceiptImgContentType() {
        return courierReceiptImgContentType;
    }

    public OfferingCourier courierReceiptImgContentType(String courierReceiptImgContentType) {
        this.courierReceiptImgContentType = courierReceiptImgContentType;
        return this;
    }

    public void setCourierReceiptImgContentType(String courierReceiptImgContentType) {
        this.courierReceiptImgContentType = courierReceiptImgContentType;
    }

    public String getCourierReceiptImgUrl() {
        return courierReceiptImgUrl;
    }

    public OfferingCourier courierReceiptImgUrl(String courierReceiptImgUrl) {
        this.courierReceiptImgUrl = courierReceiptImgUrl;
        return this;
    }

    public void setCourierReceiptImgUrl(String courierReceiptImgUrl) {
        this.courierReceiptImgUrl = courierReceiptImgUrl;
    }

    public String getCourierReceiptImgThumbUrl() {
        return courierReceiptImgThumbUrl;
    }

    public OfferingCourier courierReceiptImgThumbUrl(String courierReceiptImgThumbUrl) {
        this.courierReceiptImgThumbUrl = courierReceiptImgThumbUrl;
        return this;
    }

    public void setCourierReceiptImgThumbUrl(String courierReceiptImgThumbUrl) {
        this.courierReceiptImgThumbUrl = courierReceiptImgThumbUrl;
    }

    public Instant getCourierSendDate() {
        return courierSendDate;
    }

    public OfferingCourier courierSendDate(Instant courierSendDate) {
        this.courierSendDate = courierSendDate;
        return this;
    }

    public void setCourierSendDate(Instant courierSendDate) {
        this.courierSendDate = courierSendDate;
    }

    public Integer getCourierDeliveryDay() {
        return courierDeliveryDay;
    }

    public OfferingCourier courierDeliveryDay(Integer courierDeliveryDay) {
        this.courierDeliveryDay = courierDeliveryDay;
        return this;
    }

    public void setCourierDeliveryDay(Integer courierDeliveryDay) {
        this.courierDeliveryDay = courierDeliveryDay;
    }

    public Instant getCourierEstDeliveredDate() {
        return courierEstDeliveredDate;
    }

    public OfferingCourier courierEstDeliveredDate(Instant courierEstDeliveredDate) {
        this.courierEstDeliveredDate = courierEstDeliveredDate;
        return this;
    }

    public void setCourierEstDeliveredDate(Instant courierEstDeliveredDate) {
        this.courierEstDeliveredDate = courierEstDeliveredDate;
    }

    public Instant getConfirmReceivedDateTime() {
        return confirmReceivedDateTime;
    }

    public OfferingCourier confirmReceivedDateTime(Instant confirmReceivedDateTime) {
        this.confirmReceivedDateTime = confirmReceivedDateTime;
        return this;
    }

    public void setConfirmReceivedDateTime(Instant confirmReceivedDateTime) {
        this.confirmReceivedDateTime = confirmReceivedDateTime;
    }

    public Offering getOffering() {
        return offering;
    }

    public OfferingCourier offering(Offering offering) {
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
        OfferingCourier offeringCourier = (OfferingCourier) o;
        if (offeringCourier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offeringCourier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferingCourier{" +
            "id=" + getId() +
            ", courierReceiptNo='" + getCourierReceiptNo() + "'" +
            ", courierReceiptImg='" + getCourierReceiptImg() + "'" +
            ", courierReceiptImgContentType='" + getCourierReceiptImgContentType() + "'" +
            ", courierReceiptImgUrl='" + getCourierReceiptImgUrl() + "'" +
            ", courierReceiptImgThumbUrl='" + getCourierReceiptImgThumbUrl() + "'" +
            ", courierSendDate='" + getCourierSendDate() + "'" +
            ", courierDeliveryDay=" + getCourierDeliveryDay() +
            ", courierEstDeliveredDate='" + getCourierEstDeliveredDate() + "'" +
            ", confirmReceivedDateTime='" + getConfirmReceivedDateTime() + "'" +
            "}";
    }
}
