package service;

import java.util.HashMap;

import model.Admin;
import model.Passenger;
import model.User;

public class UserService {

    private HashMap<String, User> users;
    private int passengerCounter;
    private int adminCounter;

    public UserService() {
        users = new HashMap<>();
        passengerCounter = 1;
        adminCounter = 1;
    }
   
    private boolean validateName(String name) {

    if (name == null || name.trim().isEmpty()) {
        System.out.println("Name cannot be empty.");
        return false;
    }

    return true;
    }

    private boolean validateEmail(String email) {

    if (email == null || email.trim().isEmpty()) {
        System.out.println("Email cannot be empty.");
        return false;
    }

    if (!email.contains("@") || !email.contains(".")) {
        System.out.println("Invalid email format.");
        return false;
    }

    return true;
    }

    private boolean validatePassword(String password) {

    if (password == null || password.trim().isEmpty()) {
        System.out.println("Password cannot be empty.");
        return false;
    }

    if (password.length() < 6) {
        System.out.println("Password must contain at least 6     characters.");
        return false;
    }

    return true;
}

    private boolean isEmailExist(String email) {

    return users.containsKey(email);

    }

    private String generatePassengerId() {
    return String.format("P%03d", passengerCounter++);
    }

    private String generateAdminId() {
    return String.format("A%03d", adminCounter++);
    }
   
    public boolean registerPassenger(String name, String email, String password, double balance) {

    if (!validateName(name)) {
        return false;
    }

    if (!validateEmail(email)) {
        return false;
    }

    if (!validatePassword(password)) {
        return false;
    }

    if (isEmailExist(email)) {
        System.out.println("Email already exists.");
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

    if (!validateName(name)) {
        return false;
    }

    if (!validateEmail(email)) {
        return false;
    }

    if (!validatePassword(password)) {
        return false;
    }

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

    if (!isEmailExist(email)) {
        System.out.println("Email does not exist.");
        return null;
    }

    User user = users.get(email);

    if (!user.getPassword().equals(password)) {
        System.out.println("Incorrect password.");
        return null;
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

    if (user == null) {
        return false;
    }

    if (!validateName(newName)) {
        return false;
    }

    if (!validateEmail(newEmail)) {
        return false;
    }

    if (!validatePassword(newPassword)) {
        return false;
    }

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
}