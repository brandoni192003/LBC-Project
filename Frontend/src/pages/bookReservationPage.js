import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ReservationClient from "../api/reservationClient";

// File created by Kenzie Academy
// File updated and all methods implemented by Brandon Januska-Wilson

/**
 * Logic needed for the book reservation page of the website.
 */
class BookReservationPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onGetTrips', 'onCreate', 'onUpdate', 'onDelete', 'renderReservation', 'renderTrips'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the current reservation.
     */
    async mount() {
        document.getElementById('get-by-id-form').addEventListener('submit', this.onGet);
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        document.getElementById('update-form').addEventListener('submit', this.onUpdate);
        document.getElementById('delete-form').addEventListener('submit', this.onDelete);
        this.client = new ReservationClient();


        this.dataStore.addChangeListener(this.renderReservation)
        this.dataStore.addChangeListener(this.renderTrips)
        this.onGetTrips();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderReservation() {
        let resultArea = document.getElementById('reservation-info');

        const reservation = this.dataStore.get("reservation");

        if (reservation) {
            resultArea.innerHTML = `
                <div>Reservation ID: ${reservation.reservationId}</div>
                <div>Trip ID: ${reservation.tripId}</div>
                <div>FullName: ${reservation.fullName}</div>
            `
        } else {
            resultArea.innerHTML = "No Reservation Details Available";
        }
    }

    async renderTrips() {
        let resultArea = document.getElementById('trip-info');

        const trips = this.dataStore.get("trips");

        let todoHTML = "";
        todoHTML += "<ul>"

        for (let trip of trips) {
            todoHTML += `
            <li>
            <h2>${trip.destination}</h2>
            <h3>Trip ID: ${trip.tripId}</h3>
            <h3>$${trip.cost}</h3>
            <h3>${trip.startDate}</h3>
            <h3>${trip.duration} weeks</h3>
            </li>
            `
        }
        todoHTML += "</ul>"

        if (trips) {
            resultArea.innerHTML = todoHTML;
        } else {
            resultArea.innerHTML = "No Trips Available";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById('id-field').value;
        this.dataStore.set("reservation", null);

        let result = await this.client.getReservation(id, this.errorHandler);
        this.dataStore.set("reservation", result);
        if (result) {
            this.showMessage(`Got Reservation for ${result.fullName}!`)
        } else {
            this.errorHandler("Error getting reservation!  Try again...");
        }
    }

    async onGetTrips() {
        let result = await this.client.getAllTrips(this.errorHandler);
        this.dataStore.set("trips", result);
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("reservation", null);

        let tripId = document.getElementById('create-trip-id-field').value;
        let fullName = document.getElementById('create-fullName-field').value;

        const createdReservation = await this.client.createReservation(tripId, fullName, this.errorHandler);
        this.dataStore.set("reservation", createdReservation);

        if (createdReservation) {
            this.showMessage(`Booked Reservation for ${createdReservation.fullName}!`)
        } else {
            this.errorHandler("Error booking reservation! Try again...");
        }
    }

    async onUpdate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("reservation", null);

        let reservationId = document.getElementById('update-id-field').value;
        let tripId = document.getElementById('update-trip-id-field').value;
        let fullName = document.getElementById('update-fullName-field').value;

        const updatedReservation = await this.client.modifyReservation(reservationId, tripId, fullName, this.errorHandler);
        this.dataStore.set("reservation", updatedReservation);

        if (updatedReservation) {
            this.showMessage("Updated Successfully!")
        } else {
            this.errorHandler("Error updating your reservation! Try again...");
        }
    }

    async onDelete(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("reservation", null);

        let reservationId = document.getElementById('delete-id-field').value;

        const response = await this.client.deleteReservation(reservationId, this.errorHandler);

        if (response == '') {
            this.showMessage("Canceled Successfully!")
        } else {
            this.errorHandler("Error canceling your reservation! Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const bookReservationPage = new BookReservationPage();
    bookReservationPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
