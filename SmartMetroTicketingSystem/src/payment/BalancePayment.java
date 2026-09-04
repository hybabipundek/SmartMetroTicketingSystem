package payment;

import model.Passenger;

/**
 * Payment implementation that processes a ticket payment using the passenger's stored balance.
 */
public class BalancePayment implements Payment {

    private Passenger passenger;

// Initializes the BalancePayment object.
    public BalancePayment(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
// Processes the payment according to the concrete payment method.
    public boolean pay(double amount) {
        if (passenger == null) {
            System.out.println("Error. Passenger account not found.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Error. Invalid payment amount.");
            return false;
        }

        if (passenger.getBalance() < amount) {
            System.out.printf(
                "Insufficient account balance. Current balance: RM %.2f%n",
                passenger.getBalance()
            );
            return false;
        }

        passenger.deductBalance(amount);

        System.out.printf(
            "Payment of RM %.2f deducted from account balance.%n", amount
        );
        System.out.printf(
            "Remaining balance: RM %.2f%n", passenger.getBalance()
        );

        return true;
    }
}
