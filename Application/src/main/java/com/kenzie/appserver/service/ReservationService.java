package com.kenzie.appserver.service;

import com.kenzie.appserver.exceptions.TripNotFoundException;
import com.kenzie.appserver.repositories.TripRepository;
import com.kenzie.appserver.repositories.model.ReservationRecord;
import com.kenzie.appserver.repositories.ReservationRepository;
import com.kenzie.appserver.service.model.Reservation;

import com.kenzie.appserver.service.model.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*** File created by Brandon Januska-Wilson ***/
/*** File implemented and updated by other team member ***/

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TripService tripService;

    public ReservationService(ReservationRepository reservationRepository, TripService tripService) {
        this.reservationRepository = reservationRepository;
        this.tripService = tripService;
    }

    public Reservation findByReservationId(String reservationId) {

        Reservation reservationFromBackend = reservationRepository
                .findById(reservationId)
                .map(reservation -> new Reservation(reservation.getReservationId(),
                        reservation.getTripId(),
                        reservation.getName()))
                .orElse(null);
        return reservationFromBackend;
    }

    public Reservation bookReservation(Reservation reservation) {
        Trip trip = tripService.getTripById(reservation.getTripId());
        if (trip == null) {
            throw new TripNotFoundException(HttpStatus.BAD_REQUEST, reservation.getTripId());
        }

        ReservationRecord reservationRecord = new ReservationRecord();
        reservationRecord.setReservationId(reservation.getReservationId());
        reservationRecord.setTripId(reservation.getTripId());
        reservationRecord.setName(reservation.getName());
        reservationRepository.save(reservationRecord);
        return reservation;
    }

    public List<Reservation> findAllReservationsByTripId(String tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new TripNotFoundException(HttpStatus.BAD_REQUEST, tripId);
        }
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository
                .findByTripId(tripId)
                .forEach(reservation -> reservations.add(new Reservation(reservation.getReservationId(), reservation.getTripId(),
                        reservation.getName())));

        return reservations;

    }

    public void modifyReservation(Reservation reservation) {
        if (reservationRepository.existsById(reservation.getReservationId())) {
            ReservationRecord reservationRecord = new ReservationRecord();
            reservationRecord.setReservationId(reservation.getReservationId());
            reservationRecord.setName(reservation.getName());
            reservationRecord.setTripId(reservation.getTripId());
            reservationRepository.save(reservationRecord);
        }
    }

    public void cancelReservation(String reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
