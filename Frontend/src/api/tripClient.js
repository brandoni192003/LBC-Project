import BaseClass from "../util/baseClass";
import axios from 'axios'

// File created by Brandon Januska-Wilson
// File updated and all methods implemented by Brandon Januska-Wilson

export default class TripClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'addTrip', 'modifyTrip', 'deleteTrip', 'getReservationsByTrip'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
     clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
     }

     async addTrip(cost, startDate, duration, destination, errorCallback) {
        try {
            const response = await this.client.post(`/trips`, {
                "cost" : cost,
                "startDate" : startDate,
                "duration" : duration,
                "destination" : destination
            });
            return response.data;
        } catch (error) {
            this.handleError("addTrip", error, errorCallback);
        }
     }

     async modifyTrip(tripId, cost, startDate, duration, destination, errorCallback) {
        try {
            const response = await this.client.put(`/trips`, {
                "tripId" : tripId,
                "cost" : cost,
                "startDate" : startDate,
                "duration" : duration,
                "destination" : destination
            });
            return response.data;
        } catch (error) {
            this.handleError("modifyTrip", error, errorCallback);
        }
     }

     async deleteTrip(tripId, errorCallback) {
        try {
            const response = await this.client.delete(`/trips/${tripId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteTrip", error, errorCallback);
        }
     }

     async getReservationsByTrip(tripId, errorCallback) {
        try {
            const response = await this.client.get(`/reservations/all/${tripId}`);
            return response.data;
        } catch (error) {
            this.handleError("getReservationsByTrip", error, errorCallback);
        }
     }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
     handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
     }
}