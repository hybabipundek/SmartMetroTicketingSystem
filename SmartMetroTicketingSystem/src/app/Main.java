package app;

import java.util.Scanner;

import model.Route;
import model.Station;
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
				System.out.println("Welcome Admin");
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
		System.out.println("\n===== BUY TICKET =====");
		
		boolean sourceNm = true;
		while(sourceNm)
		{
			System.out.println("Enter source station name : ");
			String sourceName = scanner.nextLine();
			
			Station source = stationService.searchStation(sourceName);
			
			if(source == null)
			{
				System.out.println("Source station not found. Please try again.");
			}
			else
			{
				sourceNm = false;
			}
		}
		
		
		
		
	}
}


