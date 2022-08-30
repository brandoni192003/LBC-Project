package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ReservationCreateRequest;
import com.kenzie.appserver.controller.model.ReservationResponse;
import com.kenzie.appserver.controller.model.ReservationUpdateRequest;
import com.kenzie.appserver.service.ReservationService;
import com.kenzie.appserver.service.TripService;
import com.kenzie.appserver.service.model.Reservation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.service.model.Trip;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*** File created by Kenzie Academy ***/
/*** File and all tests implemented by other team member ***/

@IntegrationTest
class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ReservationService reservationService;

    @Autowired
    TripService tripService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    private final Trip trip = new Trip(UUID.randomUUID().toString(), 100d, "12-01-2022", 10,
            "Aruba");

    @Test
    public void getReservationByID_ReservationExists() throws Exception {
        //GIVEN
        Trip persistedTrip = tripService.addTrip(trip);

        String reservationId = UUID.randomUUID().toString();
        String fullName = mockNeat.strings().valStr();
        String tripId = persistedTrip.getTripId();

        Reservation reservation = new Reservation(reservationId, tripId, fullName);
        Reservation persistedReservation = reservationService.bookReservation(reservation);

        //WHEN
        mvc.perform(get("/reservations/{reservationId}", persistedReservation.getReservationId())
                        .accept(MediaType.APPLICATION_JSON))
        //THEN
                .andExpect(jsonPath("reservationId")
                        .value(is(reservationId)))
                .andExpect(jsonPath("tripId")
                        .value(is(tripId)))
                .andExpect(jsonPath("fullName")
                        .value(is(fullName)))
                .andExpect(status().isOk());
    }

    @Test
    public void getReservationByID_ReservationDoesNotExist() throws Exception {
        //GIVEN
        String reservationId = UUID.randomUUID().toString();

        //WHEN
        mvc.perform(get("/reservations/{reservationId}", reservationId)
                .accept(MediaType.APPLICATION_JSON))
        //THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllReservationsByTripId_ReservationsExist() throws Exception {
        Trip persistedTrip = tripService.addTrip(trip);
        String reservationId1 = UUID.randomUUID().toString();
        String fullName1 = mockNeat.strings().valStr();
        String tripId1 = persistedTrip.getTripId();

        String reservationId2 = UUID.randomUUID().toString();
        String fullName2 = mockNeat.strings().valStr();
        String tripId2 = persistedTrip.getTripId();

        Reservation reservation1 = new Reservation(reservationId1, tripId1, fullName1);
        Reservation persistedReservation1 = reservationService.bookReservation(reservation1);

        Reservation reservation2 = new Reservation(reservationId2, tripId2, fullName2);
        Reservation persistedReservation2 = reservationService.bookReservation(reservation2);

        //WHEN
        ResultActions actions = mvc.perform(get("/reservations/all/{tripId}", persistedTrip.getTripId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<ReservationResponse> listReservations = mapper.readValue(responseBody, new TypeReference<List<ReservationResponse>>(){});
        assertThat(listReservations.size()).isEqualTo(2);

    }


    @Test
    public void bookReservation_PostSuccessful() throws Exception {
        //GIVEN
        Trip persistedTrip = tripService.addTrip(trip);
        String tripId = persistedTrip.getTripId();
        String fullName = mockNeat.strings().valStr();

        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest();
        reservationCreateRequest.setTripId(tripId);
        reservationCreateRequest.setName(fullName);

        //WHEN
        mvc.perform(post("/reservations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reservationCreateRequest)))
        //THEN
                .andExpect(jsonPath("reservationId")
                        .exists())
                .andExpect(jsonPath("tripId")
                        .value(is(tripId)))
                .andExpect(jsonPath("fullName")
                        .value(is(fullName)))
                .andExpect(status().isCreated());

    }

    @Test
    public void modifyReservation_PutSuccessful() throws Exception {
        //GIVEN
        Trip persistedTrip = tripService.addTrip(trip);

        String reservationId = UUID.randomUUID().toString();
        String fullName = mockNeat.strings().valStr();
        String tripId = persistedTrip.getTripId();

        Reservation reservation = new Reservation(reservationId, tripId, fullName);
        Reservation persistedReservation = reservationService.bookReservation(reservation);

        String updatedFullName = mockNeat.strings().valStr();
        String updatedTripId = UUID.randomUUID().toString();

        ReservationUpdateRequest reservationUpdateRequest = new ReservationUpdateRequest();
        reservationUpdateRequest.setReservationId(reservationId);
        reservationUpdateRequest.setTripId(updatedTripId);
        reservationUpdateRequest.setName(updatedFullName);

        //WHEN
        mvc.perform(put("/reservations")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationUpdateRequest)))
        //THEN
                .andExpect(jsonPath("reservationId")
                        .exists())
                .andExpect(jsonPath("tripId")
                        .value(is(updatedTripId)))
                .andExpect(jsonPath("fullName")
                        .value(is(updatedFullName)))
                .andExpect(status().isOk());

    }

    @Test
    public void cancelReservationById_CancelSuccessful() throws Exception {
        //GIVEN
        Trip persistedTrip = tripService.addTrip(trip);

        String reservationId = UUID.randomUUID().toString();
        String fullName = mockNeat.strings().valStr();
        String tripId = persistedTrip.getTripId();

        Reservation reservation = new Reservation(reservationId, tripId, fullName);
        Reservation persistedReservation = reservationService.bookReservation(reservation);

        //WHEN
        mvc.perform(delete("/reservations/{reservationId}", persistedReservation.getReservationId())
                        .accept(MediaType.APPLICATION_JSON))
        //THEN
                .andExpect(status().isNoContent());

        assertThat(reservationService.findByReservationId(reservationId)).isNull();
    }
}