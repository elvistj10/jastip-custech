package com.cus.jastip.profile.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.repository.AddressRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.AddressSearchRepository;

@Service
public class DefaultAddressService {

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private AddressSearchRepository addressSearchRepository;

	public void addressChange(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);
		Pageable pageable = null;
		List<Address> list = addressRepository.findByProfile(profile, pageable);
		for (Address address : list) {
			address.setDefaultAddress(false);
			Address result = addressRepository.save(address);
			addressSearchRepository.save(result);
		}
	}
}
