package com.cus.jastip.master.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ItemSubCategory.
 */
@Entity
@Table(name = "item_sub_category")
@Document(indexName = "itemsubcategory")
public class ItemSubCategory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_sub_category_name", nullable = false)
    private String itemSubCategoryName;

    @Lob
    @Column(name = "item_sub_category_icon")
    private byte[] itemSubCategoryIcon;

    @Column(name = "item_sub_category_icon_content_type")
    private String itemSubCategoryIconContentType;

    @Column(name = "item_sub_category_icon_url")
    private String itemSubCategoryIconUrl;

    @ManyToOne
    private ItemCategory itemCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemSubCategoryName() {
        return itemSubCategoryName;
    }

    public ItemSubCategory itemSubCategoryName(String itemSubCategoryName) {
        this.itemSubCategoryName = itemSubCategoryName;
        return this;
    }

    public void setItemSubCategoryName(String itemSubCategoryName) {
        this.itemSubCategoryName = itemSubCategoryName;
    }

    public byte[] getItemSubCategoryIcon() {
        return itemSubCategoryIcon;
    }

    public ItemSubCategory itemSubCategoryIcon(byte[] itemSubCategoryIcon) {
        this.itemSubCategoryIcon = itemSubCategoryIcon;
        return this;
    }

    public void setItemSubCategoryIcon(byte[] itemSubCategoryIcon) {
        this.itemSubCategoryIcon = itemSubCategoryIcon;
    }

    public String getItemSubCategoryIconContentType() {
        return itemSubCategoryIconContentType;
    }

    public ItemSubCategory itemSubCategoryIconContentType(String itemSubCategoryIconContentType) {
        this.itemSubCategoryIconContentType = itemSubCategoryIconContentType;
        return this;
    }

    public void setItemSubCategoryIconContentType(String itemSubCategoryIconContentType) {
        this.itemSubCategoryIconContentType = itemSubCategoryIconContentType;
    }

    public String getItemSubCategoryIconUrl() {
        return itemSubCategoryIconUrl;
    }

    public ItemSubCategory itemSubCategoryIconUrl(String itemSubCategoryIconUrl) {
        this.itemSubCategoryIconUrl = itemSubCategoryIconUrl;
        return this;
    }

    public void setItemSubCategoryIconUrl(String itemSubCategoryIconUrl) {
        this.itemSubCategoryIconUrl = itemSubCategoryIconUrl;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public ItemSubCategory itemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
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
        ItemSubCategory itemSubCategory = (ItemSubCategory) o;
        if (itemSubCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemSubCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemSubCategory{" +
            "id=" + getId() +
            ", itemSubCategoryName='" + getItemSubCategoryName() + "'" +
            ", itemSubCategoryIcon='" + getItemSubCategoryIcon() + "'" +
            ", itemSubCategoryIconContentType='" + getItemSubCategoryIconContentType() + "'" +
            ", itemSubCategoryIconUrl='" + getItemSubCategoryIconUrl() + "'" +
            "}";
    }
}
