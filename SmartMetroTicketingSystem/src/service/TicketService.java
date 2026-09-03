package service;

import java.util.ArrayList;

import exception.TicketNotFoundException;
import fare.FareCalculator;
import fare.StandardFareCalculator;
import enums.TicketStatus;
import enums.TicketType;
import model.Passenger;
import model.Route;
import model.Ticket;

public class TicketService {

    private ArrayList<Ticket> tickets;
    private FareCalculator fareCalculator;

    public TicketService() {
        tickets = new ArrayList<>();
        fareCalculator = new StandardFareCalculator();
    }

    public double calculateFare(Route route, TicketType type) {
        if (route == null || type == null) return 0.0;
        return fareCalculator.calculateFare(route, type);
    }

    public Ticket buyTicket(String ticketId, Passenger passenger, Route route, TicketType type) {
        if (passenger == null || route == null || type == null) {
            throw new IllegalArgumentException("Ticket information is incomplete.");
        }

        double fare = fareCalculator.calculateFare(route, type);
        Ticket ticket = new Ticket(ticketId, passenger, route.getSource(), route.getDestination(),
                type, TicketStatus.ACTIVE, fare);
        tickets.add(ticket);
        return ticket;
    }

    public String generateTicketId() {
        int next = 1;
        for (Ticket ticket : tickets) {
            String id = ticket.getTicketId();
            if (id != null && id.matches("TK\\d+")) {
                try {
                    next = Math.max(next, Integer.parseInt(id.substring(2)) + 1);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return "TK" + String.format("%03d", next);
    }

    public Ticket searchTicket(String ticketId) {
        if (ticketId == null) return null;
        for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)) return ticket;
        }
        return null;
    }

    public void cancelTicket(String ticketId, Passenger passenger) {
        Ticket ticket = searchTicket(ticketId);

        if (ticket == null || passenger == null || ticket.getPassenger() != passenger) {
            throw new TicketNotFoundException("Ticket not found or does not belong to the current passenger.");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new TicketNotFoundException("Ticket has already been cancelled.");
        }

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new TicketNotFoundException("Used tickets cannot be cancelled.");
        }

        ticket.cancelTicket();
        System.out.println("Ticket cancelled successfully.");
    }

    public void viewTickets(Passenger passenger) {
        boolean found = false;
        for (Ticket ticket : tickets) {
            if (ticket.getPassenger() == passenger) {
                ticket.printTicket();
                found = true;
            }
        }
        if (!found) System.out.println("No tickets found.");
    }

    public boolean hasTickets(Passenger passenger) {
        for (Ticket ticket : tickets) {
            if (ticket.getPassenger() == passenger) return true;
        }
        return false;
    }

    public ArrayList<Ticket> getAllTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = (tickets == null) ? new ArrayList<>() : tickets;
    }
}
