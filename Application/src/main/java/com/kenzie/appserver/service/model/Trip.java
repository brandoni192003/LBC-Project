package com.kenzie.appserver.service.model;

import java.util.Objects;

/*** File created by Brandon Januska-Wilson ***/
/*** File implemented and updated by other team member ***/

public class Trip {
    private final String tripId;
    private final double cost;
    private final String startDate;
    private final int duration;
    private final String destination;

    public Trip(String tripId, double cost, String startDate, int duration, String destination) {
        this.tripId = tripId;
        this.cost = cost;
        this.startDate = startDate;
        this.duration = duration;
        this.destination = destination;
    }

    public String getTripId() {
        return tripId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trip trip = (Trip) o;
        return Double.compare(trip.cost, cost) == 0 && duration == trip.duration
                && Objects.equals(tripId, trip.tripId) && Objects.equals(startDate, trip.startDate)
                && Objects.equals(destination, trip.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, cost, startDate, duration, destination);
    }
}
