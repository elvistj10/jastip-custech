package com.cus.jastip.wallet.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.wallet.domain.Wallet;
import com.cus.jastip.wallet.domain.WalletAudit;
import com.cus.jastip.wallet.domain.WalletAuditConfig;
import com.cus.jastip.wallet.domain.WalletTransaction;
import com.cus.jastip.wallet.domain.WalletWithdrawal;
import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WalletAuditConfigRepository;
import com.cus.jastip.wallet.repository.WalletAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WalletAuditService {

	@Autowired
	private WalletAuditRepository walletAuditRepository;

	@Autowired
	private WalletAuditConfigRepository walletAuditConfigRepository;

	Map<String, String> data = new HashMap<>();

	public void addWallet(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Wallet result = (Wallet) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}
	
	public void addWalletTransaction(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			WalletTransaction result = (WalletTransaction) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}
	
	public void addWalletWithdrawal(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			WalletWithdrawal result = (WalletWithdrawal) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}
	
	public void addWithdrawalTransferFailed(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			WithdrawalTransferFailed result = (WithdrawalTransferFailed) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}
	
	public void addWithdrawalTransferHistory(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			WithdrawalTransferHistory result = (WithdrawalTransferHistory) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}
	
	public void addWithdrawalTransferList(Object object, String entityName, UpdateType eventType) {
		WalletAuditConfig config = walletAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			WithdrawalTransferList result = (WithdrawalTransferList) object;
			WalletAudit mAudit = new WalletAudit();
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
			walletAuditRepository.save(mAudit);
		}
	}

	
}
