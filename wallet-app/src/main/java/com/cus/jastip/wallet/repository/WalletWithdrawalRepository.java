package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WalletWithdrawal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WalletWithdrawal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletWithdrawalRepository extends JpaRepository<WalletWithdrawal, Long> {

}
