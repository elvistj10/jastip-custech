package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WithdrawalTransferFailed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawalTransferFailedRepository extends JpaRepository<WithdrawalTransferFailed, Long> {

}
