package app;

import java.util.Scanner;

import utils.Validation;

import payment.CardPayment;
import payment.CashPayment;
import payment.BalancePayment;
import payment.Payment;

import model.Route;
import model.Station;
import model.Ticket;
import model.Train;
import enums.TicketType;

import model.User;
import model.Passenger;
import model.Admin;

import service.UserService;
import service.StationService;
import service.TrainService;
import service.RouteService;
import service.TicketService;
import service.PaymentService;
import repository.FileManager;
import repository.TXTFileManager;
import exception.FileProcessingException;

import service.ReportService;

public class Main 
{
	private Scanner scanner = new Scanner(System.in);
	
	private UserService userService = new UserService();
	private StationService stationService = new StationService();
	private TrainService trainService = new TrainService();
	private TicketService ticketService = new TicketService();
	private RouteService routeService = new RouteService();
    private ReportService reportService = new ReportService(ticketService);
	private PaymentService paymentService = new PaymentService();
	private FileManager fileManager = new TXTFileManager(userService, stationService, trainService, routeService, ticketService);
	
	public static void main(String[] args) 
	{
		Main system = new Main();
		system.start();
	}
	
	private void printMainHeader()
	{
		System.out.println();
		System.out.println("╔══════════════════════════════════════════════════════════╗");
		System.out.println("║              SMART METRO TICKETING SYSTEM              ║");
		System.out.println("║                  Welcome to Smart Metro                ║");
		System.out.println("╚══════════════════════════════════════════════════════════╝");
	}

	private void printSectionHeader(String title)
	{
		System.out.println();
		System.out.println("┌──────────────────────────────────────────────────────────┐");
		System.out.printf("│ %-56s │%n", title);
		System.out.println("└──────────────────────────────────────────────────────────┘");
	}

	private void printMainMenu()
	{
		System.out.println();
		System.out.println("┌──────────────────── MAIN MENU ──────────────────────────┐");
		System.out.println("│  1.  Login                                               │");
		System.out.println("│  2.  Register                                            │");
		System.out.println("│  3.  Exit                                                │");
		System.out.println("└──────────────────────────────────────────────────────────┘");
		System.out.print("  Enter your choice: ");
	}

	private void printPassengerMenu(Passenger passenger)
	{
		System.out.println();
		System.out.println("╔══════════════════════════════════════════════════════════╗");
		System.out.println("║                  PASSENGER DASHBOARD                   ║");
		System.out.printf("║  Passenger: %-43s ║%n", passenger.getName());
		System.out.printf("║  Balance:   RM %-40.2f ║%n", passenger.getBalance());
		System.out.println("╚══════════════════════════════════════════════════════════╝");
		System.out.println();
		System.out.println("┌──────────────────── PASSENGER MENU ─────────────────────┐");
		System.out.println("│  1.  View Stations                                       │");
		System.out.println("│  2.  View Routes                                         │");
		System.out.println("│  3.  Buy Ticket                                          │");
		System.out.println("│  4.  View My Tickets                                     │");
		System.out.println("│  5.  Cancel Ticket                                       │");
		System.out.println("│  6.  Top Up Balance                                      │");
		System.out.println("│  7.  Logout                                               │");
		System.out.println("└──────────────────────────────────────────────────────────┘");
		System.out.print("  Enter your choice: ");
	}

	private void printAdminMenu()
	{
		System.out.println();
		System.out.println("╔══════════════════════════════════════════════════════════╗");
		System.out.println("║                    ADMIN DASHBOARD                     ║");
		System.out.println("║              System Management & Reports               ║");
		System.out.println("╚══════════════════════════════════════════════════════════╝");
		System.out.println();
		System.out.println("┌───────────────────── ADMIN MENU ────────────────────────┐");
		System.out.println("│  1.  Add Station                                         │");
		System.out.println("│  2.  View Stations                                       │");
		System.out.println("│  3.  Add Train                                           │");
		System.out.println("│  4.  View Trains                                         │");
		System.out.println("│  5.  Add Route                                           │");
		System.out.println("│  6.  View Routes                                         │");
		System.out.println("│  7.  View Reports                                        │");
		System.out.println("│  8.  Logout                                               │");
		System.out.println("└──────────────────────────────────────────────────────────┘");
		System.out.print("  Enter your choice: ");
	}

	public void start()
	{
		try
		{
			fileManager.loadData();
		}
		catch (FileProcessingException exception)
		{
			System.out.println("Unable to load saved data: " + exception.getMessage());
		}

		printMainHeader();

		String choiceInput = "";
		int choice = 0;
		boolean running = true;
		
		while(running)
		{
			printMainMenu();

			choiceInput = scanner.nextLine(); //Clear the enter
			//save what user wrote for the choice
			
			if(Validation.validateChoice(choiceInput, 1, 3) == false)
			{
				System.out.println("Invalid choice. Please try again.");
				running = true;
			}
			else
			{
				choice = Integer.parseInt(choiceInput);
				
				switch(choice)
				{
				
				case 1:
					//login
					login();
					break;
					
				case 2:
					//register
					register();
					break;
					
				case 3:
					//exit
					try
					{
						fileManager.saveData();
					}
					catch (FileProcessingException exception)
					{
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
			
	}
	
	public void login()
	{
		printSectionHeader("LOGIN");
		String email = "";
		boolean emailXX = true;
		
		while(emailXX)
		{
			System.out.println("Please enter your email (or 0 to cancel): ");
			email = scanner.nextLine();

			if(email.equals("0"))
			{
				System.out.println("Login cancelled.");
				return;
			}
			
			if(Validation.validateEmail(email) == false)
			{
				System.out.println("Invalid email. Please try again.");
				emailXX = true;
			}
			else
			{
				emailXX = false;
			}
		}
		
		String password = "";
		boolean passwordXX = true;
		
		while(passwordXX)
		{
			System.out.println("Enter password (or 0 to cancel): ");
			password = scanner.nextLine();

			if(password.equals("0"))
			{
				System.out.println("Login cancelled.");
				return;
			}
			
			if(Validation.validatePassword(password) == false)
			{
				System.out.println("Invalid password. Please try again.");
				passwordXX = true;
			}
			else
			{
				passwordXX = false;
			}
		}
		
		User user = userService.login(email,password);
		
		if(user != null)  //when user not equal to null
		{
			if(user instanceof Passenger)
			{
				System.out.println();
				System.out.println("✓ Login successful.");
				System.out.println("Welcome, " + ((Passenger)user).getName() + "!");
				Passenger passenger = (Passenger)user; //to confirm the user is a passenger
				//Passenger menu 
				passengerMenu(passenger);
			}
			else if(user instanceof Admin)
			{
				Admin admin = (Admin)user;
				System.out.println();
				System.out.println("✓ Login successful.");
				System.out.println("Welcome, Admin!");
				System.out.println("Admin ID: " + admin.getUserId());
				//Admin menu 
				adminMenu();
			}
		}
	}
	
	public void register()
	{
		printSectionHeader("CREATE PASSENGER ACCOUNT");
		String name = "";
		boolean nameCorrect = true;
		
		while(nameCorrect)
		{
			System.out.println("Enter name (or 0 to cancel): ");
			name = scanner.nextLine();

			if(name.equals("0"))
			{
				System.out.println("Registration cancelled.");
				return;
			}
			
			if(Validation.validateName(name) == false)
			{
				System.out.println("Invalid name. Please try again.");
				nameCorrect = true;
			}
			else
			{
				nameCorrect = false;
			}
		}
		
		String email = "";
		boolean emailCorrect = true;
		
		while(emailCorrect)
		{
			System.out.println("Enter email (or 0 to cancel): ");
			email = scanner.nextLine();

			if(email.equals("0"))
			{
				System.out.println("Registration cancelled.");
				return;
			}
			
			if(Validation.validateEmail(email) == false)
			{
				System.out.println("Invalid email. Please try again.");
				emailCorrect = true;
			}
			else
			{
				emailCorrect = false;
			}
		}
		
		String password = "";
		boolean passwordCorrect = true;
		
		while(passwordCorrect)
		{
			System.out.println("Enter password (or 0 to cancel): \n");
			password = scanner.nextLine();

			if(password.equals("0"))
			{
				System.out.println("Registration cancelled.");
				return;
			}
			
			if(Validation.validatePassword(password) == false)
			{
				System.out.println("Invalid password. Please try again.");
				passwordCorrect = true;
			}
			else
			{
				passwordCorrect = false;
			}
		}
		
		String balanceInput = "";  //double change to String
		double balance = 0;
		boolean balanceCorrect = true;
		
		while(balanceCorrect)
		{
			System.out.println("Enter initial balance (or 0 to cancel): \n");
			balanceInput = scanner.nextLine();

			if(balanceInput.equals("0"))
			{
				System.out.println("Registration cancelled.");
				return;
			}
			
			if(Validation.validateNumber(balanceInput) == false)
			{
				System.out.println("Invalid input. Please try again.");
				balanceCorrect = true;
			}
			else
			{	//Convert String balanceInput into double
				balance = Double.parseDouble(balanceInput);
				balanceCorrect = false;
			}
		}
		
		userService.registerPassenger(name,email,password,balance);
	}
	
	public void passengerMenu(Passenger passenger)
	{
		String choiceInput = "";
		int choice = 0;
		boolean passengerRunning = true;
		
		while(passengerRunning)
		{
			printPassengerMenu(passenger);

			choiceInput = scanner.nextLine();
			
			if(Validation.validateChoice(choiceInput, 1, 7) == false)
			{
				System.out.println("Invalid choice. Please try again.");
			}
			else
			{
				choice = Integer.parseInt(choiceInput);
				switch(choice)
				{
				case 1:
					printSectionHeader("METRO STATIONS");
					stationService.viewStations();
					break;
					
				case 2:
					printSectionHeader("METRO ROUTES");
					routeService.viewRoutes();
					break;
					
				case 3:
					//buy ticket
					buyticket(passenger);
					break;
					
				case 4:
					printSectionHeader("MY TICKETS");
					ticketService.viewTickets(passenger);
					break;
					
				case 5:
					printSectionHeader("CANCEL TICKET");
					String ticketID = "";
					boolean tID = true;
					
					while(tID)
					{
						System.out.println("Enter ticket ID (or 0 to cancel): ");
						ticketID = scanner.nextLine();
			
			if(ticketID.equals("0"))
			{
				System.out.println("Ticket cancellation cancelled.");
				break;
			}
						
						if(Validation.validateTicketID(ticketID) == false)
						{
							System.out.println("Invalid ticket ID. Please try again.");
							tID = true;
						}
						else
						{
							tID = false;
						}
					}
					
					if(ticketID != null)
					{
						ticketService.cancelTicket(ticketID);
					}
					
					break;
					
				case 6:
					topUpBalance(passenger);
					break;

				case 7:
					//logout
					passengerRunning = false;
					System.out.println("Logged out successfully.");
					break;

								default:
					System.out.println("Invalid choice.");
				}
			}
		}
	}
	
	private void topUpBalance(Passenger passenger)
	{
		printSectionHeader("TOP UP BALANCE");

		while(true)
		{
			System.out.print("Enter top up amount (or 0 to cancel): ");
			String amountInput = scanner.nextLine();

			if(amountInput.equals("0"))
			{
				System.out.println("Top up cancelled.");
				return;
			}

			if(!Validation.validateNumber(amountInput))
			{
				System.out.println("Invalid amount. Please try again.");
				continue;
			}

			double amount = Double.parseDouble(amountInput);

			if(amount <= 0)
			{
				System.out.println("Top up amount must be greater than RM 0.");
				continue;
			}

			userService.topUpBalance(passenger, amount);
			return;
		}
	}

	private Route findRouteById(String routeId)
	{
		for(Route route : routeService.getAllRoutes())
		{
			if(route.getRouteId().equalsIgnoreCase(routeId))
			{
				return route;
			}
		}
		return null;
	}

	public void buyticket(Passenger passenger)
	{
		printSectionHeader("BUY METRO TICKET");
		System.out.println("\n===== BUY TICKET =====");

		Route route = null;
		boolean routeSelected = false;

		while(!routeSelected)
		{
			System.out.println("Available routes:");
			System.out.println();
			routeService.viewRoutes();
			System.out.println();
			System.out.print("Enter Route ID (or 0 to cancel): ");
			String routeId = scanner.nextLine();

			if(routeId.equals("0"))
			{
				System.out.println("Ticket purchase cancelled.");
				return;
			}

			if(!Validation.validateRouteID(routeId))
			{
				System.out.println("Invalid Route ID. Please try again.");
				continue;
			}

			route = findRouteById(routeId);

			if(route == null)
			{
				System.out.println("Route not found. Please try again.");
			}
			else
			{
				routeSelected = true;
				System.out.println();
				System.out.println("Selected Route:");
				System.out.println("  Route ID    : " + route.getRouteId());
				System.out.println("  Source      : " + route.getSource().getName());
				System.out.println("  Destination : " + route.getDestination().getName());
				System.out.printf("  Distance    : %.2f km%n", route.getDistanceKm());
			}
		}

		String choiceInput = "";
		int choice = 0;
		boolean validTicketType = false;
		TicketType type = null;

		while(!validTicketType)
		{
			System.out.println("\n===== SELECT TICKET TYPE =====");
			System.out.println("  1.  Single Ticket");
			System.out.println("  2.  Daily Pass");
			System.out.println("  3.  Monthly Pass");
			System.out.println("  4.  Cancel Purchase");
			System.out.println();
			System.out.print("  Enter your choice: ");

			choiceInput = scanner.nextLine();

			if(Validation.validateChoice(choiceInput, 1, 4) == false)
			{
				System.out.println("Invalid choice. Please try again.");
			}
			else
			{
				choice = Integer.parseInt(choiceInput);
				switch(choice)
				{
					case 1:
						type = TicketType.SINGLE;
						validTicketType = true;
						break;

					case 2:
						type = TicketType.DAILY;
						validTicketType = true;
						break;

					case 3:
						type = TicketType.MONTHLY;
						validTicketType = true;
						break;

					case 4:
						System.out.println("Ticket purchase cancelled.");
						return;
				}
			}
		}

		double fare = ticketService.calculateFare(route, type);
		System.out.printf("%n  Calculated Fare: RM %.2f%n", fare);

		String payChoice = "";
		int paymentChoice = 0;
		Payment payment = null;
		boolean paymentX = true;

		while(paymentX)
		{
			printSectionHeader("SELECT PAYMENT METHOD");
			System.out.println("  1.  Account Balance");
		System.out.println("  2.  Cash Payment");
		System.out.println("  3.  Card Payment");
		System.out.println("  4.  Cancel Purchase");
		System.out.println();
			System.out.print("  Enter your choice: ");

			payChoice = scanner.nextLine();

			if(Validation.validateChoice(payChoice, 1, 4) == false)
			{
				System.out.println("Invalid choice. Please try again.");
			}
			else
			{
				paymentChoice = Integer.parseInt(payChoice);
				switch(paymentChoice)
				{
					case 1:
						payment = new BalancePayment(passenger);
						paymentX = false;
						break;

					case 2:
						payment = new CashPayment();
						paymentX = false;
						break;

					case 3:
						payment = new CardPayment(scanner);
						paymentX = false;
						break;

					case 4:
						System.out.println("Ticket purchase cancelled.");
						return;
				}
			}
		}

		boolean paymentSuccess = paymentService.processPayment(payment, fare);

		if(paymentSuccess)
		{
			String ticketId = ticketService.generateTicketId();
			Ticket ticket = ticketService.buyTicket(ticketId, passenger, route, type);

			System.out.println();
			System.out.println("╔══════════════════════════════════════════════════════════╗");
			System.out.println("║             ✓ TICKET PURCHASE SUCCESSFUL              ║");
			System.out.println("╚══════════════════════════════════════════════════════════╝");

			ticket.printTicket();
		}
		else
		{
			System.out.println("Ticket purchase cancelled.");
		}
	}

	public void adminMenu()
	{
		String choiceXX = "";
		int choice = 0;
		boolean adminRunning = true;
		
		while(adminRunning)
		{
			printAdminMenu();

	        choiceXX = scanner.nextLine();
	        
	        if(Validation.validateChoice(choiceXX, 1, 8) == false)
	        {
	        	System.out.println("Invalid choice. Please try again.");
	        }
	        else
	        {
	        	choice = Integer.parseInt(choiceXX);
	        	switch(choice)
		        {
		        	case 1:
		        		//add station
		        		printSectionHeader("ADD METRO STATION");
		        		
		        		String stationId = "";
		        		boolean sID = true;
		        		
		        		while(sID)
		        		{
		        			System.out.println("Enter station ID (or 0 to cancel): ");
			        		stationId = scanner.nextLine();
			
			if(stationId.equals("0"))
			{
				System.out.println("Add station cancelled.");
				return;
			}
			        		
			        		if(Validation.validateStationID(stationId) == false)
			        		{
			        			System.out.println("Invalid station ID. Please try again.");
			        			sID = true;
			        		}
			        		else
			        		{
			        			sID = false;
			        		}
		        		}
		        		
		        		String stationName = "";
		        		boolean sName = true;
		        		
		        		while(sName)
		        		{
		        			System.out.println("Enter station name (or 0 to cancel): ");
			        		stationName = scanner.nextLine();
			
			if(stationName.equals("0"))
			{
				System.out.println("Add station cancelled.");
				return;
			}
			        		
			        		if(Validation.validateName(stationName) == false)
			        		{
			        			System.out.println("Invalid station name. Please try again.");
			        			sName = true;
			        		}
			        		else
			        		{
			        			sName = false;
			        		}
		        		}
		        		
		        		String location = "";
		        		boolean sL = true;
		        		
		        		while(sL)
		        		{
		        			System.out.println("Enter station location (or 0 to cancel): ");
			        		location = scanner.nextLine();
			
			if(location.equals("0"))
			{
				System.out.println("Add station cancelled.");
				return;
			}
			        		
			        		if(Validation.validateName(location) == false)
			        		{
			        			System.out.println("Invalid station location. Please try again.");
			        			sL = true;
			        		}
			        		else
			        		{
			        			sL = false;
			        		}
		        		}
		        		
		        		Station station = new Station(stationId , stationName , location);
		        		/*
		        		 Station ID: S001
						 Station Name: KLCC
						 Location: Kuala Lumpur
		        		 */
		        		stationService.addStation(station);
		        		
		        		break;
		        	
		        	case 2:
		        		//View stations
		        		stationService.viewStations();
		        		break;
		        		
		        	case 3:
		        		//add train
		        		printSectionHeader("ADD METRO TRAIN");
		        		
		        		String trainID = "";
		        		boolean trainIdXX = true;
		        		
		        		while(trainIdXX)
		        		{
		        			System.out.println("Enter train Id (or 0 to cancel): ");
			        		trainID = scanner.nextLine();
			
			if(trainID.equals("0"))
			{
				System.out.println("Add train cancelled.");
				return;
			}
			        		
			        		if(Validation.validateTrainID(trainID) == false)
			        		{
			        			System.out.println("Invalid train Id. Please try again.");
			        			trainIdXX = true;
			        		}
			        		else
			        		{
			        			trainIdXX = false;
			        		}
		        		}
		        		
		        		String trainName = "";
		        		boolean tName = true;
		        		
		        		while(tName)
		        		{
		        			System.out.println("Enter train name (or 0 to cancel): ");
			        		trainName = scanner.nextLine();
			
			if(trainName.equals("0"))
			{
				System.out.println("Add train cancelled.");
				return;
			}
			        		
			        		if(Validation.validateName(trainName) == false)
			        		{
			        			System.out.println("Invalid train name. Please try again.");
			        			tName = true;
			        		}
			        		else
			        		{
			        			tName = false;
			        		}
		        		}
		        		
		        		String capacityXX = "";
		        		int capacity = 0;
		        		boolean tCapacity = true;
		        		
		        		while(tCapacity)
		        		{
		        			System.out.println("Enter train capacity (or 0 to cancel): ");
			        		capacityXX = scanner.nextLine();
			
			if(capacityXX.equals("0"))
			{
				System.out.println("Add train cancelled.");
				return;
			}
			        		
		        			if(Validation.validateCapacity(capacityXX) == false)
		        			{
		        				System.out.println("Invalid capacity enter. Please try again.");
		        				tCapacity = true;
		        			}
		        			else
		        			{	
		        				//String capacityXX convert to integer
		        				capacity = Integer.parseInt(capacityXX);
		        				tCapacity = false;
		        			}
		        		}
		        		
		        		Train train = new Train(trainID , trainName , capacity);
		        		
		        		trainService.addTrain(train);
		        		break;
		        		
		        	case 4:
		        		//view trains
		        		trainService.viewTrains();
		        		break;
		        		
		        	case 5:
		        		//add route
		        		addRoute();
		        		break;
		        		
		        	case 6:
		        		//view routes
		        		routeService.viewRoutes();
		        		break;
		        		
		        	case 7:
		        	    reportService.generateReport();
		        	    break;
		        	    
		        	case 8:
		        	    adminRunning = false;
		        	    System.out.println("Logged out successfully.");
		        	    break;
		        	    
		        	default:
		        		System.out.println("Invalid choice.");
		        		adminRunning = true;
		        		break;
		        }
	        } 				
		}
	}
	
	public void addRoute()
	{
		printSectionHeader("CREATE METRO ROUTE");
		System.out.println("\n===== ADD ROUTE =====");
		
		String routeId = "";
		boolean routeIdXX = true;
		
		while(routeIdXX)
		{
			System.out.println("Enter route ID (or 0 to cancel): ");
			routeId = scanner.nextLine();
			
			if(routeId.equals("0"))
			{
				System.out.println("Add route cancelled.");
				return;
			}
			
			if(Validation.validateRouteID(routeId) == false)
			{
				System.out.println("Invalid route ID. Please try again.");
				routeIdXX = true;
			}
			else
			{
				routeIdXX = false;
			}
		}
		
		String sourceName = "";
		boolean sourceXX = true;
		Station source = null;
		
		while(sourceXX)
		{
			System.out.println("Enter source station name (or 0 to cancel): ");
			sourceName = scanner.nextLine();

			if(sourceName.equals("0"))
			{
				System.out.println("Add route cancelled.");
				return;
			}
			
			if(Validation.validateName(sourceName) == false)
			{
				System.out.println("Invalid source station name. Please try again.");
				sourceXX = true;
			}
			else
			{
				//user enter sourceName , the system then search for the source station
				source = stationService.searchStation(sourceName);
				
				if(source == null)
				{
					System.out.println("Source station not found.");
					sourceXX = true;
				}
				else
				{
					sourceXX = false;
				}
			}
		}
		
		String destinationName = "";
		boolean destinationXX = true;
		Station destination = null;
		
		while(destinationXX)
		{
			System.out.println("Enter destination station name (or 0 to cancel): ");
			destinationName = scanner.nextLine();

			if(destinationName.equals("0"))
			{
				System.out.println("Add route cancelled.");
				return;
			}
			
			if(Validation.validateName(destinationName) == false)
			{
				System.out.println("Invalid destination station name. Please try again.");
				destinationXX = true;
			}
			else
			{
				//user enter destinationName , the system then search for the destination station
				destination = stationService.searchStation(destinationName);
				
				if(destination == null)
				{
					System.out.println("Destination station not found.");
					destinationXX = true;
				}
				else if(destination.equals(source))
				{
					System.out.println("Source and destination cannot be the same. Please try again.");
				}
				else
				{
					destinationXX = false;
				}
			}
		}
		
		String distanceInput = "";
		double distance = 0;
		boolean distanceCheck = true;
		
		while(distanceCheck)
		{
			System.out.println("Enter distance(km) (or 0 to cancel): ");
			distanceInput = scanner.nextLine();
			
			if(distanceInput.equals("0"))
			{
				System.out.println("Add route cancelled.");
				return;
			}
			
			if(Validation.validateNumber(distanceInput) == false)
			{
				System.out.println("Invalid distance(km) enter. Please try again.");
				distanceCheck = true;
			}
			else
			{
				distance = Double.parseDouble(distanceInput);
				distanceCheck = false;
			}
		}
		
		
		
		//the order of the name with the function is correct then ok. The name is difference no issue.
		Route route = new Route(routeId, source , destination , distance);
		
		routeService.addRoute(route);
	}
}
	



