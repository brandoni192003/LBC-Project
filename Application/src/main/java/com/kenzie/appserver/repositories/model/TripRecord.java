package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

/*** File created by other team member ***/
/*** File and all methods updated by other team member ***/

@DynamoDBTable(tableName = "Trips")
public class TripRecord {
    private String tripId;
    private double cost;
    private String startDate;
    private int duration;
    private String destination;

    @DynamoDBHashKey(attributeName = "TripId")
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @DynamoDBAttribute(attributeName = "Cost")
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @DynamoDBAttribute(attributeName = "StartDate")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute(attributeName = "Duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @DynamoDBAttribute(attributeName = "Destination")
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripRecord that = (TripRecord) o;
        return Objects.equals(tripId, that.tripId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId);
    }
}
