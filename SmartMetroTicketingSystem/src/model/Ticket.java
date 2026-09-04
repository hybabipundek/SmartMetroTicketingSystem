package model;

import enums.TicketStatus;
import enums.TicketType;

/**
 * Model class representing a booked metro ticket, including passenger, route, type, status, and fare.
 */
public class Ticket {

    private String ticketId;
    private Passenger passenger;
    private Station source;
    private Station destination;
    private TicketType ticketType;
    private TicketStatus status;
    private double fare;

// Initializes the Ticket object.
    public Ticket() {

    }

    public Ticket(String ticketId, Passenger passenger, Station source,
                  Station destination, TicketType ticketType,
                  TicketStatus status, double fare) {

        this.ticketId = ticketId;
        this.passenger = passenger;
        this.source = source;
        this.destination = destination;
        this.ticketType = ticketType;
        this.status = status;
        this.fare = fare;
    }

// Returns the ticketid value.
    public String getTicketId() {
        return ticketId;
    }

// Returns the passenger value.
    public Passenger getPassenger() {
        return passenger;
    }

// Returns the source value.
    public Station getSource() {
        return source;
    }

// Returns the destination value.
    public Station getDestination() {
        return destination;
    }

// Returns the tickettype value.
    public TicketType getTicketType() {
        return ticketType;
    }

// Returns the status value.
    public TicketStatus getStatus() {
        return status;
    }

// Returns the fare value.
    public double getFare() {
        return fare;
    }

// Updates the ticketid value.
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

// Updates the passenger value.
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

// Updates the source value.
    public void setSource(Station source) {
        this.source = source;
    }

// Updates the destination value.
    public void setDestination(Station destination) {
        this.destination = destination;
    }

// Updates the tickettype value.
    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

// Updates the status value.
    public void setStatus(TicketStatus status) {
        this.status = status;
    }

// Updates the fare value.
    public void setFare(double fare) {
        this.fare = fare;
    }

// Displays the ticket details in the console.
    public void printTicket() {

        System.out.println("========== Ticket ==========");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Passenger Name: " + passenger.getName());
        System.out.println("Source Station: " + source.getName());
        System.out.println("Destination Station: " + destination.getName());
        System.out.println("Ticket Type: " + ticketType);
        System.out.printf("Fare: RM %.2f%n", fare);
        System.out.println("Ticket Status: " + status);
        System.out.println("============================");

    }

// Changes the ticket status to cancelled.
    public void cancelTicket() {

        status = TicketStatus.CANCELLED;

    }

}