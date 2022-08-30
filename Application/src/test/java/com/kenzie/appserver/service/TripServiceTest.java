package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.TripRepository;
import com.kenzie.appserver.repositories.model.TripRecord;
import com.kenzie.appserver.service.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/*** File created by other team member ***/
/*** File and all tests implemented and updated by other team member ***/

public class TripServiceTest {
    private TripRepository tripRepository;
    private TripService tripService;

    @BeforeEach
    void setup() {
        tripRepository = mock(TripRepository.class);
        tripService = new TripService(tripRepository);
    }

    @Test
    void addTrip_validTrip_savesAndReturnsTrip() {
        // GIVEN
        Trip trip = new Trip(randomUUID().toString(), 1.0, "today", 2, "Hawaii");
        TripRecord tripRecord = createTripRecord(trip);

        // WHEN
        Trip result = tripService.addTrip(trip);

        // THEN
        verify(tripRepository, times(1)).save(tripRecord);
        assertEquals(trip, result, "Trip record should be the same");
    }

    @Test
    void updateTrip_tripExists_tripUpdated() {
        // GIVEN
        Trip trip = new Trip(randomUUID().toString(), 1.0, "today", 2, "Hawaii");
        TripRecord tripRecord = createTripRecord(trip);
        when(tripRepository.existsById(tripRecord.getTripId())).thenReturn(true);

        // WHEN
        tripService.updateTrip(trip);

        // THEN
        verify(tripRepository, times(1)).save(tripRecord);
    }

    @Test
    void updateTrip_tripDoesNotExist_noUpdate() {
        // GIVEN
        Trip trip = new Trip(randomUUID().toString(), 1.0, "today", 2, "Hawaii");
        TripRecord tripRecord = createTripRecord(trip);
        when(tripRepository.existsById(tripRecord.getTripId())).thenReturn(false);

        // WHEN
        tripService.updateTrip(trip);

        // THEN
        verify(tripRepository, never()).save(tripRecord);
    }

    @Test
    void deleteTrip_anyTrip_tripRepositoryDeletes() {
        // GIVEN
        String tripId = randomUUID().toString();

        // WHEN
        tripService.deleteTrip(tripId);

        // THEN
        verify(tripRepository, times(1)).deleteById(tripId);
    }

    @Test
    void getTripById_validId_returnsTrip() {
        // GIVEN
        String tripId = randomUUID().toString();
        Trip trip = new Trip(tripId, 1.0, "today", 2, "Hawaii");
        TripRecord tripRecord = createTripRecord(trip);
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(tripRecord));

        // WHEN
        Trip result = tripService.getTripById(tripId);

        // THEN
        assertEquals(trip, result, "trip does not match");
    }

    @Test
    void getTripById_invalidId_returnsNull() {
        // GIVEN
        String tripId = randomUUID().toString();
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        // WHEN
        Trip result = tripService.getTripById(tripId);

        // THEN
        assertNull(result, "Should have returned null");
    }

    @Test
    void getAllTrips_methodCalled_tripsReturned() {
        // GIVEN
        List<Trip> trips = new ArrayList<>();
        String tripId = randomUUID().toString();
        Trip trip = new Trip(tripId, 1.0, "today", 2, "Hawaii");
        trips.add(trip);

        TripRecord tripRecord = createTripRecord(trip);
        List<TripRecord> tripRecords = new ArrayList<>();
        tripRecords.add(tripRecord);

        when(tripRepository.findAll()).thenReturn(tripRecords);

        // WHEN
        List<Trip> result = tripService.getAllTrips();

        // THEN
        assertEquals(trips, result, "Lists of trips should match");
    }

    // Helper method
    TripRecord createTripRecord(Trip trip) {
        TripRecord tripRecord = new TripRecord();
        tripRecord.setTripId(trip.getTripId());
        tripRecord.setCost(trip.getCost());
        tripRecord.setStartDate(trip.getStartDate());
        tripRecord.setDuration(trip.getDuration());
        tripRecord.setDestination(trip.getDestination());
        return tripRecord;
    }
}
