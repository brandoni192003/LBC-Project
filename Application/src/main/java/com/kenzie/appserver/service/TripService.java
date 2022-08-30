package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.TripRepository;
import com.kenzie.appserver.repositories.model.TripRecord;
import com.kenzie.appserver.service.model.Reservation;
import com.kenzie.appserver.service.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {
    private TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip addTrip(Trip trip) {
        TripRecord tripRecord = new TripRecord();
        tripRecord.setTripId(trip.getTripId());
        tripRecord.setCost(trip.getCost());
        tripRecord.setStartDate(trip.getStartDate());
        tripRecord.setDuration(trip.getDuration());
        tripRecord.setDestination(trip.getDestination());

        tripRepository.save(tripRecord);
        return trip;
    }

    public void updateTrip(Trip trip) {
        if (tripRepository.existsById(trip.getTripId())) {
            TripRecord tripRecord = new TripRecord();
            tripRecord.setTripId(trip.getTripId());
            tripRecord.setCost(trip.getCost());
            tripRecord.setStartDate(trip.getStartDate());
            tripRecord.setDuration(trip.getDuration());
            tripRecord.setDestination(trip.getDestination());
            tripRepository.save(tripRecord);
        }
    }

    public void deleteTrip(String tripId) {
        tripRepository.deleteById(tripId);
    }

    public Trip getTripById(String tripId) {
        Trip tripFromBackend = tripRepository
                .findById(tripId)
                .map(trip -> new Trip(trip.getTripId(), trip.getCost(), trip.getStartDate(), trip.getDuration(),
                        trip.getDestination())).orElse(null);
        return tripFromBackend;
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        tripRepository
                .findAll()
                .forEach(trip -> trips.add(new Trip(trip.getTripId(), trip.getCost(),
                        trip.getStartDate(), trip.getDuration(), trip.getDestination())));

        return trips;
    }
}
