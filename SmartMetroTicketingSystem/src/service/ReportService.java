package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.TicketStatus;
import enums.TicketType;
import exception.FileProcessingException;
import model.Ticket;

/**
 * Produces summary reports from live ticket data.
 *
 * Holds a reference to TicketService (not a copy of the list) so that every
 * report reflects the current state of the system.
 *
 * Each section is built as a List of text lines first, then either printed to
 * the console or written to a file. Separating the content from where it goes
 * means the exported file and the on-screen report can never drift apart.
 */
public class ReportService {

    /** Folder that generated reports are written into. */
    private static final Path REPORT_DIRECTORY = Paths.get("reports");

    /** Timestamp shown inside the report body. */
    private static final DateTimeFormatter STAMP =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Timestamp used to build a unique file name. */
    private static final DateTimeFormatter FILE_STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static final String DIVIDER = "==================================================";

    private final TicketService ticketService;

    public ReportService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ------------------------------------------------------------------
    // Console output
    // ------------------------------------------------------------------

    /** Full system report, printed to the console. */
    public void generateReport() {
        print(buildReport());
    }

    /** Ticket counts broken down by status. */
    public void showTotalSales() {
        print(buildTotalSales());
    }

    /** Revenue earned. Cancelled tickets are refunded and earn nothing. */
    public void showTotalRevenue() {
        print(buildTotalRevenue());
    }

    /** Revenue split by ticket type. */
    public void showRevenueByType() {
        print(buildRevenueByType());
    }

    /** Busiest departure stations, ranked using a Comparator. */
    public void showBusiestStations() {
        print(buildBusiestStations());
    }

    /** Lists every cancelled ticket. */
    public void showCancelledTickets() {
        print(buildCancelledTickets());
    }

    // ------------------------------------------------------------------
    // File export
    // ------------------------------------------------------------------

    /**
     * Exports the report using an automatically generated file name based on
     * the current date and time, for example report_20260904_143012.txt
     *
     * @return the path the report was written to
     */
    public Path exportReport() {
        return exportReport("report_" + LocalDateTime.now().format(FILE_STAMP) + ".txt");
    }

    /**
     * Exports the report to the given file inside the reports folder.
     *
     * The file is written under a temporary name first and then renamed over
     * the target, so a failure halfway through cannot leave a half-written
     * report behind.
     *
     * @param fileName name of the file to create
     * @return the path the report was written to
     * @throws FileProcessingException if the report cannot be written
     */
    public Path exportReport(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileProcessingException("Report file name must not be empty");
        }

        List<String> lines = buildReport();
        lines.add("");
        lines.add("Generated on " + LocalDateTime.now().format(STAMP));

        Path target = REPORT_DIRECTORY.resolve(fileName.trim());
        Path temporary = REPORT_DIRECTORY.resolve(fileName.trim() + ".tmp");

        try {
            Files.createDirectories(REPORT_DIRECTORY);
            Files.write(temporary, lines, StandardCharsets.UTF_8);
            Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            return target;
        } catch (IOException exception) {
            throw new FileProcessingException("Unable to export report to " + fileName, exception);
        }
    }

    // ------------------------------------------------------------------
    // Report content
    // ------------------------------------------------------------------

    /** Builds every line of the full report. */
    private List<String> buildReport() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add(DIVIDER);
        lines.add("           SMART METRO SYSTEM REPORT");
        lines.add(DIVIDER);

        if (ticketService.getAllTickets().isEmpty()) {
            lines.add("No ticket data available to report.");
            lines.add(DIVIDER);
            return lines;
        }

        lines.addAll(buildTotalSales());
        lines.addAll(buildTotalRevenue());
        lines.addAll(buildRevenueByType());
        lines.addAll(buildBusiestStations());
        lines.addAll(buildCancelledTickets());

        lines.add(DIVIDER);
        lines.add("               END OF REPORT");
        lines.add(DIVIDER);
        return lines;
    }

    private List<String> buildTotalSales() {
        Map<TicketStatus, Integer> counts = new EnumMap<>(TicketStatus.class);
        for (TicketStatus status : TicketStatus.values()) {
            counts.put(status, 0);
        }
        for (Ticket ticket : ticketService.getAllTickets()) {
            counts.put(ticket.getStatus(), counts.get(ticket.getStatus()) + 1);
        }

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- TICKET SALES ---");
        lines.add(String.format("%-22s : %d", "Total tickets sold",
                ticketService.getAllTickets().size()));
        for (TicketStatus status : TicketStatus.values()) {
            lines.add(String.format("%-22s : %d", "  " + status, counts.get(status)));
        }
        return lines;
    }

    private List<String> buildTotalRevenue() {
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

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- REVENUE ---");
        lines.add(String.format("%-22s : RM %.2f", "Total revenue", revenue));
        lines.add(String.format("%-22s : RM %.2f", "Refunded (cancelled)", refunded));
        lines.add(String.format("%-22s : RM %.2f", "Average per ticket", average));
        return lines;
    }

    private List<String> buildRevenueByType() {
        Map<TicketType, Double> byType = new EnumMap<>(TicketType.class);
        for (TicketType type : TicketType.values()) {
            byType.put(type, 0.0);
        }

        double total = 0.0;
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() != TicketStatus.CANCELLED) {
                byType.put(ticket.getTicketType(),
                        byType.get(ticket.getTicketType()) + ticket.getFare());
                total += ticket.getFare();
            }
        }

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- REVENUE BY TICKET TYPE ---");
        lines.add(String.format("%-10s %12s %8s", "TYPE", "REVENUE", "SHARE"));
        for (TicketType type : TicketType.values()) {
            double amount = byType.get(type);
            double share = (total == 0.0) ? 0.0 : (amount / total) * 100;
            lines.add(String.format("%-10s %9s%.2f %7.1f%%", type, "RM ", amount, share));
        }
        return lines;
    }

    private List<String> buildBusiestStations() {
        Map<String, Integer> boardings = new HashMap<>();
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() == TicketStatus.CANCELLED) {
                continue;
            }
            String name = ticket.getSource().getName();
            boardings.put(name, boardings.getOrDefault(name, 0) + 1);
        }

        // Copy the map entries into a list so a Comparator can sort them,
        // highest boarding count first.
        List<Map.Entry<String, Integer>> ranked = new ArrayList<>(boardings.entrySet());
        ranked.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- BUSIEST DEPARTURE STATIONS ---");
        lines.add(String.format("%-5s %-22s %10s", "RANK", "STATION", "BOARDINGS"));
        int rank = 1;
        for (Map.Entry<String, Integer> entry : ranked) {
            if (rank > 5) {
                break;
            }
            lines.add(String.format("%-5d %-22s %10d", rank++, entry.getKey(), entry.getValue()));
        }
        return lines;
    }

    private List<String> buildCancelledTickets() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("--- CANCELLED TICKETS ---");
        lines.add(String.format("%-10s %-16s %-22s %10s",
                "TICKET", "PASSENGER", "JOURNEY", "REFUND"));

        boolean found = false;
        for (Ticket ticket : ticketService.getAllTickets()) {
            if (ticket.getStatus() != TicketStatus.CANCELLED) {
                continue;
            }
            found = true;
            String journey = ticket.getSource().getName() + " -> "
                    + ticket.getDestination().getName();
            lines.add(String.format("%-10s %-16s %-22s %7s%.2f",
                    ticket.getTicketId(), ticket.getPassenger().getName(),
                    journey, "RM ", ticket.getFare()));
        }

        if (!found) {
            lines.add("No cancelled tickets.");
        }
        return lines;
    }

    /** Prints a block of report lines to the console. */
    private void print(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}