package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

/*** File created by Brandon Januska-Wilson ***/
/*** File and all methods created by Brandon Januska-Wilson ***/

public class ReservationUpdateRequest {

    @NotEmpty
    @JsonProperty("reservationId")
    private String reservationId;

    @NotEmpty
    @JsonProperty("tripId")
    private String tripId;

    @JsonProperty("fullName")
    private String fullName;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }
}
