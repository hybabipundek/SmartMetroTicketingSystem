package fare;
import enums.TicketType;

public class StandardFareCalculator implements FareCalculator{
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
		}
		
		double finalFare = distance * fare;
		return finalFare;		
	}
}
