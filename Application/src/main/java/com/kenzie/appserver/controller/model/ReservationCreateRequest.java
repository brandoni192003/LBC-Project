package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

/*** File created by Kenzie Academy ***/
/*** File and all methods updated by other team member ***/

public class ReservationCreateRequest {

    @NotEmpty
    @JsonProperty("tripId")
    private String tripId;

    @JsonProperty("fullName")
    private String fullName;

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
