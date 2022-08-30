package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

/*** File created by Kenzie Academy ***/
/*** File and all methods updated by other team member ***/

@DynamoDBTable(tableName = "Reservations")
public class ReservationRecord {

    private String reservationId;
    private String tripId;
    private String fullName;

    @DynamoDBHashKey(attributeName = "ReservationId")
    public String getReservationId() {
        return reservationId;
    }

    @DynamoDBAttribute(attributeName = "FullName")
    public String getName() {
        return fullName;
    }

    @DynamoDBAttribute(attributeName = "TripId")
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public void setName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationRecord reservationRecord = (ReservationRecord) o;
        return Objects.equals(reservationId, reservationRecord.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
