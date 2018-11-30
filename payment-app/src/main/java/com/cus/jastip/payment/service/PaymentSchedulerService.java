package com.cus.jastip.payment.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import com.cus.jastip.payment.domain.PaymentTransferHistory;
import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import com.cus.jastip.payment.domain.enumeration.PaymentMethod;
import com.cus.jastip.payment.domain.enumeration.PaymentStatus;
import com.cus.jastip.payment.repository.PaymentRepository;
import com.cus.jastip.payment.repository.PaymentTransferCheckListRepository;
import com.cus.jastip.payment.repository.PaymentTransferHistoryRepository;
import com.cus.jastip.payment.repository.PaymentTransferUnmatchedRepository;
import com.cus.jastip.payment.repository.search.PaymentSearchRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferCheckListSearchRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferHistorySearchRepository;
import com.cus.jastip.payment.web.rest.BcaApi;

@RestController
@RequestMapping("/api/thread")
public class PaymentSchedulerService extends Thread {

	@Autowired
	private PaymentTransferCheckListRepository paymentTransferChecklistRepository;

	@Autowired
	private PaymentTransferCheckListSearchRepository paymentTransferChecklistSearchRepository;

	@Autowired
	private PaymentTransferHistoryRepository paymentTransferHistoryRepository;

	@Autowired
	private PaymentTransferHistorySearchRepository paymentTransferHistorySearchRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentSearchRepository paymentSearchRepository;

	@Autowired
	private PaymentTransferUnmatchedRepository paymentTransferUnmatchedRepository;

	@Autowired
	private PaymentTransferUnmatchedRepository paymentTransferUnmatchedSearchRepository;

	@Autowired
	private BcaApi bcaApi;

	Date datenow = new Date();

	int c = 0;
	int hash = 0;
	int ganjil = 0;

	@SuppressWarnings("unlikely-arg-type")
	@GetMapping("/y")
	public void apiThread() throws ParseException, IOException {

		List<Double> bca = new ArrayList<>();
		bca.add(10.0);
		bca.add(2500.0);
		bca.add(100.0);
		bca.add(12.0);
		bca.add(13.0);
		bca.add(14.0);
		bca.add(21.0);
		bca.add(22.0);
		bca.add(29.0);
		if (bca.size() % 2 == 1) {
			ganjil = 1;
			System.out.println("BCA Size = Ganjil");
		} else {
			System.out.println("BCA Size = Genap");
		}
		List<Integer> removeList = new ArrayList<>();
		List<PaymentTransferCheckList> payTransChecklist = paymentTransferChecklistRepository.findAll();
		System.out.println("Payment Size Before Unmatched= " + payTransChecklist.size());
		for (int k = 0; k < payTransChecklist.size(); k++) {
			int compare = payTransChecklist.get(k).getExpiredDateTime().compareTo(datenow.toInstant());
			int unmatch = 0;
			if (compare < 0) {
				PaymentTransferUnmatched payunmatch = new PaymentTransferUnmatched();
				payunmatch.setNominal(payTransChecklist.get(k).getNominal());
				payunmatch.setOfferingId(payTransChecklist.get(k).getOfferingId());
				payunmatch.setPostingId(payTransChecklist.get(k).getPostingId());
				payunmatch.setCheckDateTime(payTransChecklist.get(k).getPaymentConfirmDateTime());
				payunmatch.setExpiredDateTime(payTransChecklist.get(k).getExpiredDateTime());
				payunmatch.setPaymentUnmatchedDateTime(datenow.toInstant());
				paymentTransferChecklistRepository.delete(payTransChecklist.get(k));
				paymentTransferChecklistSearchRepository.delete(payTransChecklist.get(k));
				PaymentTransferUnmatched elasticUnmatched = paymentTransferUnmatchedRepository.save(payunmatch);
				paymentTransferUnmatchedSearchRepository.save(elasticUnmatched);
				removeList.add(k);
				unmatch++;
			}
			System.out.println("Unmatch Payment =  " + unmatch);
		}
		for (Integer remList : removeList) {
			payTransChecklist.remove(remList);
		}
		System.out.println("Payment Size After Unmatched= " + payTransChecklist.size());
		if (payTransChecklist.size() == 0) {
			System.out.println("Payment Size is Zero, Program End. ");
		} else {
			for (int i = 0; i < bca.size() / 2; i++) {
				Thread thread = null;
				c = i;
				thread = new Thread(new Runnable() {
					int b = c;

					@Override
					public void run() {
						System.out.println("Thread id = " + Thread.currentThread().getId() + " Start.");
						for (int j = 0; j < payTransChecklist.size(); j++) {
							double bcaAmmountGenap = bca.get(b * 2);
							double bcaAmmountGanjil = bca.get((b * 2) + 1);
							double checkListNominal = payTransChecklist.get(j).getNominal();
							if (bcaAmmountGenap == checkListNominal) {
								PaymentTransferCheckList pay = payTransChecklist.get(j);
								saveAllPayment(pay);
								System.out.println("Update Success with id = " + payTransChecklist.get(j).getId());
							}
							if (bcaAmmountGanjil == checkListNominal) {
								PaymentTransferCheckList pay = payTransChecklist.get(j);
								saveAllPayment(pay);
								System.out.println("Update Success with id = " + payTransChecklist.get(j).getId());
							}
							if ((bca.size() / 2) - 1 == b && ganjil == 1) {
								double bcaAmmountAkhir = bca.get((b * 2) + 2);
								if (bcaAmmountAkhir == checkListNominal) {
									PaymentTransferCheckList pay = payTransChecklist.get(j);
									saveAllPayment(pay);
									System.out.println("Update Success with id = " + payTransChecklist.get(j).getId());
								}
							}
						}
						System.out.println("Thread id = " + Thread.currentThread().getId() + " end.");
					}

					public void saveAllPayment(PaymentTransferCheckList pay) {
						PaymentTransferHistory payHistory = new PaymentTransferHistory();
						payHistory.setCheckDateTime(datenow.toInstant());
						payHistory.setExpiredDateTime(pay.getExpiredDateTime());
						payHistory.setNominal(pay.getNominal());
						payHistory.setOfferingId(pay.getOfferingId());
						payHistory.setPostingId(pay.getPostingId());
						payHistory.setPaymentConfirmDateTime(pay.getPaymentConfirmDateTime());
						Payment payment = new Payment();
						payment.setNominal(pay.getNominal());
						payment.setOfferingId(pay.getOfferingId());
						payment.setPostingId(pay.getPostingId());
						payment.setPaymentStatus(PaymentStatus.NEW);
						payment.setPaymentDateTime(datenow.toInstant());
						PaymentTransferHistory elasticHistory = paymentTransferHistoryRepository.save(payHistory);
						Payment elasticPayment = paymentRepository.save(payment);
						paymentTransferChecklistRepository.delete(pay);
						paymentTransferHistorySearchRepository.save(elasticHistory);
						paymentSearchRepository.save(elasticPayment);
						paymentTransferChecklistSearchRepository.delete(pay);
					}
				});
				thread.start();
			}

		}

	}

}
