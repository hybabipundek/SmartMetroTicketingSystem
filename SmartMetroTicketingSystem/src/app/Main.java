package app;

import java.util.Scanner;

import payment.CardPayment;
import payment.CashPayment;
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
import repository.TXTFileManager;
import exception.FileProcessingException;

public class Main 
{
	private Scanner scanner = new Scanner(System.in);
	
	private UserService userService = new UserService();
	private StationService stationService = new StationService();
	private TrainService trainService = new TrainService();
	private RouteService routeService = new RouteService();
	private TicketService ticketService = new TicketService();
	private PaymentService paymentService = new PaymentService();
	
	public static void main(String[] args) 
	{
		Main system = new Main();
		system.start();
	}
	
	public void start()
	{
		System.out.println("=================================");
		System.out.println(" SMART METRO TICKETING SYSTEM");
		System.out.println("=================================");
		
		boolean running = true;
		
		while(running)
		{
			System.out.println("\n1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			System.out.println("Enter your choice : ");
			
			int choice = scanner.nextInt(); //save what user wrote for the choice
			scanner.nextLine(); //Clear the enter
			
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
				running = false;
				System.out.println("Thank you for using Smart Metro Ticketing System.");
				break;
				
			default:
				System.out.println("Invalid choice.");
			}
		}
	}
	
	public void login()
	{
		System.out.println("Please enter your email : ");
		String email = scanner.nextLine();
		
		System.out.println("Enter password : ");
		String password = scanner.nextLine();
		
		User user = userService.login(email,password);
		
		if(user != null)  //when user not equal to null
		{
			if(user instanceof Passenger)
			{
				System.out.println("Welcome Passenger");
				Passenger passenger = (Passenger)user; //to confirm the user is a passenger
				//Passenger menu 
				passengerMenu(passenger);
			}
			else if(user instanceof Admin)
			{
				Admin admin = (Admin)user;
				System.out.println("Welcome Admin");
				System.out.println("Admin ID: " + admin.getUserId());
				//Admin menu 
				adminMenu();
			}
		}
	}
	
	public void register()
	{
		System.out.println("Enter name : \n");
		String name = scanner.nextLine();
		
		System.out.println("Enter email : \n");
		String email = scanner.nextLine();
		
		System.out.println("Enter password : \n");
		String password = scanner.nextLine();
		
		System.out.println("Enter initial balance : \n");
		double balance = scanner.nextDouble();
		scanner.nextLine();
		
		userService.registerPassenger(name,email,password,balance);
	}
	
	public void passengerMenu(Passenger passenger)
	{
		boolean passengerRunning = true;
		
		while(passengerRunning)
		{
			//passenger menu
			System.out.println("\n===== PASSENGER MENU =====");
			System.out.println("1. View Station");
			System.out.println("2. View Route");
			System.out.println("3. Buy Ticket");
			System.out.println("4. View Ticket");
			System.out.println("5. Cancel Ticket");
			System.out.println("6. Logout");
			
			System.out.println("Enter your choice : ");
			
			int choice = scanner.nextInt();
			scanner.nextLine();
			
			switch(choice)
			{
			case 1:
				//view station
				stationService.viewStations();
				break;
				
			case 2:
				//view route
				routeService.viewRoutes();
				break;
				
			case 3:
				//buy ticket
				buyticket(passenger);
				break;
				
			case 4:
				//view ticket
				ticketService.viewTickets(passenger);
				break;
				
			case 5:
				//cancel ticket
				System.out.println("Enter ticket ID : ");
				String ticketID = scanner.nextLine();
				
				ticketService.cancelTicket(ticketID);
				
				break;
				
			case 6:
				//logout
				passengerRunning = false;
				System.out.println("Logged out succesfully.");
				break;
				
			default:
				System.out.println("Invalid choice.");
			}
		}
	}
	
	public void buyticket(Passenger passenger)
	{
		//buy ticket
		System.out.println("\n===== BUY TICKET =====");
		
		boolean routeFound = true;
		Route route = null;
		
		while(routeFound)  //return back to let user enter again the source and destination
		{
			boolean sourceNm = true;  //boolean means true and false
			Station source = null;  //Station is a class, class can be a data type
			
			while(sourceNm)
			{
				System.out.println("Enter source station name : ");
				String sourceName = scanner.nextLine();
				
				source = stationService.searchStation(sourceName);
				
				if(source == null)
				{
					System.out.println("Source station not found. Please try again.");
				}
				else
				{
					sourceNm = false;
				}
			}
			
			boolean destinationNm = true;
			Station destination = null;
			
			while(destinationNm)
			{
				System.out.println("Enter destination station name : ");
				String destinationName = scanner.nextLine();
				
				destination = stationService.searchStation(destinationName);
				
				if(destination == null)
				{
					System.out.println("Destination station not found. Please try again.");
				}
				else
				{
					destinationNm = false;
				}
			}
			
			route = routeService.findRoute(source, destination);
			
			if(route == null)
			{
				System.out.println("Route not found.");
			}
			else
			{
				System.out.println("Route found successfully.");
				routeFound = false;
			}
		}
		
		boolean validTicketType = false;
		TicketType type = null;
		
		while(!validTicketType)  //if validTicketType is true , then here means !true = false , so while loop is a false then will break out
		{
			//ticket type
			System.out.println("\n===== SELECT TICKET TYPE =====");
			System.out.println("1. Single");
			System.out.println("2. Daily");
			System.out.println("3. Monthly");
			
			System.out.println("Enter your choice : ");
			
			int choice = scanner.nextInt();
			scanner.nextLine();
			
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
					
				default:
					System.out.println("Invalid ticket type. Please try again.");
			}
		}
		
		//calculate fare
		double fare = ticketService.calculateFare(route, type);
		System.out.println("\nTicket Fare: RM" + fare);
		
		
		Payment payment = null;
		boolean paymentX = true;
		
		while(paymentX)
		{
			//payment
			System.out.println("\n===== PAYMENT METHOD =====");
			System.out.println("1. Cash");
			System.out.println("2. Card");
			System.out.println("3. Cancel");

			System.out.print("Enter your choice: ");
			
			int paymentChoice = scanner.nextInt();
			scanner.nextLine();
			
			switch(paymentChoice)
			{
				case 1:
					payment = new CashPayment();
					paymentX = false;
					break;
					
				case 2:
					payment = new CardPayment("");
					paymentX = false;
					break;
					
				case 3:
					System.out.println("Ticket purchase cancelled.");
					return; //return back to passengerMenu() , it won't continue paymentSuccess that part.
					
				default:
					System.out.println("Invalid payment method.");
					paymentX = true;
					break;
			}
		}
		
		boolean paymentSuccess = paymentService.processPayment(payment, fare);
		
		if(paymentSuccess)
		{
			String ticketId = ticketService.generateTicketId();
			
			Ticket ticket = ticketService.buyTicket(ticketId, passenger, route, type);
			/*ticket return 
			ticketId = T001
			passenger = Kai
			source = KLCC
			destination = KL Sentral
			type = SINGLE
			fare = RM3.50
			*/
				
			System.out.println("\nTicket purchased successfully");
				
			ticket.printTicket();	
		}
		
		else
		{
			System.out.println("Ticket purchase cancelled.");
		}
	}
	
	public void adminMenu()
	{
		boolean adminRunning = true;
		
		while(adminRunning)
		{
			System.out.println("\n===== ADMIN MENU =====");
			
			System.out.println("1. Add Station");
			System.out.println("2. View Stations");
	        System.out.println("3. Add Train");
	        System.out.println("4. View Trains");
	        System.out.println("5. Add Route");
	        System.out.println("6. View Routes");
	        System.out.println("7. Logout");
	        
	        System.out.println("Enter your choice : ");
	        
	        int choice = scanner.nextInt();
	        scanner.nextLine();
	        
	        switch(choice)
	        {
	        	case 1:
	        		//add station
	        		System.out.println("\n===== ADD STATION =====");
	        		
	        		System.out.println("Enter station ID : ");
	        		String stationId = scanner.nextLine();
	        		
	        		System.out.println("Enter station name : ");
	        		String stationName = scanner.nextLine();
	        		
	        		System.out.println("Enter station location : ");
	        		String location = scanner.nextLine();
	        		
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
	        		System.out.println("\n===== ADD TRAIN =====");
	        		
	        		System.out.println("Enter train Id : ");
	        		String trainID = scanner.nextLine();
	        		
	        		System.out.println("Enter train name : ");
	        		String trainName = scanner.nextLine();
	        		
	        		System.out.println("Enter train capacity : ");
	        		int capacity = scanner.nextInt();
	        		scanner.nextLine();
	        		
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
	
	public void addRoute()
	{
		System.out.println("\n===== ADD ROUTE =====");
		
		System.out.println("Enter route ID : ");
		String routeId = scanner.nextLine();
		
		boolean sourceXX = true;
		Station source = null;
		
		while(sourceXX)
		{
			System.out.println("Enter source station name : ");
			String sourceName = scanner.nextLine();
			
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
		
		boolean destinationXX = true;
		Station destination = null;
		
		while(destinationXX)
		{
			System.out.println("Enter destination station name : ");
			String destinationName = scanner.nextLine();
			
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
		
		System.out.println("Enter distance(km) : ");
		double distance = scanner.nextDouble();
		scanner.nextLine();
		
		//the order of the name with the function is correct then ok. The name is difference no issue.
		Route route = new Route(routeId, source , destination , distance);
		
		routeService.addRoute(route);
	}
	
}
	



