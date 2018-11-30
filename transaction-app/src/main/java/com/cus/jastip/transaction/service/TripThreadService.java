package com.cus.jastip.transaction.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.TripStatus;
import com.cus.jastip.transaction.repository.TripRepository;

@Service
public class TripThreadService {

	@Autowired
	private TripRepository tripRepository;

	Instant dateNow = Instant.now();

	public void tripExpired() {
		List<Trip> trip = tripRepository.getPostingOngoingOrUpcoming();
		if (trip.size() != 0) {
			for (Trip tr : trip) {
				ZoneId zone = ZoneId.of(tr.getTimezone());
				ZonedDateTime zoneNow = ZonedDateTime.ofInstant(dateNow, zone);
				ZonedDateTime zoneExpired = ZonedDateTime.ofInstant(tr.getEndDate(), zone);
				int compare = zoneExpired.compareTo(zoneNow);
				if (compare < 0) {
					tr.setStatus(TripStatus.EXPIRED);
					tripRepository.save(tr);
				}

			}
		}
	}

}
