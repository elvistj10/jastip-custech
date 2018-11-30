package com.cus.jastip.transaction.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Comment;
import com.cus.jastip.transaction.domain.Offering;
import com.cus.jastip.transaction.domain.OfferingCourier;
import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import com.cus.jastip.transaction.domain.OfferingDeliveryService;
import com.cus.jastip.transaction.domain.OfferingPayment;
import com.cus.jastip.transaction.domain.OfferingPuchase;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.SubComment;
import com.cus.jastip.transaction.domain.TransactionAudit;
import com.cus.jastip.transaction.domain.TransactionAuditConfig;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.TransactionAuditConfigRepository;
import com.cus.jastip.transaction.repository.TransactionAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionAuditService {

	@Autowired
	private TransactionAuditRepository transactionAuditRepository;

	@Autowired
	private TransactionAuditConfigRepository transacionAuditConfigRepository;

	Map<String, String> data = new HashMap<>();

	public void addComment(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Comment result = (Comment) object;
			result.setPosting(null);
			result.setPosting(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOffering(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Offering result = (Offering) object;
			result.setPosting(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOfferingCourier(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			OfferingCourier result = (OfferingCourier) object;
			result.setOffering(null);
			result.setCourierReceiptImg(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOfferingDeliveryInfo(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			OfferingDeliveryInfo result = (OfferingDeliveryInfo) object;
			result.setOffering(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOfferingDeliveryService(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			OfferingDeliveryService result = (OfferingDeliveryService) object;
			result.setOffering(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOfferingPayment(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			OfferingPayment result = (OfferingPayment) object;
			result.setOffering(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addOfferingPuchase(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			OfferingPuchase result = (OfferingPuchase) object;
			result.setOffering(null);
			result.setReceiptImg(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addPosting(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Posting result = (Posting) object;
			result.setTrip(null);
			result.setPostingItemImg(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addSubComment(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			SubComment result = (SubComment) object;
			result.setComment(null);
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}

	public void addTrip(Object object, String entityName, UpdateType eventType) {
		TransactionAuditConfig config = transacionAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Trip result = (Trip) object;
			TransactionAudit mAudit = new TransactionAudit();
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
			transactionAuditRepository.save(mAudit);
		}
	}
}
