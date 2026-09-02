package payment;

import java.util.Scanner;

public class CardPayment implements Payment {

    private String cardNumber;
    private boolean cancelled;

    public CardPayment(Scanner input) {

        cancelled = false;

        System.out.println("Enter card number (e.g. 1234-5678-1234-5678, 0 to exit): ");

        do {

            this.cardNumber = input.next();

            if (this.cardNumber.equals("0")) {
                cancelled = true;
                return;
            }

        } while (!validateCardNum(this.cardNumber) || !luhnAlgorithm());
    }

    public boolean pay(double amount) {

        if (cancelled) {
            System.out.println("Payment cancelled.");
            return false;
        }

        System.out.printf("Processing card payment of RM %.2f.%n", amount);
        System.out.println("Card number: " + maskCardNumber(cardNumber));

        return true;
    }

    private String maskCardNumber(String cardNumber) {

        return "****-****-****-" +
               cardNumber.substring(cardNumber.length() - 4);
    }

    private boolean validateCardNum(String cardNumber) {

        if (cardNumber.length() != 19) {

            System.out.println("Invalid card number. Enter again.\n");
            return false;
        }

        if (!cardNumber.substring(4, 5).equals("-") ||
            !cardNumber.substring(9, 10).equals("-") ||
            !cardNumber.substring(14, 15).equals("-")) {

            System.out.println(
                "Invalid format. Please follow specified format. " +
                "(e.g. 1234-5678-1234-5678)\n"
            );

            return false;
        }

        String number = cardNumber.replaceAll("-", "");

        if (!number.matches("\\d+")) {

            System.out.println(
                "Card number should contain only digits. Enter again.\n"
            );

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

        if (sum % 10 == 0) {

            System.out.println("Card number is valid.");
            return true;

        } else {

            System.out.println("Card number invalid.");
            return false;
        }
    }
}