package com.cus.jastip.transaction.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.PostingStatus;
import com.cus.jastip.transaction.repository.PostingRepository;

@Service
public class PostingThreadService {

	@Autowired
	private PostingRepository postingRepository;

	Instant dateNow = Instant.now();

	public void postingExpired() {
		List<Posting> posting = postingRepository.getPostingByStatusOpen();
		if (posting.size() != 0) {
			for (Posting post : posting) {
				ZoneId zone = ZoneId.of(post.getTimezone());
				ZonedDateTime zoneNow = ZonedDateTime.ofInstant(dateNow, zone);
				ZonedDateTime zoneExpired = ZonedDateTime.ofInstant(post.getExpiredDate(), zone);
				int compare = zoneExpired.compareTo(zoneNow);
				if (compare < 0) {
					post.setStatus(PostingStatus.EXPIRED);
					postingRepository.save(post);
				}

			}
		}
	}

}
