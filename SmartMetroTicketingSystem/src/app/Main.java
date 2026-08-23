package app;

import java.util.Scanner; 

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
				break;
				
			case 2:
				//register
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
}


