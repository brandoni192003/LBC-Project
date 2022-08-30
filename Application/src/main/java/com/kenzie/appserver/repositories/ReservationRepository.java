package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ReservationRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*** File created by Brandon Januska-Wilson ***/
/*** File updated and implemented by other team members ***/

@EnableScan
public interface ReservationRepository extends CrudRepository<ReservationRecord, String> {
    List<ReservationRecord> findByTripId(String tripId);
}
