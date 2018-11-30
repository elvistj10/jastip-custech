package com.cus.jastip.wallet.repository;

import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WithdrawalTransferList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawalTransferListRepository extends JpaRepository<WithdrawalTransferList, Long> {

}
