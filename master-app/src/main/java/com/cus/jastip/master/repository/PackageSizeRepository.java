package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.PackageSize;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PackageSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageSizeRepository extends JpaRepository<PackageSize, Long> {

}
