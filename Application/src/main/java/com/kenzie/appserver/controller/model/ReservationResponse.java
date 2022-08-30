package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*** File created by Kenzie Academy ***/
/*** File and all methods updated by other team member ***/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    @JsonProperty("reservationId")
    private String reservationId;

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

    public String getName() {
        return fullName;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
