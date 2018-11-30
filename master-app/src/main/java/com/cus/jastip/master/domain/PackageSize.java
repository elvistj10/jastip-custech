package com.cus.jastip.master.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PackageSize.
 */
@Entity
@Table(name = "package_size")
@Document(indexName = "packagesize")
public class PackageSize extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "package_size_name", nullable = false)
    private String packageSizeName;

    @NotNull
    @Column(name = "package_size_desciption", nullable = false)
    private String packageSizeDesciption;

    @Lob
    @Column(name = "package_size_icon")
    private byte[] packageSizeIcon;

    @Column(name = "package_size_icon_content_type")
    private String packageSizeIconContentType;

    @Column(name = "package_size_icon_url")
    private String packageSizeIconUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageSizeName() {
        return packageSizeName;
    }

    public PackageSize packageSizeName(String packageSizeName) {
        this.packageSizeName = packageSizeName;
        return this;
    }

    public void setPackageSizeName(String packageSizeName) {
        this.packageSizeName = packageSizeName;
    }

    public String getPackageSizeDesciption() {
        return packageSizeDesciption;
    }

    public PackageSize packageSizeDesciption(String packageSizeDesciption) {
        this.packageSizeDesciption = packageSizeDesciption;
        return this;
    }

    public void setPackageSizeDesciption(String packageSizeDesciption) {
        this.packageSizeDesciption = packageSizeDesciption;
    }

    public byte[] getPackageSizeIcon() {
        return packageSizeIcon;
    }

    public PackageSize packageSizeIcon(byte[] packageSizeIcon) {
        this.packageSizeIcon = packageSizeIcon;
        return this;
    }

    public void setPackageSizeIcon(byte[] packageSizeIcon) {
        this.packageSizeIcon = packageSizeIcon;
    }

    public String getPackageSizeIconContentType() {
        return packageSizeIconContentType;
    }

    public PackageSize packageSizeIconContentType(String packageSizeIconContentType) {
        this.packageSizeIconContentType = packageSizeIconContentType;
        return this;
    }

    public void setPackageSizeIconContentType(String packageSizeIconContentType) {
        this.packageSizeIconContentType = packageSizeIconContentType;
    }

    public String getPackageSizeIconUrl() {
        return packageSizeIconUrl;
    }

    public PackageSize packageSizeIconUrl(String packageSizeIconUrl) {
        this.packageSizeIconUrl = packageSizeIconUrl;
        return this;
    }

    public void setPackageSizeIconUrl(String packageSizeIconUrl) {
        this.packageSizeIconUrl = packageSizeIconUrl;
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
        PackageSize packageSize = (PackageSize) o;
        if (packageSize.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), packageSize.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PackageSize{" +
            "id=" + getId() +
            ", packageSizeName='" + getPackageSizeName() + "'" +
            ", packageSizeDesciption='" + getPackageSizeDesciption() + "'" +
            ", packageSizeIcon='" + getPackageSizeIcon() + "'" +
            ", packageSizeIconContentType='" + getPackageSizeIconContentType() + "'" +
            ", packageSizeIconUrl='" + getPackageSizeIconUrl() + "'" +
            "}";
    }
}
