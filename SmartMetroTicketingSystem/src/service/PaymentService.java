package service;

import payment.Payment;

public class PaymentService {

    public boolean processPayment(Payment payment, double amount) {
        if (payment == null) {
            System.out.println("Error. No payment method selected.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Error. Invalid payment amount.");
            return false;
        }

        boolean success = payment.pay(amount);

        if (success) {
            System.out.printf("Payment of RM %.2f processed successfully.%n", amount);
        } else {
            System.out.println("Payment failed. Please try again.");
        }

        return success;
    }
}
