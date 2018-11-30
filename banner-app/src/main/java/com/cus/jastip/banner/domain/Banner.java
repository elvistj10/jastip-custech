package com.cus.jastip.banner.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.cus.jastip.banner.domain.enumeration.BannerType;

import com.cus.jastip.banner.domain.enumeration.PostingType;

/**
 * A Banner.
 */
@Entity
@Table(name = "banner")
@Document(indexName = "banner")
public class Banner extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banner_title")
    private String bannerTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "banner_type")
    private BannerType bannerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "posting_type")
    private PostingType postingType;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @Column(name = "query")
    private String query;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public Banner bannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
        return this;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public BannerType getBannerType() {
        return bannerType;
    }

    public Banner bannerType(BannerType bannerType) {
        this.bannerType = bannerType;
        return this;
    }

    public void setBannerType(BannerType bannerType) {
        this.bannerType = bannerType;
    }

    public PostingType getPostingType() {
        return postingType;
    }

    public Banner postingType(PostingType postingType) {
        this.postingType = postingType;
        return this;
    }

    public void setPostingType(PostingType postingType) {
        this.postingType = postingType;
    }

    public byte[] getImage() {
        return image;
    }

    public Banner image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Banner imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Banner imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Banner startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Banner endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public Banner description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public Banner notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQuery() {
        return query;
    }

    public Banner query(String query) {
        this.query = query;
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
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
        Banner banner = (Banner) o;
        if (banner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), banner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Banner{" +
            "id=" + getId() +
            ", bannerTitle='" + getBannerTitle() + "'" +
            ", bannerType='" + getBannerType() + "'" +
            ", postingType='" + getPostingType() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", query='" + getQuery() + "'" +
            "}";
    }
}
