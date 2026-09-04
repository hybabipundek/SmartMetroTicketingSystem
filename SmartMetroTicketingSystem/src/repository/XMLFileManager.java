package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

/**
 * Optional file manager implementation that saves and loads system data using XML format.
 */
public class XMLFileManager implements FileManager {

    private final Path dataDirectory;
    private final UserService userService;
    private final StationService stationService;
    private final TrainService trainService;
    private final RouteService routeService;
    private final TicketService ticketService;

// Initializes the XMLFileManager object.
    public XMLFileManager() {
        this(Paths.get("data"), null, null, null, null, null);
    }

    public XMLFileManager(UserService userService, StationService stationService,
                          TrainService trainService, RouteService routeService,
                          TicketService ticketService) {
        this(Paths.get("data"), userService, stationService, trainService,
                routeService, ticketService);
    }

    public XMLFileManager(Path dataDirectory, UserService userService,
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
            saveUsers();
            saveStations();
            saveTrains();
            saveRoutes();
            saveTickets();
        } catch (IOException | ParserConfigurationException | TransformerException exception) {
            throw new FileProcessingException("Unable to save XML metro data", exception);
        }
    }

// Loads user records from the data file.
    private void loadUsers() {
        Document document = document("users.xml");
        if (document == null) return;
        HashMap<String, User> loaded = new HashMap<>();
        for (Element element : records(document)) {
            UserRole role = enumValue(UserRole.class, text(element, "role"), "user role");
            User user;
            if (role == UserRole.PASSENGER) {
                user = new Passenger(text(element, "userId"), text(element, "name"),
                        text(element, "email"), text(element, "password"),
                        number(element, "balance"));
            } else {
                user = new Admin(text(element, "userId"), text(element, "name"),
                        text(element, "email"), text(element, "password"));
            }
            loaded.put(user.getEmail(), user);
        }
        userService.setAllUsers(loaded);
    }

// Loads station records and returns the stations indexed by ID.
    private HashMap<String, Station> loadStations() {
        Document document = document("stations.xml");
        HashMap<String, Station> stations = new HashMap<>();
        if (document == null) return stations;
        ArrayList<Station> loaded = new ArrayList<>();
        for (Element element : records(document)) {
            Station station = new Station(text(element, "stationId"), text(element, "name"),
                    text(element, "location"));
            loaded.add(station);
            stations.put(station.getStationId(), station);
        }
        stationService.setStations(loaded);
        return stations;
    }

// Loads train records into the train service.
    private void loadTrains() {
        Document document = document("trains.xml");
        if (document == null) return;
        ArrayList<Train> loaded = new ArrayList<>();
        for (Element element : records(document)) {
            loaded.add(new Train(text(element, "trainId"), text(element, "trainName"),
                    integer(element, "capacity")));
        }
        trainService.setTrains(loaded);
    }

// Loads route records and reconnects them to their station objects.
    private void loadRoutes(HashMap<String, Station> stations) {
        Document document = document("routes.xml");
        if (document == null) return;
        ArrayList<Route> loaded = new ArrayList<>();
        for (Element element : records(document)) {
            loaded.add(new Route(text(element, "routeId"), station(stations, text(element, "sourceStationId")),
                    station(stations, text(element, "destinationStationId")), number(element, "distanceKm")));
        }
        routeService.setRoutes(loaded);
    }

// Loads ticket records and reconnects them to passengers and stations.
    private void loadTickets(HashMap<String, Station> stations) {
        Document document = document("tickets.xml");
        if (document == null) return;
        ArrayList<Ticket> loaded = new ArrayList<>();
        for (Element element : records(document)) {
            User user = findUserById(text(element, "passengerId"));
            if (!(user instanceof Passenger passenger)) {
                throw new FileProcessingException("Passenger not found for ticket: " + text(element, "ticketId"));
            }
            loaded.add(new Ticket(text(element, "ticketId"), passenger,
                    station(stations, text(element, "sourceStationId")),
                    station(stations, text(element, "destinationStationId")),
                    enumValue(TicketType.class, text(element, "ticketType"), "ticket type"),
                    enumValue(TicketStatus.class, text(element, "status"), "ticket status"),
                    number(element, "fare")));
        }
        ticketService.setTickets(loaded);
    }

    private void saveUsers() throws ParserConfigurationException, TransformerException {
        Document document = document("users");
        Element root = document.getDocumentElement();
        for (User user : userService.getAllUsers().values()) {
            Element record = document.createElement("user");
            child(document, record, "userId", user.getUserId());
            child(document, record, "name", user.getName());
            child(document, record, "email", user.getEmail());
            child(document, record, "password", user.getPassword());
            child(document, record, "role", user.getRole().name());
            if (user instanceof Passenger passenger) child(document, record, "balance", passenger.getBalance());
            root.appendChild(record);
        }
        write("users.xml", document);
    }

    private void saveStations() throws ParserConfigurationException, TransformerException {
        Document document = document("stations");
        Element root = document.getDocumentElement();
        for (Station station : stationService.getAllStations()) {
            Element record = document.createElement("station");
            child(document, record, "stationId", station.getStationId());
            child(document, record, "name", station.getName());
            child(document, record, "location", station.getLocation());
            root.appendChild(record);
        }
        write("stations.xml", document);
    }

    private void saveTrains() throws ParserConfigurationException, TransformerException {
        Document document = document("trains");
        Element root = document.getDocumentElement();
        for (Train train : trainService.getAllTrains()) {
            Element record = document.createElement("train");
            child(document, record, "trainId", train.getTrainId());
            child(document, record, "trainName", train.getTrainName());
            child(document, record, "capacity", train.getCapacity());
            root.appendChild(record);
        }
        write("trains.xml", document);
    }

    private void saveRoutes() throws ParserConfigurationException, TransformerException {
        Document document = document("routes");
        Element root = document.getDocumentElement();
        for (Route route : routeService.getAllRoutes()) {
            Element record = document.createElement("route");
            child(document, record, "routeId", route.getRouteId());
            child(document, record, "sourceStationId", route.getSource().getStationId());
            child(document, record, "destinationStationId", route.getDestination().getStationId());
            child(document, record, "distanceKm", route.getDistanceKm());
            root.appendChild(record);
        }
        write("routes.xml", document);
    }

    private void saveTickets() throws ParserConfigurationException, TransformerException {
        Document document = document("tickets");
        Element root = document.getDocumentElement();
        for (Ticket ticket : ticketService.getAllTickets()) {
            Element record = document.createElement("ticket");
            child(document, record, "ticketId", ticket.getTicketId());
            child(document, record, "passengerId", ticket.getPassenger().getUserId());
            child(document, record, "sourceStationId", ticket.getSource().getStationId());
            child(document, record, "destinationStationId", ticket.getDestination().getStationId());
            child(document, record, "ticketType", ticket.getTicketType().name());
            child(document, record, "status", ticket.getStatus().name());
            child(document, record, "fare", ticket.getFare());
            root.appendChild(record);
        }
        write("tickets.xml", document);
    }

// Loads an XML document from the requested file.
    private Document document(String name) {
        Path path = dataDirectory.resolve(name);
        if (name.endsWith(".xml")) {
            if (!Files.exists(path)) return null;
            try {
                return builder().parse(path.toFile());
            } catch (Exception exception) {
                throw new FileProcessingException("Unable to read " + name, exception);
            }
        }
        try {
            Document document = builder().newDocument();
            document.appendChild(document.createElement(name));
            return document;
        } catch (ParserConfigurationException exception) {
            throw new FileProcessingException("Unable to create XML document", exception);
        }
    }

    private DocumentBuilder builder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory.newDocumentBuilder();
    }

    private void write(String name, Document document) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(document), new StreamResult(dataDirectory.resolve(name).toFile()));
    }

// Retrieves record elements from an XML document.
    private ArrayList<Element> records(Document document) {
        ArrayList<Element> result = new ArrayList<>();
        NodeList nodes = document.getDocumentElement().getChildNodes();
        for (int index = 0; index < nodes.getLength(); index++) {
            Node node = nodes.item(index);
            if (node instanceof Element element) result.add(element);
        }
        return result;
    }

// Adds a child XML element containing the supplied value.
    private void child(Document document, Element parent, String name, Object value) {
        Element element = document.createElement(name);
        element.setTextContent(String.valueOf(value));
        parent.appendChild(element);
    }

// Reads text from a named XML child element.
    private String text(Element element, String name) {
        NodeList nodes = element.getElementsByTagName(name);
        if (nodes.getLength() == 0) throw new FileProcessingException("Missing XML field: " + name);
        return nodes.item(0).getTextContent();
    }

// Retrieves and converts an integer field from a loaded record.
    private int integer(Element element, String name) {
        try {
            return Integer.parseInt(text(element, name));
        } catch (NumberFormatException exception) {
            throw new FileProcessingException("Invalid " + name + ": " + text(element, name), exception);
        }
    }

// Retrieves and converts a numeric field from a loaded record.
    private double number(Element element, String name) {
        try {
            return Double.parseDouble(text(element, name));
        } catch (NumberFormatException exception) {
            throw new FileProcessingException("Invalid " + name + ": " + text(element, name), exception);
        }
    }

// Converts a stored XML value into the corresponding enum constant.
    private <T extends Enum<T>> T enumValue(Class<T> type, String value, String field) {
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException exception) {
            throw new FileProcessingException("Invalid " + field + ": " + value, exception);
        }
    }

// Finds a station object by station ID while loading XML records.
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

// Checks that the required service objects are available before file operations.
    private void requireServices() {
        if (userService == null || stationService == null || trainService == null
                || routeService == null || ticketService == null) {
            throw new FileProcessingException("XMLFileManager requires all metro services");
        }
    }
}
