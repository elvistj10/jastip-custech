package com.cus.jastip.profile.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.BankAccount;
import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.BlockedByList;
import com.cus.jastip.profile.domain.CreditCard;
import com.cus.jastip.profile.domain.Feedback;
import com.cus.jastip.profile.domain.FeedbackResponse;
import com.cus.jastip.profile.domain.FollowerList;
import com.cus.jastip.profile.domain.FollowingList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.ProfilesAudit;
import com.cus.jastip.profile.domain.ProfilesAuditConfig;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.ProfilesAuditConfigRepository;
import com.cus.jastip.profile.repository.ProfilesAuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProfilesAuditService {

	@Autowired
	private ProfilesAuditRepository profilesAuditRepository;

	@Autowired
	private ProfilesAuditConfigRepository profilesAuditConfigRepository;
	
	Map<String, String> data = new HashMap<>();

	public void addAddress(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Address result = (Address) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addBankAccount(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			BankAccount result = (BankAccount) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addBlockedByList(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			BlockedByList result = (BlockedByList) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addBlockList(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			BlockList result = (BlockList) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addCreditCard(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			CreditCard result = (CreditCard) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addFeedback(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Feedback result = (Feedback) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addFeedbackResponse(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			FeedbackResponse result = (FeedbackResponse) object;
			result.setFeedback(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addFollowerList(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			FollowerList result = (FollowerList) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addFollowingList(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			FollowingList result = (FollowingList) object;
			result.setProfile(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}
	
	public void addProfiles(Object object, String entityName, UpdateType eventType) {
		ProfilesAuditConfig config = profilesAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			Profile result = (Profile) object;
			result.setImage(null);
			ProfilesAudit mAudit = new ProfilesAudit();
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
			profilesAuditRepository.save(mAudit);
		}
	}

}
