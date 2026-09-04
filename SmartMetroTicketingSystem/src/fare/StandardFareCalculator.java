package fare;
import enums.TicketType;
import model.Route;

/**
 * Standard implementation of FareCalculator that calculates fare using route distance and ticket type.
 */
public class StandardFareCalculator implements FareCalculator{
// Calculates the fare for the specified route and ticket type.
	public double calculateFare(Route route, TicketType ticketType) {
		double distance = route.getDistanceKm();
		double fare;
		
		switch(ticketType) {
		case SINGLE:
			fare = 3.0;
			break;
		case DAILY:
			fare = 10.0;
			break;
		case MONTHLY:
			fare = 60.0;
			break;
		default:
			fare = 0.0;
			break;
		}
		
		double finalFare = distance * fare;
		return finalFare;		
	}
}
