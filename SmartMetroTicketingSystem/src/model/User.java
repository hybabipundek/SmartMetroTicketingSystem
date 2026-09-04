package model;

import enums.UserRole;

/**
 * Abstract base class containing common user information shared by Passenger and Admin.
 */
public abstract class User {

    private String userId;
    private String name;
    private String email;
    private String password;
    private UserRole role;

// Initializes the User object.
    public User() {

    }

// Initializes the User object.
    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

// Returns the userid value.
    public String getUserId() {
        return userId;
    }

// Updates the userid value.
    public void setUserId(String userId) {
        this.userId = userId;
    }

// Returns the name value.
    public String getName() {
        return name;
    }

// Updates the name value.
    public void setName(String name) {
        this.name = name;
    }

// Returns the email value.
    public String getEmail() {
        return email;
    }

// Updates the email value.
    public void setEmail(String email) {
        this.email = email;
    }

// Returns the password value.
    public String getPassword() {
        return password;
    }

// Updates the password value.
    public void setPassword(String password) {
        this.password = password;
    }

// Returns the role value.
    public UserRole getRole() {
        return role;
    }

// Updates the role value.
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User ID: " + userId +
               "\nName: " + name +
               "\nEmail: " + email +
               "\nRole: " + role;
    }

}
