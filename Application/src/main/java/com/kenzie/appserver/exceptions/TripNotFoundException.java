package com.kenzie.appserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/*** File created by other team member ***/
/*** File updated/modified by other team member ***/

public class TripNotFoundException extends ResponseStatusException {

    public TripNotFoundException(HttpStatus status, String tripId) {
        super(status, "Trip does not exist" + tripId);
    }
}
