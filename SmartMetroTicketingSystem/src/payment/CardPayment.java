package payment;

import java.util.Scanner;

public class CardPayment implements Payment {

    private String cardNumber;
    private boolean cancelled;

    public CardPayment(Scanner input) {

        cancelled = false;

        while (true) {

            System.out.print(
                "Enter card number (e.g. 1234-5678-1234-5678, 0 to exit): "
            );

            cardNumber = input.nextLine().trim();

            // CANCEL
            if (cardNumber.equals("0")) {
                cancelled = true;
                return;
            }
            
            // EMPTY INPUT            
            if (cardNumber.isEmpty()) {
                System.out.println(
                    "Card number cannot be empty. Please try again.\n"
                );
                continue;
            }
           
            // CARD VALIDATION           
            if (!validateCardNum(cardNumber)) {
                System.out.println(
                    "Invalid card number. Enter again.\n"
                );
                continue;
            }
            
            // LUHN VALIDATION            
            if (!luhnAlgorithm()) {
                System.out.println(
                    "Invalid card number. Enter again.\n"
                );
                continue;
            }

            // Everything valid
            break;
        }
    }

    public boolean pay(double amount) {

        if (cancelled) {
            System.out.println("Payment cancelled.");
            return false;
        }

        System.out.printf(
            "Processing card payment of RM %.2f.%n",
            amount
        );

        System.out.println(
            "Card number: " + maskCardNumber(cardNumber)
        );

        return true;
    }

    private String maskCardNumber(String cardNumber) {

        return "****-****-****-" +
               cardNumber.substring(cardNumber.length() - 4);
    }

    private boolean validateCardNum(String cardNumber) {

        // Check length
        if (cardNumber.length() != 19) {
            return false;
        }

        // Check dash positions
        if (!cardNumber.substring(4, 5).equals("-") ||
            !cardNumber.substring(9, 10).equals("-") ||
            !cardNumber.substring(14, 15).equals("-")) {

            return false;
        }

        // Remove dashes
        String number = cardNumber.replaceAll("-", "");

        // Check digits only
        if (!number.matches("\\d+")) {
            return false;
        }

        return true;
    }

    private boolean luhnAlgorithm() {

        String number = cardNumber.replaceAll("-", "");

        int[] digits = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            digits[i] = Character.getNumericValue(number.charAt(i));
        }

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = digits.length - 1; i >= 0; i--) {

            int currentDigit = digits[i];

            if (doubleDigit) {

                currentDigit *= 2;

                if (currentDigit > 9) {
                    currentDigit -= 9;
                }
            }

            sum += currentDigit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }
}