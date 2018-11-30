package com.cus.jastip.master.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Document(indexName = "country")
public class Country extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_thumb_url")
    private String imageThumbUrl;

    @Lob
    @Column(name = "image_flag")
    private byte[] imageFlag;

    @Column(name = "image_flag_content_type")
    private String imageFlagContentType;

    @Column(name = "image_flag_url")
    private String imageFlagUrl;

    @Column(name = "image_flag_thumb_url")
    private String imageFlagThumbUrl;

    @NotNull
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public Country countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public byte[] getImage() {
        return image;
    }

    public Country image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Country imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Country imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public Country imageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
        return this;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public byte[] getImageFlag() {
        return imageFlag;
    }

    public Country imageFlag(byte[] imageFlag) {
        this.imageFlag = imageFlag;
        return this;
    }

    public void setImageFlag(byte[] imageFlag) {
        this.imageFlag = imageFlag;
    }

    public String getImageFlagContentType() {
        return imageFlagContentType;
    }

    public Country imageFlagContentType(String imageFlagContentType) {
        this.imageFlagContentType = imageFlagContentType;
        return this;
    }

    public void setImageFlagContentType(String imageFlagContentType) {
        this.imageFlagContentType = imageFlagContentType;
    }

    public String getImageFlagUrl() {
        return imageFlagUrl;
    }

    public Country imageFlagUrl(String imageFlagUrl) {
        this.imageFlagUrl = imageFlagUrl;
        return this;
    }

    public void setImageFlagUrl(String imageFlagUrl) {
        this.imageFlagUrl = imageFlagUrl;
    }

    public String getImageFlagThumbUrl() {
        return imageFlagThumbUrl;
    }

    public Country imageFlagThumbUrl(String imageFlagThumbUrl) {
        this.imageFlagThumbUrl = imageFlagThumbUrl;
        return this;
    }

    public void setImageFlagThumbUrl(String imageFlagThumbUrl) {
        this.imageFlagThumbUrl = imageFlagThumbUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Country countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
        Country country = (Country) o;
        if (country.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), country.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", countryName='" + getCountryName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", imageThumbUrl='" + getImageThumbUrl() + "'" +
            ", imageFlag='" + getImageFlag() + "'" +
            ", imageFlagContentType='" + getImageFlagContentType() + "'" +
            ", imageFlagUrl='" + getImageFlagUrl() + "'" +
            ", imageFlagThumbUrl='" + getImageFlagThumbUrl() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            "}";
    }
}
