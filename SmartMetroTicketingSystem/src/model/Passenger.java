package model;

import enums.UserRole;

/**
 * Passenger user class that inherits from User and stores passenger balance information.
 */
public class Passenger extends User {
    private double balance;

// Initializes the Passenger object.
    public Passenger() {
        super();
        this.balance = 0.0;
    }

// Initializes the Passenger object.
    public Passenger(String userId, String name, String email, String password, double balance) {
        super(userId, name, email, password, UserRole.PASSENGER);
        this.balance = balance;
    }

// Returns the passenger's current account balance.
    public double getBalance() {
        return balance;
    }

// Updates the passenger's account balance.
    public void setBalance(double balance) {
        this.balance = balance;
    }

// Deducts a payment amount from the passenger balance when sufficient funds are available.
    public boolean deductBalance(double amount) {
        if (amount <= 0 || balance < amount) return false;
        balance -= amount;
        return true;
    }

// Adds a valid amount to the passenger's balance.
    public void topUp(double amount) {
        if (amount > 0) balance += amount;
    }

    @Override
    public String toString() {
        return super.toString() +
               "\nBalance: RM " + String.format("%.2f", balance);
    }
}
