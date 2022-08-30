package com.kenzie.appserver.service.model;

import java.util.Objects;

/*** File created by Brandon Januska-Wilson ***/
/*** File implemented and updated by other team member ***/

public class Reservation {
    private final String reservationId;
    private final String tripId;
    private final String fullName;


    public Reservation(String reservationId, String tripId, String fullName) {
        this.reservationId = reservationId;
        this.tripId = tripId;
        this.fullName = fullName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId) && Objects.equals(tripId, that.tripId)
                && Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, tripId, fullName);
    }
}
