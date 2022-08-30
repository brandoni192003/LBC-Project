package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*** File created by other team member ***/
/*** File and all methods created by other team member ***/

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripResponse {
    @JsonProperty("tripId")
    private String tripId;

    @JsonProperty("cost")
    private double cost;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("destination")
    private String destination;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
