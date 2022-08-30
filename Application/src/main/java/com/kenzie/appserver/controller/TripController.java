package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.TripCreateRequest;
import com.kenzie.appserver.controller.model.TripResponse;
import com.kenzie.appserver.controller.model.TripUpdateRequest;
import com.kenzie.appserver.service.TripService;
import com.kenzie.appserver.service.model.Trip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*** File created by other team member ***/
/*** File updated/modified by all team members ***/

@RestController
@RequestMapping("/trips")
public class TripController {

    private TripService tripService;

    TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // Method created by Brandon Januska-Wilson
    @PostMapping
    public ResponseEntity<TripResponse> addTrip(@RequestBody TripCreateRequest tripCreateRequest) {
        Trip trip = new Trip(UUID.randomUUID().toString(),
                tripCreateRequest.getCost(),
                tripCreateRequest.getStartDate(),
                tripCreateRequest.getDuration(),
                tripCreateRequest.getDestination());

        tripService.addTrip(trip);
        TripResponse tripResponse = createTripResponse(trip);

        return ResponseEntity.created(URI.create("/trips/" + tripResponse.getTripId())).body(tripResponse);
    }

    // Method created by Brandon Januska-Wilson
    @PutMapping
    public ResponseEntity<TripResponse> updateTrip(@RequestBody TripUpdateRequest tripUpdateRequest) {
        Trip trip = new Trip(tripUpdateRequest.getTripId(),
                tripUpdateRequest.getCost(),
                tripUpdateRequest.getStartDate(),
                tripUpdateRequest.getDuration(),
                tripUpdateRequest.getDestination());

        tripService.updateTrip(trip);
        TripResponse tripResponse = createTripResponse(trip);

        return ResponseEntity.ok(tripResponse);
    }

    // Method created by Brandon Januska-Wilson
    @DeleteMapping("/{tripId}")
    public ResponseEntity deleteTripById(@PathVariable("tripId") String tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    // Method created by other team member
    // Method updated by Brandon Januska-Wilson (bug fix)
    @GetMapping("/all")
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();

        if (trips == null || trips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TripResponse> responses = trips.stream().map(trip ->
                createTripResponse(trip)).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Method created by other team member
    private TripResponse createTripResponse(Trip trip) {
        TripResponse tripResponse = new TripResponse();
        tripResponse.setTripId(trip.getTripId());
        tripResponse.setCost(trip.getCost());
        tripResponse.setDestination(trip.getDestination());
        tripResponse.setDuration(trip.getDuration());
        tripResponse.setStartDate(trip.getStartDate());

        return tripResponse;
    }
}
