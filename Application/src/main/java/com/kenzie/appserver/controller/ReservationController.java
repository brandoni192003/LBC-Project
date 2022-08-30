package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ReservationCreateRequest;
import com.kenzie.appserver.controller.model.ReservationResponse;
import com.kenzie.appserver.controller.model.ReservationUpdateRequest;
import com.kenzie.appserver.service.ReservationService;

import com.kenzie.appserver.service.model.Reservation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

/*** File created by Brandon Januska-Wilson ***/
/*** File updated/modified by all team members ***/

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Method created by other team member
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable("reservationId") String reservationId) {

        Reservation reservation = reservationService.findByReservationId(reservationId);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }

        ReservationResponse reservationResponse = createReservationResponse(reservation);
        return ResponseEntity.ok(reservationResponse);
    }

    // Method created by other team member
    @GetMapping("/all/{tripId}")
    public ResponseEntity<List<ReservationResponse>> getAllReservations(@PathVariable("tripId") String tripId) {
        List<Reservation> reservations = reservationService.findAllReservationsByTripId(tripId);

        if (reservations == null || reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ReservationResponse> reservationResponses = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationResponses.add(createReservationResponse(reservation));
        }

        return ResponseEntity.ok(reservationResponses);
    }

    // Method created by other team member
    // Method updated by Brandon Januska-Wilson (bug fix)
    @PostMapping
    public ResponseEntity<ReservationResponse> bookReservation(@RequestBody ReservationCreateRequest reservationCreateRequest) {
        Reservation reservation = new Reservation(randomUUID().toString(),
                reservationCreateRequest.getTripId(), reservationCreateRequest.getName());
        reservationService.bookReservation(reservation);

        ReservationResponse reservationResponse = createReservationResponse(reservation);

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.getReservationId())).body(reservationResponse);
    }

    // Method created by other team member
    @PutMapping
    public ResponseEntity<ReservationResponse> modifyReservation(@RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        Reservation reservation = new Reservation(reservationUpdateRequest.getReservationId(),
                reservationUpdateRequest.getTripId(),
                reservationUpdateRequest.getName());
        reservationService.modifyReservation(reservation);

        ReservationResponse reservationResponse = createReservationResponse(reservation);

        return ResponseEntity.ok(reservationResponse);
    }

    // Method created by other team member
    @DeleteMapping("/{reservationId}")
    public ResponseEntity cancelReservationById(@PathVariable("reservationId") String reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    // Method created by other team member
    private ReservationResponse createReservationResponse(Reservation reservation) {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setReservationId(reservation.getReservationId());
        reservationResponse.setTripId(reservation.getTripId());
        reservationResponse.setName(reservation.getName());
        return reservationResponse;
    }
}
