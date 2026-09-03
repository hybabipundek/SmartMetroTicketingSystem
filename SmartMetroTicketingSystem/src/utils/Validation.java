package utils;

public class Validation {

    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        if (name.trim().length() < 2) return false;
        return name.matches("[a-zA-Z ]+");
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(gmail\\.com|metro\\.com)$");
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) return false;
        return password.length() >= 6;
    }

    public static boolean validateNumber(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            double number = Double.parseDouble(input);
            return Double.isFinite(number) && number >= 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public static boolean validateCapacity(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            int number = Integer.parseInt(input);
            return number > 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public static boolean validatePositiveNumber(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            double number = Double.parseDouble(input);
            return Double.isFinite(number) && number > 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public static boolean validatePassengerID(String id) {
        return id != null && id.matches("P\\d{3}");
    }

    public static boolean validateAdminID(String id) {
        return id != null && id.matches("A\\d{3}");
    }

    public static boolean validateStationID(String id) {
        return id != null && id.matches("S\\d{3}");
    }

    public static boolean validateTrainID(String id) {
        return id != null && id.matches("T\\d{3}");
    }

    public static boolean validateRouteID(String id) {
        return id != null && id.matches("R\\d{3}");
    }

    public static boolean validateTicketID(String id) {
        return id != null && id.matches("TK\\d{3}");
    }

    public static boolean validateChoice(String input, int min, int max) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            int choice = Integer.parseInt(input);
            return choice >= min && choice <= max;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
