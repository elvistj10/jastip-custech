package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.Wallet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Wallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Wallet findByOwnerId(Long id);
	

}
