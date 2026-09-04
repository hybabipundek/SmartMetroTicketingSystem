package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exception.FileProcessingException;
import enums.TicketStatus;
import enums.TicketType;
import enums.UserRole;
import model.Admin;
import model.Passenger;
import model.Route;
import model.Station;
import model.Ticket;
import model.Train;
import model.User;
import service.RouteService;
import service.StationService;
import service.TicketService;
import service.TrainService;
import service.UserService;

/**
 * File manager implementation that saves and loads the required system data using TXT files.
 */
public class TXTFileManager implements FileManager{

    private final Path dataDirectory;
    private final UserService userService;
    private final StationService stationService;
    private final TrainService trainService;
    private final RouteService routeService;
    private final TicketService ticketService;

// Initializes the TXTFileManager object.
    public TXTFileManager() {
        this(Paths.get("data"), null, null, null, null, null);
    }

    public TXTFileManager(UserService userService, StationService stationService,
                          TrainService trainService, RouteService routeService,
                          TicketService ticketService) {
        this(Paths.get("data"), userService, stationService, trainService,
                routeService, ticketService);
    }

    public TXTFileManager(Path dataDirectory, UserService userService,
                          StationService stationService, TrainService trainService,
                          RouteService routeService, TicketService ticketService) {
        this.dataDirectory = dataDirectory;
        this.userService = userService;
        this.stationService = stationService;
        this.trainService = trainService;
        this.routeService = routeService;
        this.ticketService = ticketService;
    }

    @Override
// Loads application data from the selected file format into the service collections.
    public void loadData() {
        requireServices();
        loadUsers();
        HashMap<String, Station> stations = loadStations();
        loadTrains();
        loadRoutes(stations);
        loadTickets(stations);
    }

    @Override
// Saves the current application data to files.
    public void saveData() {
        requireServices();
        try {
            Files.createDirectories(dataDirectory);
            write("users.txt", userLines());
            write("stations.txt", stationLines());
            write("trains.txt", trainLines());
            write("routes.txt", routeLines());
            write("tickets.txt", ticketLines());
        } catch (IOException exception) {
            throw new FileProcessingException("Unable to save metro data", exception);
        }
    }

// Loads user records from the data file.
    private void loadUsers() {
        if (!Files.exists(file("users.txt"))) return;
        HashMap<String, User> loaded = new HashMap<>();
        for (String line : read("users.txt")) {
            List<String> fields = fields(line, 5);
            UserRole role = enumValue(UserRole.class, fields.get(4), "user role");
            User user;
            if (role == UserRole.PASSENGER) {
                requireFieldCount(fields, 6, "passenger");
                user = new Passenger(fields.get(0), fields.get(1), fields.get(2),
                        fields.get(3), number(fields.get(5), "passenger balance"));
            } else {
                user = new Admin(fields.get(0), fields.get(1), fields.get(2), fields.get(3));
            }
            loaded.put(user.getEmail(), user);
        }
        userService.setAllUsers(loaded);
    }

// Loads station records and returns the stations indexed by ID.
    private HashMap<String, Station> loadStations() {
        HashMap<String, Station> stations = new HashMap<>();
        if (!Files.exists(file("stations.txt"))) return stations;
        ArrayList<Station> loaded = new ArrayList<>();
        for (String line : read("stations.txt")) {
            List<String> fields = fields(line, 3);
            Station station = new Station(fields.get(0), fields.get(1), fields.get(2));
            loaded.add(station);
            stations.put(station.getStationId(), station);
        }
        stationService.setStations(loaded);
        return stations;
    }

// Loads train records into the train service.
    private void loadTrains() {
        if (!Files.exists(file("trains.txt"))) return;
        ArrayList<Train> loaded = new ArrayList<>();
        for (String line : read("trains.txt")) {
            List<String> fields = fields(line, 3);
            loaded.add(new Train(fields.get(0), fields.get(1), integer(fields.get(2), "train capacity")));
        }
        trainService.setTrains(loaded);
    }

// Loads route records and reconnects them to their station objects.
    private void loadRoutes(HashMap<String, Station> stations) {
        if (!Files.exists(file("routes.txt"))) return;
        ArrayList<Route> loaded = new ArrayList<>();
        for (String line : read("routes.txt")) {
            List<String> fields = fields(line, 4);
            Station source = station(stations, fields.get(1));
            Station destination = station(stations, fields.get(2));
            loaded.add(new Route(fields.get(0), source, destination,
                    number(fields.get(3), "route distance")));
        }
        routeService.setRoutes(loaded);
    }

// Loads ticket records and reconnects them to passengers and stations.
    private void loadTickets(HashMap<String, Station> stations) {
        if (!Files.exists(file("tickets.txt"))) return;
        ArrayList<Ticket> loaded = new ArrayList<>();
        for (String line : read("tickets.txt")) {
            List<String> fields = fields(line, 7);
            User user = findUserById(fields.get(1));
            if (!(user instanceof Passenger)) {
                throw new FileProcessingException("Passenger not found for ticket: " + fields.get(0));
            }
            loaded.add(new Ticket(fields.get(0), (Passenger) user,
                    station(stations, fields.get(2)), station(stations, fields.get(3)),
                    enumValue(TicketType.class, fields.get(4), "ticket type"),
                    enumValue(TicketStatus.class, fields.get(5), "ticket status"),
                    number(fields.get(6), "ticket fare")));
        }
        ticketService.setTickets(loaded);
    }

    private List<String> userLines() {
        List<String> lines = new ArrayList<>();

        List<User> users = new ArrayList<>(userService.getAllUsers().values());

        users.sort((user1, user2) ->
            user1.getUserId().compareTo(user2.getUserId())
        );

        for (User user : users) {
            String line = String.join("|",
                    escape(user.getUserId()),
                    escape(user.getName()),
                    escape(user.getEmail()),
                    escape(user.getPassword()),
                    user.getRole().name());

            if (user instanceof Passenger) {
                line += "|" + String.format("%.2f", ((Passenger) user).getBalance());
            }

            lines.add(line);
        }

        return lines;
    }
    private List<String> stationLines() {
        List<String> lines = new ArrayList<>();
        for (Station station : stationService.getAllStations()) {
            lines.add(String.join("|", escape(station.getStationId()), escape(station.getName()),
                    escape(station.getLocation())));
        }
        return lines;
    }

    private List<String> trainLines() {
        List<String> lines = new ArrayList<>();
        for (Train train : trainService.getAllTrains()) {
            lines.add(String.join("|", escape(train.getTrainId()), escape(train.getTrainName()),
                    Integer.toString(train.getCapacity())));
        }
        return lines;
    }

    private List<String> routeLines() {
        List<String> lines = new ArrayList<>();
        for (Route route : routeService.getAllRoutes()) {
            lines.add(String.join("|", escape(route.getRouteId()), escape(route.getSource().getStationId()),
                    escape(route.getDestination().getStationId()), Double.toString(route.getDistanceKm())));
        }
        return lines;
    }

    private List<String> ticketLines() {
        List<String> lines = new ArrayList<>();
        for (Ticket ticket : ticketService.getAllTickets()) {
            lines.add(String.join("|", escape(ticket.getTicketId()), escape(ticket.getPassenger().getUserId()),
                    escape(ticket.getSource().getStationId()), escape(ticket.getDestination().getStationId()),
                    ticket.getTicketType().name(), ticket.getStatus().name(), String.format("%.2f", ticket.getFare())));
        }
        return lines;
    }

    private void write(String name, List<String> lines) throws IOException {
        Files.write(file(name), lines, StandardCharsets.UTF_8);
    }

// Reads lines from the requested TXT data file.
    private List<String> read(String name) {
        try {
            List<String> lines = new ArrayList<>();
            for (String line : Files.readAllLines(file(name), StandardCharsets.UTF_8)) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
            return lines;
        } catch (IOException exception) {
            throw new FileProcessingException("Unable to read " + name, exception);
        }
    }

// Builds the path for a data file.
    private Path file(String name) {
        return dataDirectory.resolve(name);
    }

// Checks that the required service objects are available before file operations.
    private void requireServices() {
        if (userService == null || stationService == null || trainService == null
                || routeService == null || ticketService == null) {
            throw new FileProcessingException("TXTFileManager requires all metro services");
        }
    }

// Splits and validates a TXT record into its individual fields.
    private List<String> fields(String line, int minimum) {
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (escaped) {
                field.append(character == 'n' ? '\n' : character == 'r' ? '\r' : character);
                escaped = false;
            } else if (character == '\\') {
                escaped = true;
            } else if (character == '|') {
                result.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (escaped) field.append('\\');
        result.add(field.toString());
        requireFieldCount(result, minimum, "record");
        return result;
    }

// Checks that a loaded record contains the required number of fields.
    private void requireFieldCount(List<String> fields, int minimum, String type) {
        if (fields.size() < minimum) throw new FileProcessingException("Invalid " + type + " record");
    }

// Escapes text values before they are written to a TXT data file.
    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

// Finds a station object by station ID while loading TXT records.
    private Station station(HashMap<String, Station> stations, String id) {
        Station station = stations.get(id);
        if (station == null) throw new FileProcessingException("Station not found: " + id);
        return station;
    }

// Finds a user object using its user ID.
    private User findUserById(String id) {
        for (User user : userService.getAllUsers().values()) {
            if (id.equals(user.getUserId())) return user;
        }
        return null;
    }

// Converts a TXT field into an integer value.
    private int integer(String value, String field) {
        try { return Integer.parseInt(value); }
        catch (NumberFormatException exception) { throw new FileProcessingException("Invalid " + field + ": " + value, exception); }
    }

// Converts a TXT field into a numeric value.
    private double number(String value, String field) {
        try { return Double.parseDouble(value); }
        catch (NumberFormatException exception) { throw new FileProcessingException("Invalid " + field + ": " + value, exception); }
    }

// Converts a stored TXT value into the corresponding enum constant.
    private <T extends Enum<T>> T enumValue(Class<T> type, String value, String field) {
        try { return Enum.valueOf(type, value); }
        catch (IllegalArgumentException exception) { throw new FileProcessingException("Invalid " + field + ": " + value, exception); }
    }

}
