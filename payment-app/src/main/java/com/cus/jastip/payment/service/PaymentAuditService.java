package com.cus.jastip.payment.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.PaymentAudit;
import com.cus.jastip.payment.domain.PaymentAuditConfig;
import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import com.cus.jastip.payment.domain.PaymentTransferHistory;
import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import com.cus.jastip.payment.domain.enumeration.UpdateType;
import com.cus.jastip.payment.repository.PaymentAuditConfigRepository;
import com.cus.jastip.payment.repository.PaymentAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentAuditService {

	@Autowired
	private PaymentAuditRepository paymentAuditRepository;

	@Autowired
	private PaymentAuditConfigRepository paymentAuditConfigRepository;

	Map<String, String> data = new HashMap<>();

	public void addPayment(Object object, String entityName, UpdateType eventType) {
		PaymentAuditConfig config = paymentAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Payment result = (Payment) object;
			PaymentAudit mAudit = new PaymentAudit();
			mAudit.setEntityId(result.getId());
			mAudit.setEntityName(entityName);
			mAudit.setAuditEventType(eventType.toString());
			mAudit.setPrincipal(result.getLastModifiedBy());
			mAudit.setAuditEventDate(result.getLastModifiedDate());
			ObjectMapper oMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = oMapper.convertValue(result, Map.class);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					data.put(entry.getKey(), "");
				} else {
					data.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mAudit.setData(data);
			paymentAuditRepository.save(mAudit);
		}
	}

	public void addPaymentTransferCheckList(Object object, String entityName, UpdateType eventType) {
		PaymentAuditConfig config = paymentAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			PaymentTransferCheckList result = (PaymentTransferCheckList) object;
			PaymentAudit mAudit = new PaymentAudit();
			mAudit.setEntityId(result.getId());
			mAudit.setEntityName(entityName);
			mAudit.setAuditEventType(eventType.toString());
			mAudit.setPrincipal(result.getLastModifiedBy());
			mAudit.setAuditEventDate(result.getLastModifiedDate());
			ObjectMapper oMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = oMapper.convertValue(result, Map.class);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					data.put(entry.getKey(), "");
				} else {
					data.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mAudit.setData(data);
			paymentAuditRepository.save(mAudit);
		}
	}

	public void addPaymentTransferHistory(Object object, String entityName, UpdateType eventType) {
		PaymentAuditConfig config = paymentAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			PaymentTransferHistory result = (PaymentTransferHistory) object;
			PaymentAudit mAudit = new PaymentAudit();
			mAudit.setEntityId(result.getId());
			mAudit.setEntityName(entityName);
			mAudit.setAuditEventType(eventType.toString());
			mAudit.setPrincipal(result.getLastModifiedBy());
			mAudit.setAuditEventDate(result.getLastModifiedDate());
			ObjectMapper oMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = oMapper.convertValue(result, Map.class);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					data.put(entry.getKey(), "");
				} else {
					data.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mAudit.setData(data);
			paymentAuditRepository.save(mAudit);
		}
	}

	public void addPaymentTransferUnmatched(Object object, String entityName, UpdateType eventType) {
		PaymentAuditConfig config = paymentAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			PaymentTransferUnmatched result = (PaymentTransferUnmatched) object;
			PaymentAudit mAudit = new PaymentAudit();
			mAudit.setEntityId(result.getId());
			mAudit.setEntityName(entityName);
			mAudit.setAuditEventType(eventType.toString());
			mAudit.setPrincipal(result.getLastModifiedBy());
			mAudit.setAuditEventDate(result.getLastModifiedDate());
			ObjectMapper oMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = oMapper.convertValue(result, Map.class);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					data.put(entry.getKey(), "");
				} else {
					data.put(entry.getKey(), entry.getValue().toString());
				}
			}
			mAudit.setData(data);
			paymentAuditRepository.save(mAudit);
		}
	}
}
