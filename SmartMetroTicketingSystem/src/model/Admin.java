package model;

import enums.UserRole;

/**
 * Admin user class that inherits common user information from the abstract User class.
 */
public class Admin extends User {

// Initializes the Admin object.
    public Admin() {
        super();
    }

// Initializes the Admin object.
    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}