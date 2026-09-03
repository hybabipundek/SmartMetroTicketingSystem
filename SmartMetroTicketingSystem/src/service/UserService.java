package service;

import java.util.HashMap;

import exception.InvalidLoginException;
import model.Admin;
import model.Passenger;
import model.User;
import utils.Validation;

public class UserService {

    private HashMap<String, User> users;
    private int passengerCounter;
    private int adminCounter;

    public UserService() {
        users = new HashMap<>();
        passengerCounter = 1;
        adminCounter = 1;

        Admin admin = new Admin(
                "A001",
                "System Admin",
                "admin@metro.com",
                "admin123"
        );

        users.put("admin@metro.com", admin);
    }

    private boolean validateName(String name) {
        if (!Validation.validateName(name)) {
            System.out.println("Invalid name.");
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (!Validation.validateEmail(email)) {
            System.out.println("Invalid email format. Use a valid Gmail or metro.com address.");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (!Validation.validatePassword(password)) {
            System.out.println("Password must contain at least 6 characters.");
            return false;
        }
        return true;
    }

    private boolean isEmailExist(String email) {
        return users.containsKey(email);
    }

    public boolean emailExists(String email) {
        return email != null && users.containsKey(email);
    }

    private String generatePassengerId() {
        return String.format("P%03d", passengerCounter++);
    }

    private String generateAdminId() {
        return String.format("A%03d", adminCounter++);
    }

    public boolean registerPassenger(String name, String email, String password, double balance) {
        if (!validateName(name)) return false;
        if (!validateEmail(email)) return false;
        if (!validatePassword(password)) return false;

        if (isEmailExist(email)) {
            System.out.println("Email already exists.");
            return false;
        }

        if (!Double.isFinite(balance) || balance < 0) {
            System.out.println("Initial balance cannot be negative or invalid.");
            return false;
        }

        String userId = generatePassengerId();
        Passenger passenger = new Passenger(userId, name, email, password, balance);
        users.put(email, passenger);

        System.out.println("Passenger registered successfully.");
        System.out.println("Passenger ID: " + userId);
        return true;
    }

    public boolean registerAdmin(String name, String email, String password) {
        if (!validateName(name)) return false;
        if (!validateEmail(email)) return false;
        if (!validatePassword(password)) return false;

        if (isEmailExist(email)) {
            System.out.println("Email already exists.");
            return false;
        }

        String userId = generateAdminId();
        Admin admin = new Admin(userId, name, email, password);
        users.put(email, admin);

        System.out.println("Admin registered successfully.");
        System.out.println("Admin ID: " + userId);
        return true;
    }

    public User login(String email, String password) {
        User user = users.get(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginException("Invalid email or password.");
        }

        System.out.println("Login successful.");
        return user;
    }

    public User findUserByEmail(String email) {
        if (!isEmailExist(email)) {
            System.out.println("Email does not exist.");
            return null;
        }
        return users.get(email);
    }

    public boolean updateUser(String email, String newName, String newEmail, String newPassword) {
        User user = findUserByEmail(email);
        if (user == null) return false;

        if (!validateName(newName)) return false;
        if (!validateEmail(newEmail)) return false;
        if (!validatePassword(newPassword)) return false;

        if (!email.equals(newEmail) && isEmailExist(newEmail)) {
            System.out.println("Email already exists.");
            return false;
        }

        users.remove(email);
        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        users.put(newEmail, user);

        System.out.println("User updated successfully.");
        return true;
    }

    public boolean deleteUser(String email) {
        if (!isEmailExist(email)) {
            System.out.println("Email does not exist.");
            return false;
        }

        users.remove(email);
        System.out.println("User deleted successfully.");
        return true;
    }

    public boolean topUpBalance(Passenger passenger, double amount) {
        if (passenger == null) {
            System.out.println("Passenger account not found.");
            return false;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Top up amount must be greater than RM 0.");
            return false;
        }

        passenger.topUp(amount);
        System.out.printf("Top up successful. New balance: RM %.2f%n", passenger.getBalance());
        return true;
    }

    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No user found.");
            return;
        }

        for (User user : users.values()) {
            System.out.println(user);
            System.out.println("-------------------------");
        }
    }

    public HashMap<String, User> getAllUsers() {
        return users;
    }

    public void setAllUsers(HashMap<String, User> loadedUsers) {
        users = (loadedUsers == null) ? new HashMap<>() : loadedUsers;
        passengerCounter = nextCounter('P');
        adminCounter = nextCounter('A');
    }

    private int nextCounter(char prefix) {
        int next = 1;
        for (User user : users.values()) {
            String id = user.getUserId();
            if (id != null && id.length() > 1 && Character.toUpperCase(id.charAt(0)) == prefix) {
                try {
                    next = Math.max(next, Integer.parseInt(id.substring(1)) + 1);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return next;
    }
}
