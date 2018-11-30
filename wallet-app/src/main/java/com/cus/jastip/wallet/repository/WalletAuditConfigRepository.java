package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WalletAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WalletAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletAuditConfigRepository extends JpaRepository<WalletAuditConfig, Long> {
	
	WalletAuditConfig findByEntityName(String entityName);

}
