package payment;
import java.util.Scanner;

public class CashPayment implements Payment{
	public boolean pay(double amount) {
		System.out.printf("Total payment is RM %.2f.\n", amount);
		System.out.println("Enter amount paid (0 to exit): ");
		Scanner input = new Scanner(System.in);
		double paid;
		double change;
		boolean success = false;
		do {
			paid = input.nextDouble();
			
			if(paid == 0) {
				System.out.println("Payment cancelled.\n");
				input.close();
				return false;
			}
			
			if(paid < 0) {
				System.out.println("Amount cannot be negative. Please try again.\n");
				System.out.println("Enter amount paid (0 to exit): ");
				continue;
			}
			
			change = paid - amount;
			
			if(change < 0) {
				System.out.println("Insufficient amount.\n");
				System.out.println("Enter amount paid (0 to exit): ");
			}
			else {
				System.out.printf("Change is RM %.2f.\n", change);
				System.out.println("Thank you.");
				success = true;
			}
		}while(!success);
		
		input.close();
		return true;
	}
}
