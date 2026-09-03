package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import enums.TicketStatus;
import enums.TicketType;
import enums.UserRole;
import exception.FileProcessingException;
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

public class JSONFileManager implements FileManager {

    private final Path dataDirectory;
    private final UserService userService;
    private final StationService stationService;
    private final TrainService trainService;
    private final RouteService routeService;
    private final TicketService ticketService;

    public JSONFileManager() {
        this(Paths.get("data"), null, null, null, null, null);
    }

    public JSONFileManager(UserService userService, StationService stationService,
                           TrainService trainService, RouteService routeService,
                           TicketService ticketService) {
        this(Paths.get("data"), userService, stationService, trainService,
                routeService, ticketService);
    }

    public JSONFileManager(Path dataDirectory, UserService userService,
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
    public void loadData() {
        requireServices();
        loadUsers();
        HashMap<String, Station> stations = loadStations();
        loadTrains();
        loadRoutes(stations);
        loadTickets(stations);
    }

    @Override
    public void saveData() {
        requireServices();
        try {
            Files.createDirectories(dataDirectory);
            write("users.json", usersJson());
            write("stations.json", stationsJson());
            write("trains.json", trainsJson());
            write("routes.json", routesJson());
            write("tickets.json", ticketsJson());
        } catch (IOException | RuntimeException exception) {
            if (exception instanceof FileProcessingException fileException) throw fileException;
            throw new FileProcessingException("Unable to save JSON metro data", exception);
        }
    }

    private void loadUsers() {
        List<Map<String, Object>> records = records("users.json");
        if (records == null) return;
        HashMap<String, User> loaded = new HashMap<>();
        for (Map<String, Object> record : records) {
            UserRole role = enumValue(UserRole.class, string(record, "role"), "user role");
            User user;
            if (role == UserRole.PASSENGER) {
                user = new Passenger(string(record, "userId"), string(record, "name"),
                        string(record, "email"), string(record, "password"),
                        number(record, "balance"));
            } else {
                user = new Admin(string(record, "userId"), string(record, "name"),
                        string(record, "email"), string(record, "password"));
            }
            loaded.put(user.getEmail(), user);
        }
        userService.setAllUsers(loaded);
    }

    private HashMap<String, Station> loadStations() {
        List<Map<String, Object>> records = records("stations.json");
        HashMap<String, Station> stations = new HashMap<>();
        if (records == null) return stations;
        ArrayList<Station> loaded = new ArrayList<>();
        for (Map<String, Object> record : records) {
            Station station = new Station(string(record, "stationId"), string(record, "name"),
                    string(record, "location"));
            loaded.add(station);
            stations.put(station.getStationId(), station);
        }
        stationService.setStations(loaded);
        return stations;
    }

    private void loadTrains() {
        List<Map<String, Object>> records = records("trains.json");
        if (records == null) return;
        ArrayList<Train> loaded = new ArrayList<>();
        for (Map<String, Object> record : records) {
            loaded.add(new Train(string(record, "trainId"), string(record, "trainName"),
                    integer(record, "capacity")));
        }
        trainService.setTrains(loaded);
    }

    private void loadRoutes(HashMap<String, Station> stations) {
        List<Map<String, Object>> records = records("routes.json");
        if (records == null) return;
        ArrayList<Route> loaded = new ArrayList<>();
        for (Map<String, Object> record : records) {
            loaded.add(new Route(string(record, "routeId"), station(stations, string(record, "sourceStationId")),
                    station(stations, string(record, "destinationStationId")), number(record, "distanceKm")));
        }
        routeService.setRoutes(loaded);
    }

    private void loadTickets(HashMap<String, Station> stations) {
        List<Map<String, Object>> records = records("tickets.json");
        if (records == null) return;
        ArrayList<Ticket> loaded = new ArrayList<>();
        for (Map<String, Object> record : records) {
            User user = findUserById(string(record, "passengerId"));
            if (!(user instanceof Passenger passenger)) {
                throw new FileProcessingException("Passenger not found for ticket: " + string(record, "ticketId"));
            }
            loaded.add(new Ticket(string(record, "ticketId"), passenger,
                    station(stations, string(record, "sourceStationId")),
                    station(stations, string(record, "destinationStationId")),
                    enumValue(TicketType.class, string(record, "ticketType"), "ticket type"),
                    enumValue(TicketStatus.class, string(record, "status"), "ticket status"),
                    number(record, "fare")));
        }
        ticketService.setTickets(loaded);
    }

    private String usersJson() {
        List<Map<String, Object>> records = new ArrayList<>();
        for (User user : userService.getAllUsers().values()) {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("userId", user.getUserId());
            record.put("name", user.getName());
            record.put("email", user.getEmail());
            record.put("password", user.getPassword());
            record.put("role", user.getRole().name());
            if (user instanceof Passenger passenger) record.put("balance", passenger.getBalance());
            records.add(record);
        }
        return Json.write(records);
    }

    private String stationsJson() {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Station station : stationService.getAllStations()) {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("stationId", station.getStationId());
            record.put("name", station.getName());
            record.put("location", station.getLocation());
            records.add(record);
        }
        return Json.write(records);
    }

    private String trainsJson() {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Train train : trainService.getAllTrains()) {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("trainId", train.getTrainId());
            record.put("trainName", train.getTrainName());
            record.put("capacity", train.getCapacity());
            records.add(record);
        }
        return Json.write(records);
    }

    private String routesJson() {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Route route : routeService.getAllRoutes()) {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("routeId", route.getRouteId());
            record.put("sourceStationId", route.getSource().getStationId());
            record.put("destinationStationId", route.getDestination().getStationId());
            record.put("distanceKm", route.getDistanceKm());
            records.add(record);
        }
        return Json.write(records);
    }

    private String ticketsJson() {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Ticket ticket : ticketService.getAllTickets()) {
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("ticketId", ticket.getTicketId());
            record.put("passengerId", ticket.getPassenger().getUserId());
            record.put("sourceStationId", ticket.getSource().getStationId());
            record.put("destinationStationId", ticket.getDestination().getStationId());
            record.put("ticketType", ticket.getTicketType().name());
            record.put("status", ticket.getStatus().name());
            record.put("fare", ticket.getFare());
            records.add(record);
        }
        return Json.write(records);
    }

    private void write(String name, String content) throws IOException {
        Files.writeString(dataDirectory.resolve(name), content, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> records(String name) {
        Path path = dataDirectory.resolve(name);
        if (!Files.exists(path)) return null;
        try {
            Object value = Json.parse(Files.readString(path, StandardCharsets.UTF_8));
            if (!(value instanceof List<?> list)) throw new FileProcessingException("JSON root must be an array: " + name);
            List<Map<String, Object>> result = new ArrayList<>();
            for (Object item : list) {
                if (!(item instanceof Map<?, ?> map)) throw new FileProcessingException("JSON record must be an object: " + name);
                result.add((Map<String, Object>) map);
            }
            return result;
        } catch (IOException | FileProcessingException exception) {
            if (exception instanceof FileProcessingException fileException) throw fileException;
            throw new FileProcessingException("Unable to read " + name, exception);
        } catch (RuntimeException exception) {
            throw new FileProcessingException("Malformed JSON in " + name, exception);
        }
    }

    private String string(Map<String, Object> record, String field) {
        Object value = record.get(field);
        if (!(value instanceof String)) throw new FileProcessingException("Invalid JSON field: " + field);
        return (String) value;
    }

    private int integer(Map<String, Object> record, String field) {
        Object value = record.get(field);
        if (!(value instanceof Number)) throw new FileProcessingException("Invalid JSON field: " + field);
        return ((Number) value).intValue();
    }

    private double number(Map<String, Object> record, String field) {
        Object value = record.get(field);
        if (!(value instanceof Number)) throw new FileProcessingException("Invalid JSON field: " + field);
        return ((Number) value).doubleValue();
    }

    private <T extends Enum<T>> T enumValue(Class<T> type, String value, String field) {
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException exception) {
            throw new FileProcessingException("Invalid " + field + ": " + value, exception);
        }
    }

    private Station station(HashMap<String, Station> stations, String id) {
        Station station = stations.get(id);
        if (station == null) throw new FileProcessingException("Station not found: " + id);
        return station;
    }

    private User findUserById(String id) {
        for (User user : userService.getAllUsers().values()) {
            if (id.equals(user.getUserId())) return user;
        }
        return null;
    }

    private void requireServices() {
        if (userService == null || stationService == null || trainService == null
                || routeService == null || ticketService == null) {
            throw new FileProcessingException("JSONFileManager requires all metro services");
        }
    }

    private static final class Json {
        private Json() {
        }

        static String write(Object value) {
            StringBuilder result = new StringBuilder();
            append(result, value);
            return result.toString();
        }

        private static void append(StringBuilder result, Object value) {
            if (value == null) {
                result.append("null");
            } else if (value instanceof String string) {
                result.append('"');
                for (int index = 0; index < string.length(); index++) {
                    char character = string.charAt(index);
                    if (character == '"' || character == '\\') result.append('\\').append(character);
                    else if (character == '\n') result.append("\\n");
                    else if (character == '\r') result.append("\\r");
                    else if (character == '\t') result.append("\\t");
                    else if (character < 32) result.append(String.format("\\u%04x", (int) character));
                    else result.append(character);
                }
                result.append('"');
            } else if (value instanceof Number || value instanceof Boolean) {
                result.append(value);
            } else if (value instanceof List<?> list) {
                result.append('[');
                for (int index = 0; index < list.size(); index++) {
                    if (index > 0) result.append(',');
                    append(result, list.get(index));
                }
                result.append(']');
            } else if (value instanceof Map<?, ?> map) {
                result.append('{');
                boolean first = true;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (!first) result.append(',');
                    first = false;
                    append(result, entry.getKey().toString());
                    result.append(':');
                    append(result, entry.getValue());
                }
                result.append('}');
            } else {
                throw new IllegalArgumentException("Unsupported JSON value");
            }
        }

        static Object parse(String value) {
            Parser parser = new Parser(value);
            Object result = parser.value();
            parser.skipWhitespace();
            if (!parser.end()) throw new IllegalArgumentException("Unexpected JSON content");
            return result;
        }

        private static final class Parser {
            private final String input;
            private int position;

            Parser(String input) {
                this.input = input;
            }

            Object value() {
                skipWhitespace();
                if (end()) throw new IllegalArgumentException("Missing JSON value");
                char character = input.charAt(position);
                if (character == '{') return object();
                if (character == '[') return array();
                if (character == '"') return string();
                if (input.startsWith("true", position)) { position += 4; return Boolean.TRUE; }
                if (input.startsWith("false", position)) { position += 5; return Boolean.FALSE; }
                if (input.startsWith("null", position)) { position += 4; return null; }
                return number();
            }

            Map<String, Object> object() {
                Map<String, Object> result = new LinkedHashMap<>();
                position++;
                skipWhitespace();
                if (take('}')) return result;
                while (true) {
                    skipWhitespace();
                    if (end() || input.charAt(position) != '"') throw new IllegalArgumentException("Invalid JSON object");
                    String key = string();
                    skipWhitespace();
                    expect(':');
                    result.put(key, value());
                    skipWhitespace();
                    if (take('}')) return result;
                    expect(',');
                }
            }

            List<Object> array() {
                List<Object> result = new ArrayList<>();
                position++;
                skipWhitespace();
                if (take(']')) return result;
                while (true) {
                    result.add(value());
                    skipWhitespace();
                    if (take(']')) return result;
                    expect(',');
                }
            }

            String string() {
                expect('"');
                StringBuilder result = new StringBuilder();
                while (!end()) {
                    char character = input.charAt(position++);
                    if (character == '"') return result.toString();
                    if (character != '\\') {
                        result.append(character);
                        continue;
                    }
                    if (end()) throw new IllegalArgumentException("Invalid JSON string");
                    character = input.charAt(position++);
                    if (character == '"' || character == '\\' || character == '/') result.append(character);
                    else if (character == 'b') result.append('\b');
                    else if (character == 'f') result.append('\f');
                    else if (character == 'n') result.append('\n');
                    else if (character == 'r') result.append('\r');
                    else if (character == 't') result.append('\t');
                    else if (character == 'u') {
                        if (position + 4 > input.length()) throw new IllegalArgumentException("Invalid JSON escape");
                        result.append((char) Integer.parseInt(input.substring(position, position + 4), 16));
                        position += 4;
                    } else throw new IllegalArgumentException("Invalid JSON escape");
                }
                throw new IllegalArgumentException("Unclosed JSON string");
            }

            Number number() {
                int start = position;
                while (!end() && "-+0123456789.eE".indexOf(input.charAt(position)) >= 0) position++;
                String number = input.substring(start, position);
                if (number.contains(".") || number.contains("e") || number.contains("E")) return Double.parseDouble(number);
                return Long.parseLong(number);
            }

            void skipWhitespace() {
                while (!end() && Character.isWhitespace(input.charAt(position))) position++;
            }

            void expect(char expected) {
                if (!take(expected)) throw new IllegalArgumentException("Expected JSON character: " + expected);
            }

            boolean take(char expected) {
                if (!end() && input.charAt(position) == expected) {
                    position++;
                    return true;
                }
                return false;
            }

            boolean end() {
                return position >= input.length();
            }
        }
    }
}
