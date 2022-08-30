package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.TripService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.service.model.Trip;
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

/*** File created by other team member ***/
/*** File and all tests implemented by other team member ***/

@IntegrationTest
public class TripControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    TripService tripService;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String tripId = UUID.randomUUID().toString();
    private final double cost = 2000d;
    private final String startDate = "12-01-2022";
    private final int duration = 10;
    private final String destination = "Aruba";

    @Test
    public void addTrip_CreateSuccessful() throws Exception {
        TripCreateRequest tripCreateRequest = new TripCreateRequest();
        tripCreateRequest.setCost(cost);
        tripCreateRequest.setStartDate(startDate);
        tripCreateRequest.setDuration(duration);
        tripCreateRequest.setDestination(destination);

        //WHEN
        mvc.perform(post("/trips")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tripCreateRequest)))
        //THEN
                .andExpect(jsonPath("tripId")
                        .exists())
                .andExpect(jsonPath("cost")
                        .value(is(cost)))
                .andExpect(jsonPath("startDate")
                        .value(is(startDate)))
                .andExpect(jsonPath("duration")
                        .value(is(duration)))
                .andExpect(jsonPath("destination")
                        .value(is(destination)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateTrip_UpdateSuccessful() throws Exception {
        //GIVEN
        Trip trip = new Trip(tripId, cost, startDate, duration, destination);
        Trip persistedTrip = tripService.addTrip(trip);

        double updatedCost = 15000d;
        int updatedDuration = 14;
        String updatedDestination = "Seychelles";

        TripUpdateRequest tripUpdateRequest = new TripUpdateRequest();
        tripUpdateRequest.setTripId(persistedTrip.getTripId());
        tripUpdateRequest.setCost(updatedCost);
        tripUpdateRequest.setStartDate(persistedTrip.getStartDate());
        tripUpdateRequest.setDuration(updatedDuration);
        tripUpdateRequest.setDestination(updatedDestination);

        //WHEN
        mvc.perform(put("/trips")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tripUpdateRequest)))
        //THEN
                .andExpect(jsonPath("tripId")
                        .value(persistedTrip.getTripId()))
                .andExpect(jsonPath("cost")
                        .value(is(updatedCost)))
                .andExpect(jsonPath("startDate")
                        .value(is(persistedTrip.getStartDate())))
                .andExpect(jsonPath("duration")
                        .value(is(updatedDuration)))
                .andExpect(jsonPath("destination")
                        .value(is(updatedDestination)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTripById_DeleteSuccess() throws Exception {
        //GIVEN
        Trip trip = new Trip(tripId, cost, startDate, duration, destination);
        Trip persistedTrip = tripService.addTrip(trip);

        //WHEN
        mvc.perform(delete("/trips/{tripId}", persistedTrip.getTripId())
                        .accept(MediaType.APPLICATION_JSON))
        //THEN
                .andExpect(status().isNoContent());

        assertThat(tripService.getTripById(trip.getTripId())).isNull();
    }

    @Test
    public void getAllTrips_ReturnSuccess() throws Exception {
        Trip trip1 = new Trip(tripId, cost, startDate, duration, destination);
        Trip trip2 = new Trip(UUID.randomUUID().toString(), 4000d, "02-12-2023", 12,
                "Cape Town");

        Trip persistedTrip1 = tripService.addTrip(trip1);
        Trip persistedTrip2 = tripService.addTrip(trip2);


        //WHEN
        ResultActions actions = mvc.perform(get("/trips/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<TripResponse> listTrips = mapper.readValue(responseBody, new TypeReference<List<TripResponse>>(){});
        assertThat(listTrips.size()).isEqualTo(2);
    }
}
