package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ReservationRecord;
import com.kenzie.appserver.repositories.model.TripRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*** File created by other team member ***/
/*** File updated and implemented by other team member ***/

@EnableScan
public interface TripRepository extends CrudRepository<TripRecord, String> {

}
