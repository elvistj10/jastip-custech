package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.Comment;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.TripStatus;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.TripRepository;
import com.cus.jastip.transaction.repository.search.TripSearchRepository;
import com.cus.jastip.transaction.service.KafkaService;
import com.cus.jastip.transaction.service.TransactionAuditService;
import com.cus.jastip.transaction.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.transaction.web.rest.util.HeaderUtil;
import com.cus.jastip.transaction.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Trip.
 */
@RestController
@RequestMapping("/api")
public class TripResource {
	
	@Autowired
	private KafkaService kafkaService;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(TripResource.class);

	private static final String ENTITY_NAME = "trip";

	private final TripRepository tripRepository;

	private final TripSearchRepository tripSearchRepository;

	public TripResource(TripRepository tripRepository, TripSearchRepository tripSearchRepository) {
		this.tripRepository = tripRepository;
		this.tripSearchRepository = tripSearchRepository;
	}

	@GetMapping("/trips/owner/{id}")
	@Timed
	public ResponseEntity<List<Trip>> getTripsByOwner(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Trips");
		List<Trip> list = tripRepository.findByOwnerId(id, pageable);
		Page<Trip> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trips/owner/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/trips/status/{status}")
	@Timed
	public ResponseEntity<List<Trip>> getTripsByTripStatus(@PathVariable TripStatus status, Pageable pageable) {
		log.debug("REST request to get all Trips");
		List<Trip> list = tripRepository.findByStatus(status, pageable);
		Page<Trip> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trips/status/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/trips/owner/{ownerId}/status/{status}")
	@Timed
	public ResponseEntity<List<Trip>> getTripsByOwnerTripStatus(@PathVariable Long ownerId,
			@PathVariable TripStatus status, Pageable pageable) {
		log.debug("REST request to get all Trips");
		List<Trip> list = tripRepository.findByOwnerIdInAndStatusIn(ownerId, status, pageable);
		Page<Trip> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/owner/" + ownerId + "/status/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/trips/origin-country/{originId}/destination-country/{destId}")
	@Timed
	public ResponseEntity<List<Trip>> getTripsByOriginDestinationCountry(@PathVariable Long originId,
			@PathVariable Long destId, Pageable pageable) {
		log.debug("REST request to get all Trips");
		List<Trip> list = tripRepository.findByOriginCountryIdInAndDestCountryIdIn(originId, destId, pageable);
		Page<Trip> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/origin-country/" + originId + "/destination-country/" + destId);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /trips : Create a new trip.
	 *
	 * @param trip
	 *            the trip to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         trip, or with status 400 (Bad Request) if the trip has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/trips")
	@Timed
	public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) throws URISyntaxException {
		log.debug("REST request to save Trip : {}", trip);
		if (trip.getId() != null) {
			throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Trip result = tripRepository.save(trip);
		tripSearchRepository.save(result);
		transactionAuditService.addTrip(result, ENTITY_NAME, UpdateType.CREATE);
		kafkaService.tripCountProcess(result);
		return ResponseEntity.created(new URI("/api/trips/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /trips : Updates an existing trip.
	 *
	 * @param trip
	 *            the trip to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         trip, or with status 400 (Bad Request) if the trip is not valid, or
	 *         with status 500 (Internal Server Error) if the trip couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/trips")
	@Timed
	public ResponseEntity<Trip> updateTrip(@Valid @RequestBody Trip trip) throws URISyntaxException {
		log.debug("REST request to update Trip : {}", trip);
		if (trip.getId() == null) {
			return createTrip(trip);
		}
		Trip result = tripRepository.save(trip);
		tripSearchRepository.save(result);
		transactionAuditService.addTrip(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trip.getId().toString()))
				.body(result);
	}

	/**
	 * GET /trips : get all the trips.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of trips in body
	 */
	@GetMapping("/trips")
	@Timed
	public ResponseEntity<List<Trip>> getAllTrips(Pageable pageable) {
		log.debug("REST request to get all Trips");
		Page<Trip> page = tripRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trips");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /trips/:id : get the "id" trip.
	 *
	 * @param id
	 *            the id of the trip to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the trip, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/trips/{id}")
	@Timed
	public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
		log.debug("REST request to get Trip : {}", id);
		Trip trip = tripRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trip));
	}

	/**
	 * DELETE /trips/:id : delete the "id" trip.
	 *
	 * @param id
	 *            the id of the trip to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/trips/{id}")
	@Timed
	public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
		log.debug("REST request to delete Trip : {}", id);
		Trip result = tripRepository.findOne(id);
		tripRepository.delete(id);
		tripSearchRepository.delete(id);
		transactionAuditService.addTrip(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/trips?query=:query : search for the trip corresponding to the
	 * query.
	 *
	 * @param query
	 *            the query of the trip search
	 * @return the result of the search
	 */
	@GetMapping("/_search/trips")
	@Timed
	public ResponseEntity<List<Trip>> searchTrips(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Trips for query {}", query);
		List<Trip> list = StreamSupport
				.stream(tripSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Trip> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/trips");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}
