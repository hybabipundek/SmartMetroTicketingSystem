package model;

import enums.UserRole;

public class Passenger extends User {

    private double balance;

    public Passenger() {
        super();
        this.balance = 0.0;
    }

    public Passenger(String userId, String name, String email, String password, double balance) {
        super(userId, name, email, password, UserRole.PASSENGER);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return super.toString() +
               "\nBalance: RM " + String.format("%.2f", balance);
    }

}