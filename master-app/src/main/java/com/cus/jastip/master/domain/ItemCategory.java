package com.cus.jastip.master.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ItemCategory.
 */
@Entity
@Table(name = "item_category")
@Document(indexName = "itemcategory")
public class ItemCategory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_category_name", nullable = false)
    private String itemCategoryName;

    @Lob
    @Column(name = "item_category_icon")
    private byte[] itemCategoryIcon;

    @Column(name = "item_category_icon_content_type")
    private String itemCategoryIconContentType;

    @Column(name = "item_category_icon_url")
    private String itemCategoryIconUrl;

    @OneToMany(mappedBy = "itemCategory")
    @JsonIgnore
    private Set<ItemSubCategory> itemSubCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public ItemCategory itemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
        return this;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public byte[] getItemCategoryIcon() {
        return itemCategoryIcon;
    }

    public ItemCategory itemCategoryIcon(byte[] itemCategoryIcon) {
        this.itemCategoryIcon = itemCategoryIcon;
        return this;
    }

    public void setItemCategoryIcon(byte[] itemCategoryIcon) {
        this.itemCategoryIcon = itemCategoryIcon;
    }

    public String getItemCategoryIconContentType() {
        return itemCategoryIconContentType;
    }

    public ItemCategory itemCategoryIconContentType(String itemCategoryIconContentType) {
        this.itemCategoryIconContentType = itemCategoryIconContentType;
        return this;
    }

    public void setItemCategoryIconContentType(String itemCategoryIconContentType) {
        this.itemCategoryIconContentType = itemCategoryIconContentType;
    }

    public String getItemCategoryIconUrl() {
        return itemCategoryIconUrl;
    }

    public ItemCategory itemCategoryIconUrl(String itemCategoryIconUrl) {
        this.itemCategoryIconUrl = itemCategoryIconUrl;
        return this;
    }

    public void setItemCategoryIconUrl(String itemCategoryIconUrl) {
        this.itemCategoryIconUrl = itemCategoryIconUrl;
    }

    public Set<ItemSubCategory> getItemSubCategories() {
        return itemSubCategories;
    }

    public ItemCategory itemSubCategories(Set<ItemSubCategory> itemSubCategories) {
        this.itemSubCategories = itemSubCategories;
        return this;
    }

    public ItemCategory addItemSubCategory(ItemSubCategory itemSubCategory) {
        this.itemSubCategories.add(itemSubCategory);
        itemSubCategory.setItemCategory(this);
        return this;
    }

    public ItemCategory removeItemSubCategory(ItemSubCategory itemSubCategory) {
        this.itemSubCategories.remove(itemSubCategory);
        itemSubCategory.setItemCategory(null);
        return this;
    }

    public void setItemSubCategories(Set<ItemSubCategory> itemSubCategories) {
        this.itemSubCategories = itemSubCategories;
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
        ItemCategory itemCategory = (ItemCategory) o;
        if (itemCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemCategory{" +
            "id=" + getId() +
            ", itemCategoryName='" + getItemCategoryName() + "'" +
            ", itemCategoryIcon='" + getItemCategoryIcon() + "'" +
            ", itemCategoryIconContentType='" + getItemCategoryIconContentType() + "'" +
            ", itemCategoryIconUrl='" + getItemCategoryIconUrl() + "'" +
            "}";
    }
}
