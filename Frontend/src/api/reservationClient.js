import BaseClass from "../util/baseClass";
import axios from 'axios'

// File created by Kenzie Academy
// File updated and all methods implemented by Brandon Januska-Wilson

export default class ReservationClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getReservation', 'modifyReservation', 'createReservation', 'deleteReservation', 'getAllTrips'];
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


    async getReservation(reservationId, errorCallback) {
        try {
            const response = await this.client.get(`/reservations/${reservationId}`);
            return response.data;
        } catch (error) {
            this.handleError("getReservation", error, errorCallback)
        }
    }

    async createReservation(tripId, fullName, errorCallback) {
        try {
            const response = await this.client.post(`/reservations`, {
                "tripId" : tripId,
                "fullName" : fullName
            });
            return response.data;
        } catch (error) {
            this.handleError("createReservation", error, errorCallback);
        }
    }

    async modifyReservation(reservationId, tripId, fullName, errorCallback) {
        try {
            const response = await this.client.put(`/reservations`, {
                "reservationId" : reservationId,
                "tripId" : tripId,
                "fullName" : fullName
            });
            return response.data;
        } catch (error) {
            this.handleError("modifyReservation", error, errorCallback);
        }
    }

    async getAllTrips(errorCallback) {
        try {
            const response = await this.client.get(`/trips/all`);
            return response.data;
        } catch (error) {
            this.handleError("getAllTrips", error, errorCallback);
        }
    }

    async deleteReservation(reservationId, errorCallback) {
        try {
            const response = await this.client.delete(`/reservations/${reservationId}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteReservation", error, errorCallback);
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
