package app;

import java.util.Scanner;

import enums.TicketType;
import exception.FileProcessingException;
import exception.InvalidLoginException;
import exception.TicketNotFoundException;
import model.Admin;
import model.Passenger;
import model.Route;
import model.Station;
import model.Ticket;
import model.Train;
import model.User;
import payment.BalancePayment;
import payment.CardPayment;
import payment.CashPayment;
import payment.Payment;
import repository.FileManager;
import repository.TXTFileManager;
import service.PaymentService;
import service.ReportService;
import service.RouteService;
import service.StationService;
import service.TicketService;
import service.TrainService;
import service.UserService;
import utils.Validation;

/**
 * Main application class that provides the menu-driven console interface and coordinates the system services.
 */
public class Main {

    private final Scanner scanner = new Scanner(System.in);

    private final UserService userService = new UserService();
    private final StationService stationService = new StationService();
    private final TrainService trainService = new TrainService();
    private final TicketService ticketService = new TicketService();
    private final RouteService routeService = new RouteService();
    private final ReportService reportService = new ReportService(ticketService);
    private final PaymentService paymentService = new PaymentService();
    private final FileManager fileManager = new TXTFileManager(
            userService, stationService, trainService, routeService, ticketService);

// Program entry point that creates the Main application and starts the system.
    public static void main(String[] args) {
        Main system = new Main();
        system.start();
    }

// Displays Main information in the console.
    private void printMainHeader() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              SMART METRO TICKETING SYSTEM                ║");
        System.out.println("║                  Welcome to Smart Metro                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

// Displays Main information in the console.
    private void printSectionHeader(String title) {
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-56s │%n", title);
        System.out.println("└──────────────────────────────────────────────────────────┘");
    }

// Displays Main information in the console.
    private void printMainMenu() {
        System.out.println();
        System.out.println("┌─────────────────────── MAIN MENU ────────────────────────┐");
        System.out.println("│  1.  Login                                               │");
        System.out.println("│  2.  Register                                            │");
        System.out.println("│  3.  Exit                                                │");
        System.out.println("│                                                          │");
        System.out.println("│  New to Smart Metro? Start by registering.	           │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.print(" Enter your choice: ");
    }

// Displays Main information in the console.
    private void printPassengerMenu(Passenger passenger) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                  PASSENGER DASHBOARD                     ║");
        System.out.printf("║  Passenger: %-43s  ║%n", passenger.getName());
        System.out.printf("║  Balance:   RM %-40.2f  ║%n", passenger.getBalance());
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("┌──────────────────── PASSENGER MENU ──────────────────────┐");
        System.out.println("│  1.  View Profile                                        │");
        System.out.println("│  2.  View Stations                                       │");
        System.out.println("│  3.  View Routes                                         │");
        System.out.println("│  4.  Buy Ticket                                          │");
        System.out.println("│  5.  View My Tickets                                     │");
        System.out.println("│  6.  Cancel Ticket                                       │");
        System.out.println("│  7.  Top Up Balance                                      │");
        System.out.println("│  8.  Logout                                              │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.print("  Enter your choice: ");
    }

// Displays Main information in the console.
    private void printAdminMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    ADMIN DASHBOARD                       ║");
        System.out.println("║              System Management & Reports                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("┌───────────────────── ADMIN MENU ─────────────────────────┐");
        System.out.println("│  1.  Add Station                                         │");
        System.out.println("│  2.  View Stations                                       │");
        System.out.println("│  3.  Search Station                                      │");
        System.out.println("│  4.  Sort Stations                                       │");
        System.out.println("│  5.  Add Train                                           │");
        System.out.println("│  6.  View Trains                                         │");
        System.out.println("│  7.  Add Route                                           │");
        System.out.println("│  8.  View Routes                                         │");
        System.out.println("│  9.  View Users                                          │");
        System.out.println("│ 10.  Update User                                         │");
        System.out.println("│ 11.  Delete User                                         │");
        System.out.println("│ 12.  View Reports                                        │");
        System.out.println("│ 13.  Export Report to File                               │");
        System.out.println("│ 14.  Logout                                              │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.print("  Enter your choice: ");
    }

// Loads saved data and controls the main menu until the user chooses to exit.
    public void start() {
        try {
            fileManager.loadData();
        } catch (FileProcessingException exception) {
            System.out.println("Unable to load saved data: " + exception.getMessage());
        }

        printMainHeader();
        boolean running = true;

        while (running) {
            printMainMenu();
            String choiceInput = scanner.nextLine().trim();

            if (!Validation.validateChoice(choiceInput, 1, 3)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int choice = Integer.parseInt(choiceInput);
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    try {
                        fileManager.saveData();
                    } catch (FileProcessingException exception) {
                        System.out.println("Unable to save data: " + exception.getMessage());
                    }
                    running = false;
                    System.out.println("Thank you for using Smart Metro Ticketing System.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

// Authenticates a user and opens the appropriate Passenger or Admin menu.
    public void login() {
        printSectionHeader("LOGIN");

        String email;
        while (true) {
            System.out.print("Please enter your email (or 0 to cancel): ");
            email = scanner.nextLine().trim();

            if (email.equals("0")) {
                System.out.println("Login cancelled.");
                return;
            }

            if (!Validation.validateEmail(email)) {
                System.out.println("Invalid email. Please try again.");
                continue;
            }

            if (!userService.emailExists(email)) {
                System.out.println("Email does not exist. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter password (or 0 to cancel): ");
            String password = scanner.nextLine();

            if (password.equals("0")) {
                System.out.println("Login cancelled.");
                return;
            }

            if (!Validation.validatePassword(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            try {
                User user = userService.login(email, password);
                if (user instanceof Passenger) {
                    Passenger passenger = (Passenger) user;
                    System.out.println();
                    System.out.println("✓ Login successful.");
                    System.out.println("Welcome, " + passenger.getName() + "!");
                    passengerMenu(passenger);
                    return;
                }

                if (user instanceof Admin) {
                    Admin admin = (Admin) user;
                    System.out.println();
                    System.out.println("✓ Login successful.");
                    System.out.println("Welcome, Admin!");
                    System.out.println("Admin ID: " + admin.getUserId());
                    adminMenu(admin);
                    return;
                }
            } catch (InvalidLoginException exception) {
                System.out.println("Invalid password. Please try again.");
            }
        }
    }

// Handles passenger registration through the console interface.
    public void register() {
        printSectionHeader("CREATE PASSENGER ACCOUNT");

        String name;
        while (true) {
            System.out.print("Enter name (or 0 to cancel): ");
            name = scanner.nextLine().trim();
            if (name.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (Validation.validateName(name)) break;
            System.out.println("Invalid name. Please try again.");
        }

        String email;
        while (true) {
            System.out.print("Enter email (or 0 to cancel): ");
            email = scanner.nextLine().trim();
            if (email.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (!Validation.validateEmail(email)) {
                System.out.println("Invalid email. Please try again.");
                continue;
            }
            if (userService.emailExists(email)) {
                System.out.println("Email already exists. Please try again.");
                continue;
            }
            break;
        }

        String password;
        while (true) {
            System.out.print("Enter password (or 0 to cancel): ");
            password = scanner.nextLine();
            if (password.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (Validation.validatePassword(password)) break;
            System.out.println("Invalid password. Please try again.");
        }

        while (true) {
            System.out.print("Enter initial balance (RM 0 allowed, -1 to cancel): ");
            String balanceInput = scanner.nextLine().trim();

            if (balanceInput.equals("-1")) {
                System.out.println("Registration cancelled.");
                return;
            }

            if (!Validation.validateNumber(balanceInput)) {
                System.out.println("Invalid balance. Please try again.");
                continue;
            }

            double balance = Double.parseDouble(balanceInput);
            if (userService.registerPassenger(name, email, password, balance)) return;
        }
    }

// Displays and processes the available operations for the logged-in passenger.
    public void passengerMenu(Passenger passenger) {
        boolean passengerRunning = true;

        while (passengerRunning) {
            printPassengerMenu(passenger);
            String choiceInput = scanner.nextLine().trim();

            if (!Validation.validateChoice(choiceInput, 1, 8)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int choice = Integer.parseInt(choiceInput);
            switch (choice) {
                case 1:
                    printSectionHeader("MY PROFILE");
                    System.out.println(passenger);
                    break;
                case 2:
                    printSectionHeader("METRO STATIONS");
                    stationService.viewStations();
                    break;
                case 3:
                    printSectionHeader("METRO ROUTES");
                    routeService.viewRoutes();
                    break;
                case 4:
                    buyticket(passenger);
                    break;
                case 5:
                    printSectionHeader("MY TICKETS");
                    ticketService.viewTickets(passenger);
                    break;
                case 6:
                    cancelPassengerTicket(passenger);
                    break;
                case 7:
                    topUpBalance(passenger);
                    break;
                case 8:
                    passengerRunning = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

// Handles cancellation of a ticket belonging to the current passenger.
    private void cancelPassengerTicket(Passenger passenger) {
        printSectionHeader("CANCEL TICKET");

        while (true) {
            System.out.print("Enter ticket ID (or 0 to cancel): ");
            String ticketId = scanner.nextLine().trim();

            if (ticketId.equals("0")) {
                System.out.println("Ticket cancellation cancelled.");
                return;
            }

            if (!Validation.validateTicketID(ticketId)) {
                System.out.println("Invalid ticket ID. Please try again.");
                continue;
            }

            try {
                ticketService.cancelTicket(ticketId, passenger);
            } catch (TicketNotFoundException exception) {
                System.out.println(exception.getMessage());
            }
            return;
        }
    }

// Validates and adds a top-up amount to a passenger account.
    private void topUpBalance(Passenger passenger) {
        printSectionHeader("TOP UP BALANCE");

        while (true) {
            System.out.print("Enter top up amount (or 0 to cancel): ");
            String amountInput = scanner.nextLine().trim();

            if (amountInput.equals("0")) {
                System.out.println("Top up cancelled.");
                return;
            }

            if (!Validation.validatePositiveNumber(amountInput)) {
                System.out.println("Invalid amount. Please enter an amount greater than RM 0.");
                continue;
            }

            double amount = Double.parseDouble(amountInput);
            userService.topUpBalance(passenger, amount);
            return;
        }
    }

// Finds a route in the route service using its route ID.
    private Route findRouteById(String routeId) {
        for (Route route : routeService.getAllRoutes()) {
            if (route.getRouteId().equalsIgnoreCase(routeId)) return route;
        }
        return null;
    }

// Handles ticket booking by selecting a route and ticket type, calculating the fare, and processing payment.
    public void buyticket(Passenger passenger) {
        printSectionHeader("BUY METRO TICKET");
        System.out.println("\n===== BUY TICKET =====");

        Route route;
        while (true) {
            System.out.println("Available routes:\n");
            routeService.viewRoutes();
            System.out.print("\nEnter Route ID (or 0 to cancel): ");
            String routeId = scanner.nextLine().trim();

            if (routeId.equals("0")) {
                System.out.println("Ticket purchase cancelled.");
                return;
            }

            if (!Validation.validateRouteID(routeId)) {
                System.out.println("Invalid Route ID. Please try again.");
                continue;
            }

            route = findRouteById(routeId);
            if (route == null) {
                System.out.println("Route not found. Please try again.");
                continue;
            }

            System.out.println("\nSelected Route:");
            System.out.println("  Route ID    : " + route.getRouteId());
            System.out.println("  Source      : " + route.getSource().getName());
            System.out.println("  Destination : " + route.getDestination().getName());
            System.out.printf("  Distance    : %.2f km%n", route.getDistanceKm());
            break;
        }

        TicketType type;
        while (true) {
            System.out.println("\n===== SELECT TICKET TYPE =====");
            System.out.println("  1.  Single Ticket");
            System.out.println("  2.  Daily Pass");
            System.out.println("  3.  Monthly Pass");
            System.out.println("  4.  Cancel Purchase");
            System.out.print("\n  Enter your choice: ");
            String choiceInput = scanner.nextLine().trim();

            if (!Validation.validateChoice(choiceInput, 1, 4)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int choice = Integer.parseInt(choiceInput);
            if (choice == 4) {
                System.out.println("Ticket purchase cancelled.");
                return;
            }

            type = (choice == 1) ? TicketType.SINGLE
                    : (choice == 2) ? TicketType.DAILY : TicketType.MONTHLY;
            break;
        }

        double fare = ticketService.calculateFare(route, type);
        System.out.printf("\n  Calculated Fare: RM %.2f%n", fare);

        Payment payment;
        while (true) {
            printSectionHeader("SELECT PAYMENT METHOD");
            System.out.println("  1.  Account Balance");
            System.out.println("  2.  Cash Payment");
            System.out.println("  3.  Card Payment");
            System.out.println("  4.  Cancel Purchase");
            System.out.print("\n  Enter your choice: ");
            String payChoice = scanner.nextLine().trim();

            if (!Validation.validateChoice(payChoice, 1, 4)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int paymentChoice = Integer.parseInt(payChoice);
            if (paymentChoice == 4) {
                System.out.println("Ticket purchase cancelled.");
                return;
            }

            if (paymentChoice == 1) payment = new BalancePayment(passenger);
            else if (paymentChoice == 2) payment = new CashPayment(scanner);
            else payment = new CardPayment(scanner);
            break;
        }

        boolean paymentSuccess = paymentService.processPayment(payment, fare);

        if (!paymentSuccess) {
            System.out.println("Ticket purchase cancelled.");
            return;
        }

        String ticketId = ticketService.generateTicketId();
        Ticket ticket = ticketService.buyTicket(ticketId, passenger, route, type);

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║             ✓ TICKET PURCHASE SUCCESSFUL                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        ticket.printTicket();
    }

// Displays and processes the available operations for the logged-in admin.
    public void adminMenu(Admin currentAdmin) {
        boolean adminRunning = true;

        while (adminRunning) {
            printAdminMenu();
            String choiceInput = scanner.nextLine().trim();

            if (!Validation.validateChoice(choiceInput, 1, 14)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int choice = Integer.parseInt(choiceInput);
            switch (choice) {
                case 1:
                    addStation();
                    break;
                case 2:
                    stationService.viewStations();
                    break;
                case 3:
                    searchStation();
                    break;
                case 4:
                    stationService.sortStationsByName();
                    stationService.viewStations();
                    break;
                case 5:
                    addTrain();
                    break;
                case 6:
                    trainService.viewTrains();
                    break;
                case 7:
                    addRoute();
                    break;
                case 8:
                    routeService.viewRoutes();
                    break;
                case 9:
                    printSectionHeader("ALL USERS");
                    userService.displayAllUsers();
                    break;
                case 10:
                    updateUser();
                    break;
                case 11:
                    deleteUser(currentAdmin);
                    break;
                case 12:
                    reportService.generateReport();
                    break;
                case 13:
                    try {
                        java.nio.file.Path saved = reportService.exportReport();
                        System.out.println("Report saved to: " + saved.toAbsolutePath());
                    } catch (FileProcessingException e) {
                        System.out.println("Export failed: " + e.getMessage());
                    }
                    break;
                case 14:
                    adminRunning = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

// Collects station information and adds a new station to the station service.
    private void addStation() {
        printSectionHeader("ADD METRO STATION");

        String stationId;
        while (true) {
            System.out.print("Enter station ID (or 0 to cancel): ");
            stationId = scanner.nextLine().trim();
            if (stationId.equals("0")) {
                System.out.println("Add station cancelled.");
                return;
            }
            if (Validation.validateStationID(stationId)) break;
            System.out.println("Invalid station ID. Please try again.");
        }

        String stationName;
        while (true) {
            System.out.print("Enter station name (or 0 to cancel): ");
            stationName = scanner.nextLine().trim();
            if (stationName.equals("0")) {
                System.out.println("Add station cancelled.");
                return;
            }
            if (!Validation.validateName(stationName)) {
                System.out.println("Invalid station name. Please try again.");
                continue;
            }
            if (stationService.searchStation(stationName) != null) {
                System.out.println("Station name already exists. Please try again.");
                continue;
            }
            break;
        }

        String location;
        while (true) {
            System.out.print("Enter station location (or 0 to cancel): ");
            location = scanner.nextLine().trim();
            if (location.equals("0")) {
                System.out.println("Add station cancelled.");
                return;
            }
            if (Validation.validateName(location)) break;
            System.out.println("Invalid station location. Please try again.");
        }

        stationService.addStation(new Station(stationId, stationName, location));
    }

// Searches for a station by name and displays the matching station.
    private void searchStation() {
        printSectionHeader("SEARCH STATION");

        while (true) {
            System.out.print("Enter station name (or 0 to cancel): ");
            String name = scanner.nextLine().trim();
            if (name.equals("0")) {
                System.out.println("Station search cancelled.");
                return;
            }
            if (!Validation.validateName(name)) {
                System.out.println("Invalid station name. Please try again.");
                continue;
            }

            Station station = stationService.searchStation(name);
            if (station == null) {
                System.out.println("Station not found. Please try again.");
            } else {
                System.out.println("Station found:");
                station.displayInfo();
                return;
            }
        }
    }

// Collects train information and adds a new train to the train service.
    private void addTrain() {
        printSectionHeader("ADD METRO TRAIN");

        String trainId;
        while (true) {
            System.out.print("Enter train ID (or 0 to cancel): ");
            trainId = scanner.nextLine().trim();
            if (trainId.equals("0")) {
                System.out.println("Add train cancelled.");
                return;
            }
            if (Validation.validateTrainID(trainId)) break;
            System.out.println("Invalid train ID. Please try again.");
        }

        String trainName;
        while (true) {
            System.out.print("Enter train name (or 0 to cancel): ");
            trainName = scanner.nextLine().trim();
            if (trainName.equals("0")) {
                System.out.println("Add train cancelled.");
                return;
            }
            if (Validation.validateName(trainName)) break;
            System.out.println("Invalid train name. Please try again.");
        }

        int capacity;
        while (true) {
            System.out.print("Enter train capacity (or 0 to cancel): ");
            String capacityInput = scanner.nextLine().trim();
            if (capacityInput.equals("0")) {
                System.out.println("Add train cancelled.");
                return;
            }
            if (!Validation.validateCapacity(capacityInput)) {
                System.out.println("Invalid capacity. Please enter a positive whole number.");
                continue;
            }
            capacity = Integer.parseInt(capacityInput);
            break;
        }

        trainService.addTrain(new Train(trainId, trainName, capacity));
    }

// Collects route information and creates a route between two stations.
    public void addRoute() {
        printSectionHeader("CREATE METRO ROUTE");
        System.out.println("\n===== ADD ROUTE =====");

        String routeId;
        while (true) {
            System.out.print("Enter route ID (or 0 to cancel): ");
            routeId = scanner.nextLine().trim();
            if (routeId.equals("0")) {
                System.out.println("Add route cancelled.");
                return;
            }
            if (Validation.validateRouteID(routeId)) break;
            System.out.println("Invalid route ID. Please try again.");
        }

        Station source;
        while (true) {
            System.out.print("Enter source station name (or 0 to cancel): ");
            String sourceName = scanner.nextLine().trim();
            if (sourceName.equals("0")) {
                System.out.println("Add route cancelled.");
                return;
            }
            if (!Validation.validateName(sourceName)) {
                System.out.println("Invalid source station name. Please try again.");
                continue;
            }
            source = stationService.searchStation(sourceName);
            if (source == null) {
                System.out.println("Source station not found. Please try again.");
                continue;
            }
            break;
        }

        Station destination;
        while (true) {
            System.out.print("Enter destination station name (or 0 to cancel): ");
            String destinationName = scanner.nextLine().trim();
            if (destinationName.equals("0")) {
                System.out.println("Add route cancelled.");
                return;
            }
            if (!Validation.validateName(destinationName)) {
                System.out.println("Invalid destination station name. Please try again.");
                continue;
            }
            destination = stationService.searchStation(destinationName);
            if (destination == null) {
                System.out.println("Destination station not found. Please try again.");
                continue;
            }
            if (destination.getStationId().equalsIgnoreCase(source.getStationId())) {
                System.out.println("Source and destination cannot be the same. Please try again.");
                continue;
            }
            break;
        }

        double distance;
        while (true) {
            System.out.print("Enter distance (km) (or 0 to cancel): ");
            String distanceInput = scanner.nextLine().trim();
            if (distanceInput.equals("0")) {
                System.out.println("Add route cancelled.");
                return;
            }
            if (!Validation.validatePositiveNumber(distanceInput)) {
                System.out.println("Invalid distance. Please enter a value greater than 0 km.");
                continue;
            }
            distance = Double.parseDouble(distanceInput);
            break;
        }

        routeService.addRoute(new Route(routeId, source, destination, distance));
    }

// Updates the details of an existing user while preserving email uniqueness.
    private void updateUser() {
        printSectionHeader("UPDATE USER");
        System.out.print("Enter current user email (or 0 to cancel): ");
        String email = scanner.nextLine().trim();
        if (email.equals("0")) {
            System.out.println("Update cancelled.");
            return;
        }

        User user = userService.findUserByEmail(email);
        if (user == null) return;

        String name = readUserName();
        if (name == null) return;

        String newEmail;
        while (true) {
            System.out.print("Enter new email (or 0 to cancel): ");
            newEmail = scanner.nextLine().trim();
            if (newEmail.equals("0")) {
                System.out.println("Update cancelled.");
                return;
            }
            if (!Validation.validateEmail(newEmail)) {
                System.out.println("Invalid email. Please try again.");
                continue;
            }
            if (!newEmail.equalsIgnoreCase(email) && userService.emailExists(newEmail)) {
                System.out.println("Email already exists. Please try again.");
                continue;
            }
            break;
        }

        String password;
        while (true) {
            System.out.print("Enter new password (or 0 to cancel): ");
            password = scanner.nextLine();
            if (password.equals("0")) {
                System.out.println("Update cancelled.");
                return;
            }
            if (Validation.validatePassword(password)) break;
            System.out.println("Invalid password. Please try again.");
        }

        userService.updateUser(email, name, newEmail, password);
    }

// Reads and validates a user name from the console.
    private String readUserName() {
        while (true) {
            System.out.print("Enter new name (or 0 to cancel): ");
            String name = scanner.nextLine().trim();
            if (name.equals("0")) {
                System.out.println("Update cancelled.");
                return null;
            }
            if (Validation.validateName(name)) return name;
            System.out.println("Invalid name. Please try again.");
        }
    }

// Removes a user from the user collection.
    private void deleteUser(Admin currentAdmin) {
        printSectionHeader("DELETE USER");
        System.out.print("Enter user email (or 0 to cancel): ");
        String email = scanner.nextLine().trim();

        if (email.equals("0")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (email.equalsIgnoreCase(currentAdmin.getEmail())) {
            System.out.println("You cannot delete the currently logged-in admin account.");
            return;
        }

        User user = userService.findUserByEmail(email);
        if (user == null) return;

        if (user instanceof Passenger && ticketService.hasTickets((Passenger) user)) {
            System.out.println("Cannot delete a passenger who has ticket records.");
            return;
        }

        System.out.print("Confirm delete user? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            userService.deleteUser(email);
        } else {
            System.out.println("Delete cancelled.");
        }
    }
}
