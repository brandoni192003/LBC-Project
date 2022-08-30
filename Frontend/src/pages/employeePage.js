import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import TripClient from "../api/tripClient";

// File created by Brandon Januska-Wilson
// File updated and all methods implemented by Brandon Januska-Wilson

/**
 * Logic needed for the employee page of the website.
 */
class EmployeePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onUpdate', 'onDelete', 'onGetReservations', 'renderReservations'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers
     */
     async mount() {
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-form').addEventListener('submit', this.onUpdate);
        document.getElementById('delete-form').addEventListener('submit', this.onDelete);
        document.getElementById('get-by-trip-id-form').addEventListener('submit', this.onGetReservations);
        this.client = new TripClient();

        this.dataStore.addChangeListener(this.renderReservations)
        this.dataStore.set("reservations", null);
     }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderReservations() {
        let resultArea = document.getElementById('reservations-info');

        const reservations = this.dataStore.get("reservations");

        let todoHTML = "";
        todoHTML += "<ul>"

        for (let reservation of reservations) {
            todoHTML += `
            <li>
            <h3>Reservation ID: ${reservation.reservationId}</h3>
            <h3>Trip ID: ${reservation.tripId}</h3>
            <h3>FullName: ${reservation.fullName}</h3>
            </li>
            `
        }
        todoHTML += "</ul>"

        if (reservations) {
            resultArea.innerHTML = todoHTML;
        } else {
            resultArea.innerHTML = "No Reservations Available";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let cost = document.getElementById('create-cost-field').value;
        let startDate = document.getElementById('create-start-date-field').value;
        let duration = document.getElementById('create-duration-field').value;
        let destination = document.getElementById('create-destination-field').value;

        const createdTrip = await this.client.addTrip(cost, startDate, duration, destination, this.errorHandler);

        if (createdTrip) {
            this.showMessage("Created Trip Successfully!")
        } else {
            this.errorHandler("Error creating a new trip! Try again...");
        }
    }

    async onUpdate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let tripId = document.getElementById('update-trip-id-field').value;
        let cost = document.getElementById('update-cost-field').value;
        let startDate = document.getElementById('update-start-date-field').value;
        let duration = document.getElementById('update-duration-field').value;
        let destination = document.getElementById('update-destination-field').value;

        const updatedTrip = await this.client.modifyTrip(tripId, cost, startDate, duration, destination, this.errorHandler);

        if (updatedTrip) {
            this.showMessage("Updated Trip Successfully!")
        } else {
            this.errorHandler("Error updating trip! Try again...");
        }
    }

    async onDelete(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let tripId = document.getElementById('delete-trip-id-field').value;

        const response = await this.client.deleteTrip(tripId, this.errorHandler);

        if (response == '') {
            this.showMessage("Deleted Trip Successfully!")
        } else {
            this.errorHandler("Error deleting trip! Try again...");
        }
    }

    async onGetReservations(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let tripId = document.getElementById('get-trip-id-field').value;

        const reservations = await this.client.getReservationsByTrip(tripId, this.errorHandler);

        this.dataStore.set("reservations", reservations);

        if (reservations) {
            this.showMessage("Retrieved all reservations!")
        } else {
            this.errorHandler("No reservations for trip ID found!");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const employeePage = new EmployeePage();
    employeePage.mount();
};

window.addEventListener('DOMContentLoaded', main);