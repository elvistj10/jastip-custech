package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WithdrawalTransferHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawalTransferHistoryRepository extends JpaRepository<WithdrawalTransferHistory, Long> {

}
