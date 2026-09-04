package payment;

import java.util.Scanner;

/**
 * Payment implementation that processes a ticket payment using cash input.
 */
public class CashPayment implements Payment {

    private final Scanner input;

// Initializes the CashPayment object.
    public CashPayment(Scanner input) {
        this.input = input;
    }

    @Override
// Processes the payment according to the concrete payment method.
    public boolean pay(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        while (true) {
            System.out.printf("Total payment is RM %.2f.%n", amount);
            System.out.print("Enter amount paid (0 to exit): ");
            String inputValue = input.nextLine().trim();

            if (inputValue.equals("0")) {
                System.out.println("Payment cancelled.\n");
                return false;
            }

            double paid;
            try {
                paid = Double.parseDouble(inputValue);
            } catch (NumberFormatException exception) {
                System.out.println("Invalid amount. Please enter a valid number.\n");
                continue;
            }

            if (!Double.isFinite(paid) || paid < 0) {
                System.out.println("Amount cannot be negative or invalid. Please try again.\n");
                continue;
            }

            double change = paid - amount;
            if (change < 0) {
                System.out.println("Insufficient amount.\n");
                continue;
            }

            System.out.printf("Change is RM %.2f.%n", change);
            System.out.println("Thank you.");
            return true;
        }
    }
}
