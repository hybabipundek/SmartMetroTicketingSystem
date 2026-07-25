package model;

import enums.TicketStatus;
import enums.TicketType;

public class Ticket {

    private String ticketId;
    private Passenger passenger;
    private Station source;
    private Station destination;
    private TicketType ticketType;
    private TicketStatus status;
    private double fare;

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

    public String getTicketId() {
        return ticketId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public double getFare() {
        return fare;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

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

    public void cancelTicket() {

        status = TicketStatus.CANCELLED;

    }

}