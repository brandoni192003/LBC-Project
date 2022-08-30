package com.kenzie.appserver.service;

import com.kenzie.appserver.exceptions.TripNotFoundException;
import com.kenzie.appserver.repositories.ReservationRepository;
import com.kenzie.appserver.repositories.model.ReservationRecord;
import com.kenzie.appserver.service.model.Reservation;
import com.kenzie.appserver.service.model.Trip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/*** File created by Brandon Januska-Wilson ***/
/*** File and all tests implemented and updated by other team member ***/

public class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private TripService tripService;

    @BeforeEach
    void setup() {
        reservationRepository = mock(ReservationRepository.class);
        tripService = mock(TripService.class);
        reservationService = new ReservationService(reservationRepository, tripService);
    }
    
    @Test
    void findByReservationId_validId_returnsReservation() {
        // GIVEN
        String reservationId = randomUUID().toString();
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setReservationId(reservationId);
        reservationRecord.setTripId(randomUUID().toString());
        reservationRecord.setName("TestReservation");


        // WHEN
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationRecord));
        Reservation reservation = reservationService.findByReservationId(reservationId);

        // THEN
        Assertions.assertNotNull(reservation, "The object is returned");
        assertEquals(reservationRecord.getReservationId(), reservation.getReservationId(),
                "The reservation id matches");
        assertEquals(reservationRecord.getName(), reservation.getName(), "The name matches");
        assertEquals(reservationRecord.getTripId(), reservation.getTripId(), "The trip ID matches");
    }

    @Test
    void findByReservationId_invalidId_returnsNull() {
        // GIVEN
        String id = randomUUID().toString();

        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Reservation reservation = reservationService.findByReservationId(id);

        // THEN
        Assertions.assertNull(reservation, "The example is null when not found");
    }

    @Test
    void bookReservation_validTrip_savesAndReturnsReservation() {
        // GIVEN
        String reservationId = randomUUID().toString();
        String tripId = randomUUID().toString();
        ArgumentCaptor<ReservationRecord> reservationRecordCaptor = ArgumentCaptor.forClass(ReservationRecord.class);

        when(reservationRepository.save(any(ReservationRecord.class))).then(i -> i.getArgumentAt(0, ReservationRecord.class));

        Trip trip = new Trip(tripId,1000.00d, "12-15-2022", 10, "Singapore");
        Reservation reservation = new Reservation(reservationId, tripId, "Stephen Blalock");

        when(tripService.getTripById(tripId)).thenReturn(trip);


        // WHEN
        Reservation returnedReservation = reservationService.bookReservation(reservation);

        // THEN
        verify(reservationRepository).save(reservationRecordCaptor.capture());
        ReservationRecord reservationRecord = reservationRecordCaptor.getValue();

        Assertions.assertNotNull(reservationRecord, "The object exists");
        assertEquals(reservationRecord.getReservationId(), reservation.getReservationId(),
                "Reservation Id matches");
        assertEquals(reservationRecord.getTripId(), reservation.getTripId(), "Trip Id matches");
        assertEquals(reservationRecord.getName(), reservation.getName(), "Name matches");

        Assertions.assertNotNull(returnedReservation, "The object exists");
        assertEquals(returnedReservation.getReservationId(), reservation.getReservationId(),
                "Reservation Id matches");
        assertEquals(returnedReservation.getTripId(), reservation.getTripId(), "Trip Id matches");
        assertEquals(returnedReservation.getName(), reservation.getName(), "Name matches");
    }

    @Test
    void bookReservation_invalidTrip_throwsTripNotFoundException() {
        // GIVEN
        Trip trip = null;
        String tripId = randomUUID().toString();
        String reservationId = randomUUID().toString();
        when(tripService.getTripById(tripId)).thenReturn(trip);
        Reservation reservation = new Reservation(reservationId, tripId, "Stephen Blalock");


        // WHEN/THEN
        assertThrows(TripNotFoundException.class, () -> reservationService.bookReservation(reservation));

    }

    @Test
    void findAllReservationsByTripId_validTrip_returnsReservations() {
        // GIVEN
        String tripId = randomUUID().toString();
        Trip trip = new Trip(tripId, 1.0, "today", 2, "Hawaii");
        when(tripService.getTripById(tripId)).thenReturn(trip);

        Reservation reservation = new Reservation(randomUUID().toString(), tripId, "Jane Doe");
        ReservationRecord reservationRecord = createReservationRecord(reservation);

        List<ReservationRecord> reservationRecords = new ArrayList<>();
        reservationRecords.add(reservationRecord);
        when(reservationRepository.findByTripId(tripId)).thenReturn(reservationRecords);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        // WHEN
        List<Reservation> result = reservationService.findAllReservationsByTripId(tripId);

        // THEN
        assertEquals(reservations, result, "Lists should match");
    }

    @Test
    void findAllReservationsByTripId_invalidTrip_throwsTripNotFoundException() {
        // GIVEN
        String tripId = randomUUID().toString();
        when(tripService.getTripById(tripId)).thenReturn(null);

        // WHEN
        // THEN
        assertThrows(TripNotFoundException.class, () -> reservationService.findAllReservationsByTripId(tripId));
    }

    @Test
    void modifyReservation_reservationExists_repositorySaves() {
        // GIVEN
        String reservationId = randomUUID().toString();
        String tripId = randomUUID().toString();
        Reservation reservation = new Reservation(reservationId, tripId, "Jane Doe");
        ReservationRecord reservationRecord = createReservationRecord(reservation);
        when(reservationRepository.existsById(reservation.getReservationId())).thenReturn(true);

        // WHEN
        reservationService.modifyReservation(reservation);

        // THEN
        verify(reservationRepository, times(1)).save(reservationRecord);
    }

    @Test
    void modifyReservation_reservationDoesNotExists_repositoryDoesNotSave() {
        // GIVEN
        String reservationId = randomUUID().toString();
        String tripId = randomUUID().toString();
        Reservation reservation = new Reservation(reservationId, tripId, "Jane Doe");
        ReservationRecord reservationRecord = createReservationRecord(reservation);
        when(reservationRepository.existsById(reservation.getReservationId())).thenReturn(false);

        // WHEN
        reservationService.modifyReservation(reservation);

        // THEN
        verify(reservationRepository, never()).save(reservationRecord);
    }

    @Test
    void cancelReservation_anyId_deletes() {
        // GIVEN
        String reservationId = randomUUID().toString();

        // WHEN
        reservationService.cancelReservation(reservationId);

        // THEN
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    ReservationRecord createReservationRecord(Reservation reservation) {
        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setReservationId(reservation.getReservationId());
        reservationRecord.setName(reservation.getName());
        reservationRecord.setTripId(reservation.getTripId());
        return reservationRecord;
    }
}
