package com.cus.jastip.master.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.master.domain.Bank;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.City;
import com.cus.jastip.master.domain.Country;
import com.cus.jastip.master.domain.ItemCategory;
import com.cus.jastip.master.domain.ItemSubCategory;
import com.cus.jastip.master.domain.MasterAudit;
import com.cus.jastip.master.domain.MasterAuditConfig;
import com.cus.jastip.master.domain.PackageSize;
import com.cus.jastip.master.domain.PostalCode;
import com.cus.jastip.master.domain.Province;
import com.cus.jastip.master.domain.ServiceFee;
import com.cus.jastip.master.domain.Updates;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.MasterAuditConfigRepository;
import com.cus.jastip.master.repository.MasterAuditRepository;
import com.cus.jastip.master.repository.UpdatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MasterAuditService {

	@Autowired
	private MasterAuditRepository masterAuditRepository;

	@Autowired
	private MasterAuditConfigRepository masterAuditConfigRepository;

	@Autowired
	private UpdatesRepository updatesRepository;

	Map<String, String> data = new HashMap<>();

	public void addServiceFee(Object object, String entityName, UpdateType eventType) {
		ServiceFee result = (ServiceFee) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
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
			masterAuditRepository.save(mAudit);
		}

	}

	public void addProvince(Object object, String entityName, UpdateType eventType) {
		Province result = (Province) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setCountry(null);
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
			masterAuditRepository.save(mAudit);
		}
	}

	public void addPostalCode(Object object, String entityName, UpdateType eventType) {
		PostalCode result = (PostalCode) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setCity(null);
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
			masterAuditRepository.save(mAudit);
		}
	}

	public void addPackageSize(Object object, String entityName, UpdateType eventType) {
		PackageSize result = (PackageSize) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
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
			masterAuditRepository.save(mAudit);
		}

	}

	public void addItemSubCategory(Object object, String entityName, UpdateType eventType) {
		ItemSubCategory result = (ItemSubCategory) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setItemCategory(null);
			updatesRepository.save(updates);
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
			masterAuditRepository.save(mAudit);
		}

	}

	public void addItemCategory(Object object, String entityName, UpdateType eventType) {
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		ItemCategory result = (ItemCategory) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setItemSubCategories(null);
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
			masterAuditRepository.save(mAudit);
		}
	}

	public void addCountry(Object object, String entityName, UpdateType eventType) {
		Country result = (Country) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setImage(null);
			result.setImageFlag(null);
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
			masterAuditRepository.save(mAudit);
		}

	}

	public void addCity(Object object, String entityName, UpdateType eventType) {
		City result = (City) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
			result.setProvince(null);
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
			masterAuditRepository.save(mAudit);
		}

	}

	public void addBusinessAccount(Object object, String entityName, UpdateType eventType) {
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		BusinessAccount result = (BusinessAccount) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
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
			masterAuditRepository.save(mAudit);
		}
	}

	public void addBank(Object object, String entityName, UpdateType eventType) {
		Bank result = (Bank) object;
		Updates updates = new Updates();
		updates.setEntityName(entityName);
		updates.setEntityId(result.getId());
		updates.setUpdateDateTime(result.getLastModifiedDate());
		updates.setUpdateType(eventType);
		updatesRepository.save(updates);
		MasterAuditConfig config = masterAuditConfigRepository.findByEntityName(entityName);
		if (config != null && config.isActiveStatus() != null && config.isActiveStatus() == true) {
			MasterAudit mAudit = new MasterAudit();
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
			masterAuditRepository.save(mAudit);

		}
	}

}
