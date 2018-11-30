package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WalletAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WalletAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletAuditRepository extends JpaRepository<WalletAudit, Long> {

}
