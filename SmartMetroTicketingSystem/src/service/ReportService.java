package service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.TicketStatus;
import enums.TicketType;
import model.Ticket;

/**
 * Produces summary reports from live ticket data.
 * Holds a reference to TicketService (not a copy of the list) so that
 * every report reflects the current state of the system.
 */
public class ReportService {

    private final TicketService ticketService;

    public ReportService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /** Full system report: runs every section in order. */
    public void generateReport() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("           SMART METRO SYSTEM REPORT");
        System.out.println("==================================================");

        if (ticketService.getAllTickets().isEmpty()) {
            System.out.println("No ticket data available to report.");
            System.out.println("==================================================");
            return;
        }

        showTotalSales();
        showTotalRevenue();
        showRevenueByType();
        showBusiestStations();
        showCancelledTickets();

        System.out.println("==================================================");
        System.out.println("               END OF REPORT");
        System.out.println("==================================================");
    }

    /** Ticket counts broken down by status. */
    public void showTotalSales() {
        Map<TicketStatus, Integer> counts = new EnumMap<>(TicketStatus.class);
        for (TicketStatus status : TicketStatus.values()) {
            counts.put(status, 0);
        }
        for (Ticket ticket : ticketService.getAllTickets()) {
            counts.put(ticket.getStatus(), counts.get(ticket.getStatus()) + 1);
        }

        System.out.println("\n--- TICKET SALES ---");
        System.out.printf("%-22s : %d%n", "Total tickets sold", ticketService.getAllTickets().size());
        for (TicketStatus status : TicketStatus.values()) {
            System.out.printf("%-22s : %d%n", "  " + status, counts.get(status));
        }
    }

    /** Revenue earned. Cancelled tickets are refunded and earn nothing. */
    public void showTotalRevenue() {
        double revenue = 0.0;
        double refunded = 0.0;
        int counted = 0;

        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() == TicketStatus.CANCELLED) {
                refunded += ticket.getFare();
            } else {
                revenue += ticket.getFare();
                counted++;
            }
        }

        double average = (counted == 0) ? 0.0 : revenue / counted;

        System.out.println("\n--- REVENUE ---");
        System.out.printf("%-22s : RM %.2f%n", "Total revenue", revenue);
        System.out.printf("%-22s : RM %.2f%n", "Refunded (cancelled)", refunded);
        System.out.printf("%-22s : RM %.2f%n", "Average per ticket", average);
    }

    /** Revenue split by ticket type. */
    public void showRevenueByType() {
        Map<TicketType, Double> byType = new EnumMap<>(TicketType.class);
        for (TicketType type : TicketType.values()) {
            byType.put(type, 0.0);
        }
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() != TicketStatus.CANCELLED) {
                byType.put(ticket.getTicketType(), byType.get(ticket.getTicketType()) + ticket.getFare());
            }
        }

        System.out.println("\n--- REVENUE BY TICKET TYPE ---");
        System.out.printf("%-12s %12s%n", "TYPE", "REVENUE");
        for (TicketType type : TicketType.values()) {
            System.out.printf("%-12s %9s%.2f%n", type, "RM ", byType.get(type));
        }
    }

    /** Busiest departure stations, ranked using a Comparator. */
    public void showBusiestStations() {
        Map<String, Integer> boardings = new HashMap<>();
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() == TicketStatus.CANCELLED) continue;
            String name = ticket.getSource().getName();
            boardings.put(name, boardings.getOrDefault(name, 0) + 1);
        }

        List<Map.Entry<String, Integer>> ranked = new ArrayList<>(boardings.entrySet());
        ranked.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("\n--- BUSIEST DEPARTURE STATIONS ---");
        System.out.printf("%-5s %-22s %10s%n", "RANK", "STATION", "BOARDINGS");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : ranked) {
            if (rank > 5) break;
            System.out.printf("%-5d %-22s %10d%n", rank++, entry.getKey(), entry.getValue());
        }
    }

    /** Lists every cancelled ticket. */
    public void showCancelledTickets() {
        System.out.println("\n--- CANCELLED TICKETS ---");
        System.out.printf("%-10s %-16s %-22s %10s%n", "TICKET", "PASSENGER", "JOURNEY", "REFUND");

        boolean found = false;
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() != TicketStatus.CANCELLED) continue;
            found = true;
            String journey = ticket.getSource().getName() + " -> " + ticket.getDestination().getName();
            System.out.printf("%-10s %-16s %-22s %7s%.2f%n",
                    ticket.getTicketId(), ticket.getPassenger().getName(), journey, "RM ", ticket.getFare());
        }

        if (!found) {
            System.out.println("No cancelled tickets.");
        }
    }
}