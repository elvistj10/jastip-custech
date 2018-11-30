package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.ItemSubCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ItemSubCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemSubCategoryRepository extends JpaRepository<ItemSubCategory, Long> {

}
