package com.cus.jastip.payment.repository;

import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentTransferCheckList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransferCheckListRepository extends JpaRepository<PaymentTransferCheckList, Long> {

}
