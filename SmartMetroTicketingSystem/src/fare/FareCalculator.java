package fare;
import model.Route;
import enums.TicketType;


/**
 * Interface that defines the contract for calculating ticket fares.
 */
public interface FareCalculator {
	public double calculateFare(Route route, TicketType ticketType);
}
