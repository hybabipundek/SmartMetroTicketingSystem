package service;
import java.util.ArrayList; 
import fare.StandardFareCalculator;
import fare.FareCalculator;
import enums.TicketStatus;
import enums.TicketType;
import model.Passenger;
import model.Route;
import model.Ticket;

public class TicketService {

    private ArrayList<Ticket> tickets;
    private FareCalculator fareCalculator;

    public TicketService() {
        tickets = new ArrayList<>();  //tickets = [];
        fareCalculator = new StandardFareCalculator();
    }
    
    // Calculate Fare
    public double calculateFare(Route route, TicketType type)
    {
        return fareCalculator.calculateFare(route, type);
    }

    // Buy Ticket
    public Ticket buyTicket(String ticketId,
                            Passenger passenger,
                            Route route,
                            TicketType type) {

        double fare = fareCalculator.calculateFare(route, type);

        Ticket ticket = new Ticket(
                ticketId,
                passenger,
                route.getSource(),
                route.getDestination(),
                type,
                TicketStatus.ACTIVE,
                fare
        );
        //when start , the number of ticket = 0 
        tickets.add(ticket);  //buy ticket, number of ticket = 1 , 2 , 3...

        return ticket;
    }
    
    //generate ticketID
    public String generateTicketId()
    {
    	return "T" + String.format("%03d", tickets.size() + 1);
    }

    // Search Ticket
    public Ticket searchTicket(String ticketId) {

        for (Ticket ticket : tickets) {

            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }

        }

        return null;
    }

    // Cancel Ticket
    public void cancelTicket(String ticketId) {

        Ticket ticket = searchTicket(ticketId);

        if (ticket != null) {

            ticket.cancelTicket();

            System.out.println("Ticket cancelled successfully.");

        } else {

            System.out.println("Ticket not found.");

        }

    }

    // View Tickets
    public void viewTickets(Passenger passenger) {

        boolean found = false;

        for (Ticket ticket : tickets) {

            if (ticket.getPassenger().equals(passenger)) {

                ticket.printTicket();
                found = true;

            }

        }

        if (!found) {

            System.out.println("No tickets found.");

        }

    }

    public ArrayList<Ticket> getAllTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = (tickets == null) ? new ArrayList<>() : tickets;
    }

}
