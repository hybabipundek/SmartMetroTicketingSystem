package fare;
import model.Route;
import enums.TicketType;


public interface FareCalculator {
	public double calculateFare(Route route, TicketType ticketType);
}
