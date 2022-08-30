package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

/*** File created by Brandon Januska-Wilson ***/
/*** File and all methods created by Brandon Januska-Wilson ***/

public class TripCreateRequest {

    @NotEmpty
    @JsonProperty("cost")
    private double cost;

    @NotEmpty
    @JsonProperty("startDate")
    private String startDate;

    @NotEmpty
    @JsonProperty("duration")
    private int duration;

    @NotEmpty
    @JsonProperty("destination")
    private String destination;

    public double getCost() {
        return cost;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }

    public String getDestination() {
        return destination;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
